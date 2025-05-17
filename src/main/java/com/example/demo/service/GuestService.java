package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.model.Guest;

@Service
public class GuestService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<Guest> getAllGuests() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}
}
