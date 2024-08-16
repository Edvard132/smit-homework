package com.smit.tire_change_app.service;

import com.smit.tire_change_app.service.LondonService;
import com.smit.tire_change_app.service.ManchesterService;
import com.smit.tire_change_app.service.WorkshopService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class WorkshopServiceConfig {
    @Bean
    public Map<Integer, WorkshopService> workshopServices(LondonService londonService, ManchesterService manchesterWorkshopServicev2) {
        Map<Integer, WorkshopService> workshopServices = new HashMap<>();

        workshopServices.put(1, londonService);
        workshopServices.put(2, manchesterWorkshopServicev2);

        return workshopServices;
    }
}
