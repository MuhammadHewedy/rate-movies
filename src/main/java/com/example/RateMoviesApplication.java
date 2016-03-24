package com.example;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.example.model.Tuple2;
import com.example.service.DriverService;

@EnableAsync
@SpringBootApplication
public class RateMoviesApplication implements CommandLineRunner {

	@Autowired
	private DriverService driverService;

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(RateMoviesApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		driverService.drive(Arrays.asList(Tuple2.of("The Age of Adaline", "2015")));
	}
}
