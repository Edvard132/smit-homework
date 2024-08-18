package com.smit.tire_change_app.responses;

import com.smit.tire_change_app.model.AvailableTime;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTimesResponse {

    private List<AvailableTime> availableTimeList;
    private String errorMessage;

}
