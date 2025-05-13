package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.WeddingEvent;

@Repository
public class WeddingEventRepository {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	// Find all wedding events
	public List<WeddingEvent> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	// Find wedding event by ID
	public Optional<WeddingEvent> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	// Save a new or updated wedding event
	public WeddingEvent save(WeddingEvent weddingEvent) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	// Delete a wedding event by ID
	public void deleteById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
