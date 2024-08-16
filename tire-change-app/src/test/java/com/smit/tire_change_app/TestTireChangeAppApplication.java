package com.smit.tire_change_app;

import org.springframework.boot.SpringApplication;

public class TestTireChangeAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(TireChangeAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
