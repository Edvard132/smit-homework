package com.smit.tire_change_app.responses;

import com.smit.tire_change_app.model.AvailableTime;
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
public class LondonResponse {

    private List<AvailableTime> availableTimeList;

    @XmlElement(name = "availableTime")
    public List<AvailableTime> getAvailableTimeList() {
        return availableTimeList;
    }

}
