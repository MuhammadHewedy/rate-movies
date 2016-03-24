package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.model.Tuple2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DriverService {

	@Autowired
	private ImdbIdService imdbIdService;
	@Autowired
	private DetailsService rattingService;

	@Async
	public void drive(List<Tuple2<String, String>> movieList) {

		movieList.forEach(t -> imdbIdService.getDetails(t._1, t._2).addCallback(imdbId -> {
			rattingService.getDetails(imdbId).addCallback(rate -> {
				log.info("ratting for {}, {} is {}", t._1, t._2, rate);
			}, e -> {
				log.error(e.getMessage());
			});
		}, e -> {
			log.error(e.getMessage());
		}));
	}
}
