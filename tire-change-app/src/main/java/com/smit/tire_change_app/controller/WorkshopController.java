package com.smit.tire_change_app.controller;

import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.exceptions.InvalidTireChangeTimeIdException;
import com.smit.tire_change_app.exceptions.NotAvailableTimeException;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.responses.AvailableTimesResponse;
import com.smit.tire_change_app.responses.BookingResponse;
import com.smit.tire_change_app.workshop.AvailTime;
import com.smit.tire_change_app.service.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workshops")
@CrossOrigin
public class WorkshopController {

    private final Map<Integer, WorkshopService> workshopServices;

    private final BookingResponse bookingResponse;

    private final AvailableTimesResponse availableTimeResponse;

    @Autowired
    public WorkshopController(Map<Integer, WorkshopService> workshopServices, BookingResponse bookingResponse, AvailableTimesResponse availableTimeResponse) {
        this.workshopServices = workshopServices;
        this.bookingResponse = bookingResponse;
        this.availableTimeResponse = availableTimeResponse;
    }

    @GetMapping("/{workshopId}/availableTimes")
    public ResponseEntity<AvailableTimesResponse> getAvailableTimes(@PathVariable Integer workshopId, @RequestParam String from, @RequestParam String until){
        try {
            if (workshopId == 3){
                List<AvailTime> allAvailableTimes = new ArrayList<>();
                for (Integer workshopKey : workshopServices.keySet()) {
                    WorkshopService workshopService = workshopServices.get(workshopKey);
                    if (workshopService == null) {
                        availableTimeResponse.setAvailTimeList(null);
                        availableTimeResponse.setErrorMessage("Workshop not found.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(availableTimeResponse);
                    }
                    allAvailableTimes.addAll(workshopService.getAvailableTimes(from, until));
                }
                availableTimeResponse.setAvailTimeList(allAvailableTimes);
                return ResponseEntity.ok(availableTimeResponse);
            }
            else {
                WorkshopService workshopService = workshopServices.get(workshopId);
                if (workshopService != null) {
                    List<AvailTime> allAvailableTimes = workshopService.getAvailableTimes(from, until);
                    availableTimeResponse.setAvailTimeList(allAvailableTimes);
                    availableTimeResponse.setErrorMessage(null);
                    return ResponseEntity.ok(availableTimeResponse);
                }
                availableTimeResponse.setAvailTimeList(null);
                availableTimeResponse.setErrorMessage("Workshop not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(availableTimeResponse);
            }
        } catch (InvalidDatePeriodException e) {
            availableTimeResponse.setAvailTimeList(null);
            availableTimeResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(availableTimeResponse);
        } catch (JAXBException e) {
            availableTimeResponse.setAvailTimeList(null);
            availableTimeResponse.setErrorMessage("Error processing XML response.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(availableTimeResponse);
        } catch (Exception e) {
            availableTimeResponse.setAvailTimeList(null);
            availableTimeResponse.setErrorMessage("An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(availableTimeResponse);
        }
    }

    @PostMapping(value = "/{workshopId}/bookTime/{tireChangeTimeId}")
    public ResponseEntity<BookingResponse> bookTireChangeTime(@PathVariable Integer workshopId, @PathVariable String tireChangeTimeId, @RequestBody String contactInformation){
        WorkshopService workshopService = workshopServices.get(workshopId);
        if (workshopService == null) {
            bookingResponse.setId(null);
            bookingResponse.setUuid(null);
            bookingResponse.setTime(null);
            bookingResponse.setAddress(null);
            bookingResponse.setVehicleType(null);
            bookingResponse.setErrorMessage("Workshop not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookingResponse);
        }
        try {
            Booking booking = workshopService.bookTireChangeTime(tireChangeTimeId, contactInformation);
            bookingResponse.setUuid(booking.getUuid());
            bookingResponse.setTime(booking.getTime());
            bookingResponse.setVehicleType(booking.getVehicleType());
            bookingResponse.setAddress(booking.getAddress());
            bookingResponse.setErrorMessage(null);
            return ResponseEntity.ok(bookingResponse);
        } catch (NotAvailableTimeException e){
            bookingResponse.setId(null);
            bookingResponse.setUuid(null);
            bookingResponse.setTime(null);
            bookingResponse.setAddress(null);
            bookingResponse.setVehicleType(null);
            bookingResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(bookingResponse);
        }catch (InvalidTireChangeTimeIdException e) {
            bookingResponse.setId(null);
            bookingResponse.setUuid(null);
            bookingResponse.setTime(null);
            bookingResponse.setAddress(null);
            bookingResponse.setVehicleType(null);
            bookingResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookingResponse);
        }catch (Exception e) {
            bookingResponse.setId(null);
            bookingResponse.setUuid(null);
            bookingResponse.setTime(null);
            bookingResponse.setAddress(null);
            bookingResponse.setVehicleType(null);
            bookingResponse.setErrorMessage("Unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bookingResponse);
        }
    }
}
