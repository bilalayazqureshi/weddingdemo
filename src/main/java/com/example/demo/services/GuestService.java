package com.example.demo.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.Guest;

@Service
public class GuestService {

	private final Map<Long, Guest> guests = new LinkedHashMap<>();

	public GuestService() {
		// seed with a couple of sample guests
		guests.put(1L, new Guest(1L, "Alice", "alice@example.com"));
		guests.put(2L, new Guest(2L, "Bob", "bob@example.com"));
	}

	/** List all guests in insertion order */
	public List<Guest> getAllGuests() {
		return new ArrayList<>(guests.values());
	}

	/** Lookup one by id, or return null if none */
	public Guest getGuestById(long id) {
		return guests.get(id);
	}

	/** Insert a new guest, auto-assigning a fresh id */
	public Guest insertNewGuest(Guest guest) {
		long newId = guests.size() + 1L;
		guest.setId(newId);
		guests.put(newId, guest);
		return guest;
	}

	/** Overwrite an existing guest by id */
	public Guest updateGuestById(long id, Guest replacement) {
		replacement.setId(id);
		guests.put(id, replacement);
		return replacement;
	}

	/** Remove a guest by id */
	public void deleteGuestById(long id) {
		guests.remove(id);
	}

	public List<Guest> findGuestsForEvent(long eventId) {
		List<Guest> result = new ArrayList<>();
		for (Guest g : guests.values()) {
			if (g.getEvent() != null && g.getEvent().getId().equals(eventId)) {
				result.add(g);
			}
		}
		return result;
	}
}
