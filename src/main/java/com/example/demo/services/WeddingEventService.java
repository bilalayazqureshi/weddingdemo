package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.WeddingEvent;

@Service
public class WeddingEventService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<WeddingEvent> getAllEvents() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public WeddingEvent getEventById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public WeddingEvent insertNewEvent(WeddingEvent event) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public WeddingEvent updateEventById(long id, WeddingEvent updatedEvent) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void deleteEventById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}
}
