package com.example.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImdbIdService {

	@Value("${search.url}")
	private String searchUrl;
	@Autowired
	private RestTemplate restTemplate;

	@Async
	public ListenableFuture<String> getImdbId(String movie, String year) {
		String url = String.format(searchUrl, movie, year);
		log.debug("in getImdbId for {}", url);
		Map<?, ?> forObject = restTemplate.getForObject(url, Map.class);

		if (forObject != null) {
			log.debug("{} for {}, {}", forObject.toString(), movie, year);
			List<?> list = (List<?>) forObject.get("Search");

			if (list != null && list.size() > 0) {
				Object imdbId = ((Map<?, ?>) list.get(0)).get("imdbID");

				if (imdbId != null) {
					return new AsyncResult<String>((String) imdbId);
				} else {
					throw new RuntimeException(String.format("imdbId is null for %s, %s", movie, year));
				}
			} else {
				throw new RuntimeException(String.format("Search object is empty for %s, %s", movie, year));
			}
		} else {
			throw new RuntimeException(String.format("calling search url failed for %s, %s", movie, year));
		}
	}
}
