package com.smit.tire_change_app.model;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Setter
@Component
@RequiredArgsConstructor
@XmlRootElement(name = "tireChangeTimesResponse")
public class LondonAvailableTimes {

    private List<LondonTime> londonTimeList;

    @XmlElement(name = "availableTime")
    public List<LondonTime> getLondonTimeList() {
        return londonTimeList;
    }

}
