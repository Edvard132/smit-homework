package com.smit.tire_change_app.responses;

import com.smit.tire_change_app.workshop.AvailTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class AvailableTimesResponse {

    private List<AvailTime> availTimeList;
    private String errorMessage;
}
