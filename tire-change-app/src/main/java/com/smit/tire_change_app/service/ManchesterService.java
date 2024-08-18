package com.smit.tire_change_app.service;

import com.smit.tire_change_app.client.ManchesterClient;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.workshop.AvailTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManchesterService implements WorkshopService<Integer> {

    private final ManchesterClient manchesterClient;

    @Override
    public List<AvailTime> getAvailableTimes(String from, String until) throws JAXBException {
        List<AvailTime> availableTimes = manchesterClient.getAvailableTimes(from);

        return filterFutureEvents(availableTimes, from, until);
    }

    @Override
    public Booking bookTireChangeTime(Integer id, String contactInformation) {
        Booking booking =  manchesterClient.bookTireChangeTime(id, contactInformation);
        booking.setAddress("14 Bury New Rd, Manchester");
        booking.setVehicleType("Car/Truck");
        return booking;
    }

    public List<AvailTime> filterFutureEvents(List<AvailTime> availableTimes, String from, String until) {

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
                .map(manchesterTime -> {
                    AvailTime time = new AvailTime();
                    time.setId(manchesterTime.getId());
                    time.setTime(manchesterTime.getTime());
                    time.setAddress("14 Bury New Rd, Manchester");
                    time.setVehicleType("Car/Truck");
                    return time;
                })
                .toList();
    }
}
