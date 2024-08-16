package com.smit.tire_change_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TireChangeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TireChangeAppApplication.class, args);
	}

}
