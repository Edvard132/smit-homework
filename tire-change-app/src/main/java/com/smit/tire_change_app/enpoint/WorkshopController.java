package com.smit.tire_change_app.enpoint;

import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.exceptions.InvalidTireChangeTimeIdException;
import com.smit.tire_change_app.exceptions.InvalidWorkshopIdException;
import com.smit.tire_change_app.exceptions.NotAvailableTimeException;
import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.responses.AvailableTimesResponse;
import com.smit.tire_change_app.responses.BookingResponse;
import com.smit.tire_change_app.service.WorkshopServiceAggregator;
import com.smit.tire_change_app.model.AvailableTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workshops")
@CrossOrigin
public class WorkshopController {

    private final WorkshopServiceAggregator workshopServiceAggregator;

    private final BookingResponse bookingResponse;

    private final AvailableTimesResponse availableTimeResponse;

    @Autowired
    public WorkshopController(BookingResponse bookingResponse, AvailableTimesResponse availableTimeResponse, WorkshopServiceAggregator workshopServiceAggregator) {
        this.bookingResponse = bookingResponse;
        this.availableTimeResponse = availableTimeResponse;
        this.workshopServiceAggregator = workshopServiceAggregator;
    }

    @GetMapping("/{workshopId}/availableTimes")
    public ResponseEntity<AvailableTimesResponse> getAvailableTimes(@PathVariable Integer workshopId, @RequestParam String from, @RequestParam String until){
        try {
            List<AvailableTime> availableTimeList = workshopServiceAggregator.getAvailableTimes(workshopId, from, until);

            availableTimeResponse.setAvailableTimeList(availableTimeList);
            availableTimeResponse.setErrorMessage(null);
            return ResponseEntity.ok(availableTimeResponse);
        } catch (InvalidDatePeriodException e) {
            availableTimeResponse.setAvailableTimeList(null);
            availableTimeResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(availableTimeResponse);
        } catch (InvalidWorkshopIdException e) {
            availableTimeResponse.setAvailableTimeList(null);
            availableTimeResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(availableTimeResponse);
        } catch (JAXBException e) {
            availableTimeResponse.setAvailableTimeList(null);
            availableTimeResponse.setErrorMessage("Error processing XML response.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(availableTimeResponse);
        } catch (Exception e) {
            availableTimeResponse.setAvailableTimeList(null);
            availableTimeResponse.setErrorMessage("An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(availableTimeResponse);
        }
    }

    @PostMapping(value = "/{workshopId}/bookTime/{tireChangeTimeId}")
    public ResponseEntity<BookingResponse> bookTireChangeTime(@PathVariable Integer workshopId, @PathVariable String tireChangeTimeId, @RequestBody String contactInformation){
        try {
            Booking booking = workshopServiceAggregator.bookTireChangeTime(workshopId,tireChangeTimeId, contactInformation);

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
        } catch (InvalidWorkshopIdException e) {
            bookingResponse.setId(null);
            bookingResponse.setUuid(null);
            bookingResponse.setTime(null);
            bookingResponse.setAddress(null);
            bookingResponse.setVehicleType(null);
            bookingResponse.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookingResponse);
        } catch (InvalidTireChangeTimeIdException e) {
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
