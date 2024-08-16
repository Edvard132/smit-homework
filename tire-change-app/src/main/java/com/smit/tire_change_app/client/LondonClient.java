package com.smit.tire_change_app.client;

import com.smit.tire_change_app.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "london", url = "http://localhost:9003/api/v1")
public interface LondonClient {

    @RequestMapping(method = RequestMethod.GET, value = "/tire-change-times/available")
    String getAvailableTimes(@RequestParam String from, @RequestParam String until);

    @PutMapping(value = "/tire-change-times/{uuid}/booking")
    Booking bookTireChangeTime(@PathVariable String uuid, @RequestBody String contactInformation);
}
