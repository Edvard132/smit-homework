package com.smit.tire_change_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Booking {

    private String uuid;
    private Integer id;
    private String time;
    private String errorMessage;
}
