package com.smit.tire_change_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.smit.tire_change_app.client.LondonClient;
import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.exceptions.InvalidTireChangeTimeIdException;
import com.smit.tire_change_app.exceptions.NotAvailableTimeException;
import com.smit.tire_change_app.model.AvailableTime;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.model.ContactInformation;
import com.smit.tire_change_app.responses.LondonResponse;
import com.smit.tire_change_app.workshop.AvailTime;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LondonService implements WorkshopService {
    private final LondonClient londonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<AvailTime> getAvailableTimes(String from, String until) throws JAXBException, InvalidDatePeriodException {
        verifyInputs(from, until);

        String untilDateIncluded = untilDateInclusive(until);
        String availableTimesList = londonClient.getAvailableTimes(from, untilDateIncluded);
        LondonResponse response = parseLondonResponse(availableTimesList);
        return filterAndMapFutureEvents(response);
    }


    @Override
    public Booking bookTireChangeTime(String uuid, String contactInformation) throws NotAvailableTimeException, InvalidTireChangeTimeIdException {
        try {
            ContactInformation contactInformation1 = objectMapper.readValue(contactInformation, ContactInformation.class);

            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(contactInformation1);
            Booking booking = londonClient.bookTireChangeTime(uuid, xml);
            booking.setAddress("1A Gunton Rd, London");
            booking.setVehicleType("Car");
            return booking;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid contact information format");
        } catch (FeignException e){
            if (e.status() == 422){
                throw new NotAvailableTimeException("Tire change time not available");
            }
            else if (e.status() == 400){
                throw new InvalidTireChangeTimeIdException("No tire change time with provided ID");
            }
            throw new RuntimeException();
        }
    }

    private LondonResponse parseLondonResponse(String availableTimeList) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(LondonResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (LondonResponse) unmarshaller.unmarshal(new StringReader(availableTimeList));
    }

    private List<AvailTime> filterAndMapFutureEvents(LondonResponse response) {
        if (response.getAvailableTimeList() == null || response.getAvailableTimeList().isEmpty()) {
            return new ArrayList<>();
        }

        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        return response.getAvailableTimeList().stream()
                .filter(availableTime -> ZonedDateTime.parse(availableTime.getTime(), formatter).isAfter(now))
                .map(this::mapToAvailTime)
                .toList();
    }

    private AvailTime mapToAvailTime(AvailableTime londonTime) {
        AvailTime time = new AvailTime();
        time.setUuid(londonTime.getUuid());
        time.setTime(londonTime.getTime());
        time.setAddress("1A Gunton Rd, London");
        time.setVehicleType("Car");
        return time;
    }

    private String untilDateInclusive(String dateString){
        LocalDate date = LocalDate.parse(dateString);
        LocalDate incrementedDate = date.plusDays(1);
        return incrementedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private void verifyInputs(String from, String until) throws InvalidDatePeriodException {
        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate untilDate = LocalDate.parse(until);

            if (fromDate.isAfter(untilDate)) {
                throw new InvalidDatePeriodException("'From' date must not be after 'Until' date.");
            }

        } catch (DateTimeParseException e) {
            throw new InvalidDatePeriodException("Invalid date format. Please use the format 'yyyy-MM-dd'.");
        }
    }
}
