package com.smit.tire_change_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.smit.tire_change_app.client.LondonClient;
import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.model.ContactInformation;
import com.smit.tire_change_app.responses.LondonResponse;
import com.smit.tire_change_app.workshop.AvailTime;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LondonService implements WorkshopService<String> {
    private final LondonClient londonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<AvailTime> getAvailableTimes(String from, String until) throws JAXBException {
        String availableTimesList = londonClient.getAvailableTimes(from, until);
        return filterFutureEvents(availableTimesList);
    }

    private List<AvailTime> filterFutureEvents(String availableTimeList) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(LondonResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        LondonResponse response = (LondonResponse) unmarshaller.unmarshal(new StringReader(availableTimeList));
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        return response.getAvailableTimeList().stream()
                .filter(availableTime -> {
                    ZonedDateTime availableTimeZonedDateTime = ZonedDateTime.parse(availableTime.getTime(), formatter);
                    return availableTimeZonedDateTime.isAfter(now);
                })
                .map(londonTime -> {
                    AvailTime time = new AvailTime();
                    time.setUuid(londonTime.getUuid());
                    time.setTime(londonTime.getTime());
                    time.setAddress("1A Gunton Rd, London");
                    time.setVehicleType("Car");
                    return time;
                })
                .toList();
    }

    @Override
    public Booking bookTireChangeTime(String uuid, String contactInformation) {
        try {
            ContactInformation contactInformation1 = objectMapper.readValue(contactInformation, ContactInformation.class);

            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(contactInformation1);
            Booking booking = londonClient.bookTireChangeTime(uuid, xml);
            booking.setAddress("1A Gunton Rd, London");
            booking.setVehicleType("Car");
            return booking;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void verifyInputs(String from, String until) throws InvalidDatePeriodException {
        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate untilDate = LocalDate.parse(until);

            if (!fromDate.isBefore(untilDate)) {
                throw new InvalidDatePeriodException("'From' date must be before 'Until' date.");
            }

        } catch (DateTimeParseException e) {
            throw new InvalidDatePeriodException("Invalid date format. Please use the format 'yyyy-MM-dd'.");
        }
    }
}
