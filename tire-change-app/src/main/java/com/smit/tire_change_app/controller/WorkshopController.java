package com.smit.tire_change_app.controller;

import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.responses.BookingResponse;
import com.smit.tire_change_app.workshop.AvailTime;
import com.smit.tire_change_app.service.WorkshopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workshops")
@Slf4j
@CrossOrigin
public class WorkshopController {

    private final Map<Integer, WorkshopService> workshopServices;

    private final BookingResponse bookingResponse;

    @Autowired
    public WorkshopController(Map<Integer, WorkshopService> workshopServices, BookingResponse bookingResponse) {
        this.workshopServices = workshopServices;
        this.bookingResponse = bookingResponse;
    }

    @GetMapping("/{workshopId}/availableTimes")
    public ResponseEntity<List<AvailTime>> getAvailableTimes(@PathVariable Integer workshopId, @RequestParam String from, @RequestParam String until) throws JAXBException {
        List<AvailTime> allAvailableTimes = new ArrayList<>();

        if (workshopId == 3){
            for (Integer workshopKey : workshopServices.keySet()) {
                WorkshopService workshopService = workshopServices.get(workshopKey);
                if (workshopService != null) {
                    allAvailableTimes.addAll(workshopService.getAvailableTimes(from, until));
                } else {
                    log.error("WorkshopService not found for workshopId: {}", workshopKey);
                }
            }
            return ResponseEntity.ok(allAvailableTimes);
        }
        else {
            WorkshopService workshopService = workshopServices.get(workshopId);
            if (workshopService != null) {
                return ResponseEntity.ok(workshopService.getAvailableTimes(from, until));
            }
            return null;
        }
    }

    @PutMapping("/{workshopId}/bookTime/{uuid}")
    public ResponseEntity<BookingResponse> bookTireChangeTime(@PathVariable Integer workshopId, @PathVariable String uuid, @RequestBody String contactInformation) {
        WorkshopService<String> workshopService = workshopServices.get(workshopId);

        try {
            Booking booking = workshopService.bookTireChangeTime(uuid, contactInformation);
            bookingResponse.setUuid(booking.getUuid());
            bookingResponse.setTime(booking.getTime());
            bookingResponse.setVehicleType(booking.getVehicleType());
            bookingResponse.setAddress(booking.getAddress());
            bookingResponse.setErrorMessage(null);

            return ResponseEntity.ok(bookingResponse);
        } catch (Exception e){
            bookingResponse.setUuid(null);
            bookingResponse.setTime(null);
            bookingResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(bookingResponse);
        }
    }

    @PostMapping(value = "/{workshopId}/bookTime/{tireChangeTimeId}")
    public ResponseEntity<Booking> bookTireChangeTime(@PathVariable Integer workshopId, @PathVariable Integer tireChangeTimeId, @RequestBody String contactInformation){
        WorkshopService<Integer> workshopService = workshopServices.get(workshopId);
        if (workshopService == null) {
            return ResponseEntity.notFound().build();
        }
        Booking booking = workshopService.bookTireChangeTime(tireChangeTimeId, contactInformation);
        return ResponseEntity.ok(booking);
    }
}
