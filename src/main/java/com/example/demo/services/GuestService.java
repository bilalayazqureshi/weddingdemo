package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Guest;

@Service
public class GuestService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public Guest getGuestById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Guest insertNewGuest(Guest guest) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Guest updateGuestById(long id, Guest updatedGuest) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public void deleteGuestById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public List<Guest> getAllGuests() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

}
