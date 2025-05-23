package com.example.demo.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.WeddingEvent;

@Service
public class WeddingEventService {

	private final Map<Long, WeddingEvent> events = new LinkedHashMap<>();

	public WeddingEventService() {
		// seed with a couple of sample events
		events.put(1L, new WeddingEvent(1L, "Smith–Jones Wedding", LocalDate.of(2025, 6, 15), "Rome"));
		events.put(2L, new WeddingEvent(2L, "Lee–Patel Celebration", LocalDate.of(2025, 7, 20), "Venice"));
	}

	/** List all events in insertion order */
	public List<WeddingEvent> getAllEvents() {
		return new ArrayList<>(events.values());
	}

	/** Lookup one by id, or return null if none */
	public WeddingEvent getEventById(long id) {
		return events.get(id);
	}

	/** Insert a new event, auto-assigning a fresh id */
	public WeddingEvent insertNewEvent(WeddingEvent event) {
		long newId = events.size() + 1L;
		event.setId(newId);
		events.put(newId, event);
		return event;
	}

	/** Overwrite an existing event by id */
	public WeddingEvent updateEventById(long id, WeddingEvent replacement) {
		replacement.setId(id);
		events.put(id, replacement);
		return replacement;
	}

	/** Remove an event by id */
	public void deleteEventById(long id) {
		events.remove(id);
	}
}
