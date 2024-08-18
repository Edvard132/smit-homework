package com.smit.tire_change_app.service;

import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.exceptions.InvalidTireChangeTimeIdException;
import com.smit.tire_change_app.exceptions.InvalidWorkshopIdException;
import com.smit.tire_change_app.exceptions.NotAvailableTimeException;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.model.AvailableTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WorkshopServiceAggregator {

    private final Map<Integer, WorkshopService> workshopServices;

    @Autowired
    public WorkshopServiceAggregator(Map<Integer, WorkshopService> workshopServices) {
        this.workshopServices = workshopServices;
    }
    public List<AvailableTime> getAvailableTimes(Integer workshopId, String from, String until) throws InvalidDatePeriodException, JAXBException, InvalidWorkshopIdException {
        if (workshopId == 3) {
            List<AvailableTime> allAvailableTimes = new ArrayList<>();
            for (WorkshopService service : workshopServices.values()) {
                allAvailableTimes.addAll(service.getAvailableTimes(from, until));
            }
            return allAvailableTimes;
        } else {
            WorkshopService workshopService = workshopServices.get(workshopId);
            if (workshopService == null){
                throw new InvalidWorkshopIdException("Invalid workshop ID.");
            }
            return workshopService.getAvailableTimes(from, until);
        }
    }

    public Booking bookTireChangeTime(Integer workshopId, String bookingId, String contactInformation) throws InvalidWorkshopIdException, InvalidTireChangeTimeIdException, NotAvailableTimeException {
        WorkshopService workshopService = workshopServices.get(workshopId);
        if (workshopService == null){
            throw new InvalidWorkshopIdException("Invalid workshop ID.");
        }
        return workshopService.bookTireChangeTime(bookingId, contactInformation);
    }

}
