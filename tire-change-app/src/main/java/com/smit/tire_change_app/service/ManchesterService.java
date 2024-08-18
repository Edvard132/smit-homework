package com.smit.tire_change_app.service;

import com.smit.tire_change_app.client.ManchesterClient;
import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.exceptions.InvalidTireChangeTimeIdException;
import com.smit.tire_change_app.exceptions.NotAvailableTimeException;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.model.AvailableTime;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManchesterService implements WorkshopService {

    private final ManchesterClient manchesterClient;

    @Override
    public List<AvailableTime> getAvailableTimes(String from, String until) throws JAXBException, InvalidDatePeriodException {
        verifyInputs(from, until);
        List<AvailableTime> availableTimes = manchesterClient.getAvailableTimes(from);

        return filterFutureEvents(availableTimes, from, until);
    }

    @Override
    public Booking bookTireChangeTime(String id, String contactInformation) throws NotAvailableTimeException, InvalidTireChangeTimeIdException {
        try {
            Booking booking =  manchesterClient.bookTireChangeTime(Integer.parseInt(id), contactInformation);
            booking.setAddress("14 Bury New Rd, Manchester");
            booking.setVehicleType("Car/Truck");
            return booking;
        }
        catch (FeignException e){
            if (e.status() == 422){
                throw new NotAvailableTimeException("Tire change time not available");
            }
            else if (e.status() == 400){
                throw new InvalidTireChangeTimeIdException("No tire change time with provided ID");
            }
            throw new RuntimeException("Unexpected error occurred while booking tire change time");
        }
    }

    private List<AvailableTime> filterFutureEvents(List<AvailableTime> availableTimes, String from, String until) {

        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        LocalDate fromDate = LocalDate.parse(from, dateFormatter);
        LocalDate untilDate = LocalDate.parse(until, dateFormatter);

        ZonedDateTime rangeStart = fromDate.isBefore(now.toLocalDate()) || fromDate.isEqual(now.toLocalDate())
                ? now
                : fromDate.atStartOfDay(now.getZone());
        ZonedDateTime rangeEnd = untilDate.atTime(23, 59, 59).atZone(now.getZone());

        return availableTimes.stream()
                .filter(availableTime -> {
                    ZonedDateTime eventTime = ZonedDateTime.parse(availableTime.getTime(), dateTimeFormatter);
                    return !eventTime.isBefore(rangeStart) && !eventTime.isAfter(rangeEnd) && availableTime.getAvailable();
                })
                .map(this::mapToAvilTime)
                .toList();
    }

    private AvailableTime mapToAvilTime(AvailableTime manchesterTime){
        AvailableTime time = new AvailableTime();
        time.setId(manchesterTime.getId());
        time.setTime(manchesterTime.getTime());
        time.setAddress("14 Bury New Rd, Manchester");
        time.setVehicleType("Car/Truck");
        return time;
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
