package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
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

		Stream<Tuple2<String, String>> movies = Files.list(Paths.get("/Users/mhewedy/Temp/testmovies"))
				.filter(Files::isDirectory).filter(p -> !p.getFileName().toString().contains("imdb"))
				.map(p -> getTuple(p));

		driverService.drive(movies);
	}

	private Tuple2<String, String> getTuple(Path p) {
		try {
			String fileName = p.getFileName().toString();
			return Tuple2.of(fileName.substring(0, fileName.indexOf('(')).trim(),
					fileName.substring(fileName.indexOf('(') + 1, fileName.indexOf(')')).trim());
		} catch (Exception ex) {
			log.error(ex.getMessage() + " for path: " + p);
			return null;
		}
	}
}
