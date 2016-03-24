package com.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.example.model.Tuple2;
import com.example.service.DriverService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		if (arg0 == null || arg0.length < 1) {
			log.info("provide system path");
			System.exit(-1);
		}

		Stream<Tuple2<String, String>> movies = Files.list(Paths.get(arg0[0])).filter(Files::isDirectory).map(p -> {
			String fileName = p.getFileName().toString();
			return Tuple2.of(fileName.substring(0, fileName.indexOf('(')).trim(),
					fileName.substring(fileName.indexOf('(') + 1, fileName.indexOf(')')).trim());
		});

		driverService.drive(movies);
	}
}
