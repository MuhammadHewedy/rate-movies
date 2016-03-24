package com.example.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.AppException;
import com.example.model.Tuple3;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DriverService {

	@Autowired
	private ImdbIdService imdbIdService;
	@Autowired
	private DetailsService rattingService;

	@Async
	public void drive(Stream<Tuple3<String, String, Path>> movieList) {

		movieList.filter(o -> o != null).forEach(t -> imdbIdService.getImdbId(t._1, t._2).addCallback(imdbId -> {
			rattingService.getDetails(imdbId).addCallback(details -> {
				log.info("ratting for {}, {} is {}", t._1, t._2, details);
				try {
					Files.move(t._3, Paths.get(t._3 + " " + details));
				} catch (Exception e1) {
					log.error("faild to rename path {}, {}", t._3, e1.getClass());
				}
			}, e -> {
				log.error(e.getMessage(), e instanceof AppException ? "" : e);
			});
		}, e -> {
			log.error(e.getMessage(), e instanceof AppException ? "" : e);
		}));
	}
}
