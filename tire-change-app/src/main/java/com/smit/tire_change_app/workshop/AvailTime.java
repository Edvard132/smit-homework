package com.smit.tire_change_app.workshop;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailTime {
    private Integer id;
    private String uuid;
    private String time;
    private Boolean available;
    private String address;
    private String vehicleType;

}
