package com.smit.tire_change_app.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class BookingResponse {
    private String uuid;
    private Integer id;
    private String time;
    private String address;
    private String vehicleType;
    private String errorMessage;
}
