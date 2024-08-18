package com.smit.tire_change_app.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@RequiredArgsConstructor
@XmlRootElement(name = "availableTime")
public class LondonTime {
    private String uuid;
    private String time;
    private String address = "1A Gunton Rd, London";
    private String vehicleType = "Car";
    @XmlElement
    public String getUuid() {
        return uuid;
    }

    @XmlElement
    public String getTime() {
        return time;
    }

}
