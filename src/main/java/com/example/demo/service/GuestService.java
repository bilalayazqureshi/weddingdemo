package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Guest;
import com.example.demo.repositories.GuestRepository;

import jakarta.transaction.Transactional;

@Service
public class GuestService {

	private final GuestRepository guestRepository;
	
	public GuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	// Get guest by ID
	@Transactional
	public Guest getGuestById(long id) {
		Optional<Guest> guest = guestRepository.findById(id);
		return guest.orElse(null); // Return null if not found
	}

	// Insert new guest
	@Transactional
	public Guest insertNewGuest(Guest guest) {
		guest.setId(null); // Ensure a new ID is generated
		return guestRepository.save(guest);
	}

	// Update guest by ID
	@Transactional
	public Guest updateGuestById(long id, Guest updatedGuest) {
		updatedGuest.setId(id); // Set the ID for the updated guest
		return guestRepository.save(updatedGuest); // Save and return the updated guest
	}

	// Delete guest by ID
	@Transactional
	public void deleteGuestById(long id) {
		guestRepository.deleteById(id); // Delete the guest by ID
	}

	// Get all guests
	@Transactional
	public List<Guest> getAllGuests() {
		return guestRepository.findAll(); // Fetch all guests
	}
}
