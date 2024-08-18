package com.smit.tire_change_app.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private String time;
    private String address;
    private String vehicleType;
    private String errorMessage;
}
