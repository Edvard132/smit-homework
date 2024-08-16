package com.smit.tire_change_app.service;

import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.workshop.AvailTime;

import javax.xml.bind.JAXBException;
import java.util.List;

public interface WorkshopService<T> {
    List<AvailTime> getAvailableTimes(String from, String until) throws JAXBException;
    Booking bookTireChangeTime(T bookingId, String contactInformation);


}
