package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.WeddingEvent;
import com.example.demo.repositories.WeddingEventRepository;

@Service
public class WeddingEventService {

	private final WeddingEventRepository weddingEventRepository;

	public WeddingEventService(WeddingEventRepository weddingEventRepository) {
		this.weddingEventRepository = weddingEventRepository;
	}

	// Get wedding event by ID
	public WeddingEvent getWeddingEventById(long id) {
		Optional<WeddingEvent> weddingEvent = weddingEventRepository.findById(id);
		return weddingEvent.orElse(null); // Return null if not found
	}

	// Insert new wedding event
	public WeddingEvent insertNewWeddingEvent(WeddingEvent weddingEvent) {
		weddingEvent.setId(null); // Ensure a new ID is generated
		return weddingEventRepository.save(weddingEvent);
	}

	// Delete wedding event by ID
	public void deleteWeddingEventById(long id) {
		weddingEventRepository.deleteById(id); // Delete the wedding event by ID
	}

	// Update wedding event by ID
	public WeddingEvent updateWeddingEventById(long id, WeddingEvent updatedWeddingEvent) {
		updatedWeddingEvent.setId(id); // Set the ID for the updated wedding event
		return weddingEventRepository.save(updatedWeddingEvent); // Save and return the updated wedding event
	}

	// Get all wedding events
	public List<WeddingEvent> getAllWeddingEvents() {
		return weddingEventRepository.findAll(); // Fetch all wedding events
	}

	public List<WeddingEvent> getAllEventsWithGuests() {
		return weddingEventRepository.findAllWithGuests();
	}

}
