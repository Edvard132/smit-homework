package com.smit.tire_change_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Booking {

    private String uuid;
    private Integer id;
    private String time;
    private String address;
    private String vehicleType;
    private String errorMessage;
}
