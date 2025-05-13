package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Guest;

@Repository
public class GuestRepository {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	// Find all guests
	public List<Guest> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	// Find guest by ID
	public Optional<Guest> findById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	// Save a new or updated guest
	public Guest save(Guest guest) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	// Delete a guest by ID
	public void deleteById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
