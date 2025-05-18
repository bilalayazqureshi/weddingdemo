package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.model.WeddingEvent;

@Service
public class WeddingEventService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<WeddingEvent> getAllWeddingEvents() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public WeddingEvent getWeddingEventById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public WeddingEvent insertNewWeddingEvent(WeddingEvent event) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public WeddingEvent updateWeddingEventById(long id, WeddingEvent replacement) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void deleteWeddingEventById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}
}
