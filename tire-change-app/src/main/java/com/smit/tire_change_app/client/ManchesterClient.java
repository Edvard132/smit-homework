package com.smit.tire_change_app.client;

import com.smit.tire_change_app.model.Booking;
import com.smit.tire_change_app.workshop.AvailTime;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "manchester", url = "http://localhost:9004/api/v2")
public interface ManchesterClient {

    @RequestMapping(method = RequestMethod.GET, value = "/tire-change-times")
    List<AvailTime> getAvailableTimes(@RequestParam String from);

    @PostMapping(value = "/tire-change-times/{id}/booking")
    Booking bookTireChangeTime(@PathVariable Integer id, @RequestBody String contactInformation);
}
