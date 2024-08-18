package com.smit.tire_change_app.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTime {
    private Integer id;
    private String uuid;
    private String time;
    private Boolean available;
    private String address;
    private String vehicleType;

}
