package com.smit.tire_change_app.enpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smit.tire_change_app.exceptions.InvalidDatePeriodException;
import com.smit.tire_change_app.exceptions.InvalidWorkshopIdException;
import com.smit.tire_change_app.model.AvailableTime;
import com.smit.tire_change_app.responses.AvailableTimesResponse;
import com.smit.tire_change_app.service.WorkshopServiceAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class WorkshopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkshopServiceAggregator workshopServiceAggregator;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testShouldReturnManchesterTime() throws Exception, InvalidDatePeriodException, InvalidWorkshopIdException {

        AvailableTime availableTime = new AvailableTime();
        availableTime.setId(1);
        availableTime.setTime("2024-08-26T10:00:00Z");
        availableTime.setAddress("14 Bury New Rd, Manchester");
        availableTime.setAvailable(true);
        availableTime.setVehicleType("Car/Truck");

        AvailableTimesResponse response = new AvailableTimesResponse();
        response.setAvailableTimeList(List.of(availableTime));
        response.setErrorMessage(null);

        Mockito.when(workshopServiceAggregator.getAvailableTimes(2, "2024-08-20", "2024-08-28"))
                .thenReturn(List.of(availableTime));

        MvcResult result = mockMvc.perform(get("/api/v1/workshops/2/availableTimes")
                        .param("from", "2024-08-20")
                        .param("until", "2024-08-28")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.availableTimeList[0].id").value(1))
                .andExpect(jsonPath("$.availableTimeList[0].time").value("2024-08-26T10:00:00Z"))
                .andExpect(jsonPath("$.availableTimeList[0].available").value(true))
                .andExpect(jsonPath("$.availableTimeList[0].vehicleType").value("Car/Truck"))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andReturn();

        AvailableTimesResponse apiResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AvailableTimesResponse.class);
        assert apiResponse.getAvailableTimeList().getFirst().getAvailable();
        assert Objects.equals(apiResponse.getAvailableTimeList().getFirst().getVehicleType(), "Car/Truck");
        assert apiResponse.getErrorMessage() == null;
    }

    @Test
    public void testShouldReturnLondonTime() throws Exception, InvalidDatePeriodException, InvalidWorkshopIdException {

        AvailableTime availableTime = new AvailableTime();
        availableTime.setUuid("0c26b0bb-7d53-470a-8ada-2ad232af9ffb");
        availableTime.setTime("2024-08-26T10:00:00Z");
        availableTime.setAddress("1A Gunton Rd, London");
        availableTime.setVehicleType("Car");

        AvailableTimesResponse response = new AvailableTimesResponse();
        response.setAvailableTimeList(List.of(availableTime));
        response.setErrorMessage(null);

        Mockito.when(workshopServiceAggregator.getAvailableTimes(1, "2024-08-20", "2024-08-28"))
                .thenReturn(List.of(availableTime));

        MvcResult result = mockMvc.perform(get("/api/v1/workshops/1/availableTimes")
                        .param("from", "2024-08-20")
                        .param("until", "2024-08-28")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.availableTimeList[0].uuid").value("0c26b0bb-7d53-470a-8ada-2ad232af9ffb"))
                .andExpect(jsonPath("$.availableTimeList[0].time").value("2024-08-26T10:00:00Z"))
                .andExpect(jsonPath("$.availableTimeList[0].address").value("1A Gunton Rd, London"))
                .andExpect(jsonPath("$.availableTimeList[0].vehicleType").value("Car"))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andReturn();

        AvailableTimesResponse apiResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AvailableTimesResponse.class);
        assert Objects.equals(apiResponse.getAvailableTimeList().getFirst().getVehicleType(), "Car");
        assert apiResponse.getErrorMessage() == null;
    }

    @Test
    public void testShouldReturnAllTimes() throws Exception, InvalidDatePeriodException, InvalidWorkshopIdException {

        AvailableTime availableTime1 = new AvailableTime();
        availableTime1.setUuid("0c26b0bb-7d53-470a-8ada-2ad232af9ffb");
        availableTime1.setTime("2024-08-26T10:00:00Z");
        availableTime1.setAddress("1A Gunton Rd, London");
        availableTime1.setVehicleType("Car");

        AvailableTime availableTime2 = new AvailableTime();
        availableTime2.setId(1);
        availableTime2.setTime("2024-08-26T10:00:00Z");
        availableTime2.setAddress("14 Bury New Rd, Manchester");
        availableTime2.setAvailable(true);
        availableTime2.setVehicleType("Car/Truck");

        AvailableTimesResponse response = new AvailableTimesResponse();
        response.setAvailableTimeList(List.of(availableTime1, availableTime2));
        response.setErrorMessage(null);

        Mockito.when(workshopServiceAggregator.getAvailableTimes(3, "2024-08-20", "2024-08-28"))
                .thenReturn(List.of(availableTime1, availableTime2));

        MvcResult result = mockMvc.perform(get("/api/v1/workshops/3/availableTimes")
                        .param("from", "2024-08-20")
                        .param("until", "2024-08-28")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.availableTimeList[0].uuid").value("0c26b0bb-7d53-470a-8ada-2ad232af9ffb"))
                .andExpect(jsonPath("$.availableTimeList[0].time").value("2024-08-26T10:00:00Z"))
                .andExpect(jsonPath("$.availableTimeList[0].address").value("1A Gunton Rd, London"))
                .andExpect(jsonPath("$.availableTimeList[0].vehicleType").value("Car"))
                .andExpect(jsonPath("$.availableTimeList[1].id").value(1))
                .andExpect(jsonPath("$.availableTimeList[1].time").value("2024-08-26T10:00:00Z"))
                .andExpect(jsonPath("$.availableTimeList[1].available").value(true))
                .andExpect(jsonPath("$.errorMessage").doesNotExist())
                .andReturn();

        AvailableTimesResponse apiResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AvailableTimesResponse.class);
        assert Objects.equals(apiResponse.getAvailableTimeList().getFirst().getVehicleType(), "Car");
        assert Objects.equals(apiResponse.getAvailableTimeList().get(1).getVehicleType(), "Car/Truck");
        assert apiResponse.getErrorMessage() == null;
    }

    @Test
    public void testInvalidDatePeriod() throws Exception, InvalidDatePeriodException, InvalidWorkshopIdException {

        Mockito.when(workshopServiceAggregator.getAvailableTimes(1, "2024-08-20", "2024-08-19"))
                .thenThrow(new InvalidDatePeriodException("'From' date must not be after 'Until' date."));

        MvcResult result = mockMvc.perform(get("/api/v1/workshops/1/availableTimes")
                        .param("from", "2024-08-20")
                        .param("until", "2024-08-19")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("'From' date must not be after 'Until' date."))
                .andExpect(jsonPath("$.availableTimeList").isEmpty())
                .andReturn();

        AvailableTimesResponse apiResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AvailableTimesResponse.class);
        assert apiResponse.getAvailableTimeList() == null;
        assert Objects.equals(apiResponse.getErrorMessage(), "'From' date must not be after 'Until' date.");
    }


    @Test
    public void testInvalidWorkshopId() throws Exception, InvalidDatePeriodException, InvalidWorkshopIdException {

        Mockito.when(workshopServiceAggregator.getAvailableTimes(anyInt(), anyString(), anyString()))
                .thenThrow(new InvalidWorkshopIdException("Invalid workshop ID"));

        MvcResult result = mockMvc.perform(get("/api/v1/workshops/999/availableTimes")
                        .param("from", "2024-08-20")
                        .param("until", "2024-08-28")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Invalid workshop ID"))
                .andExpect(jsonPath("$.availableTimeList").isEmpty())
                .andReturn();

        AvailableTimesResponse apiResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AvailableTimesResponse.class);
        assert apiResponse.getAvailableTimeList() == null;
        assert Objects.equals(apiResponse.getErrorMessage(), "Invalid workshop ID");
    }

}
