package com.example.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import com.example.AppException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DetailsService {

	@Value("${details.url}")
	private String detailsUrl;
	@Autowired
	private RestTemplate restTemplate;

	@Async
	public ListenableFuture<String> getDetails(String imdbId) {
		String url = String.format(detailsUrl, imdbId);
		log.debug("in getImdbRating for {}", url);
		Map<?, ?> forObject = restTemplate.getForObject(url, Map.class);

		if (forObject != null) {
			log.debug("{} for {}", forObject.toString(), imdbId);

			Object imdbRating = forObject.get("imdbRating");
			Object rated = forObject.get("Rated");

			if (imdbRating == null) {
				log.error("imdbRating for {} is null", imdbId);
			}
			if (rated == null) {
				log.error("rated for {} is null", imdbId);
			}

			String result = "imdb (" + (imdbRating != null ? imdbRating : "") + ") - " + (rated != null ? rated : "");
			return new AsyncResult<String>(result);
		} else {
			throw new AppException(String.format("calling details url failed for %s", imdbId));
		}
	}
}
