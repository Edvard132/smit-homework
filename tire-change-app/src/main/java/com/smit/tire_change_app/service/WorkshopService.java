package com.smit.tire_change_app.service;

import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.exceptions.InvalidTireChangeTimeIdException;
import com.smit.tire_change_app.exceptions.NotAvailableTimeException;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.model.AvailableTime;

import javax.xml.bind.JAXBException;
import java.util.List;

public interface WorkshopService {
    List<AvailableTime> getAvailableTimes(String from, String until) throws JAXBException, InvalidDatePeriodException;

    Booking bookTireChangeTime(String bookingId, String contactInformation) throws NotAvailableTimeException, InvalidTireChangeTimeIdException;

}
