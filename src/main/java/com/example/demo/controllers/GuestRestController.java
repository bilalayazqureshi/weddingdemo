package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Guest;
import com.example.demo.service.GuestService;

@RestController
@RequestMapping("/api/guests")
public class GuestRestController {

	@Autowired
	private GuestService guestService;

	@GetMapping
	public List<Guest> allGuests() {
		return guestService.getAllGuests();
	}

	@GetMapping("/{id}")
	public Guest guest(@PathVariable long id) {
		return guestService.getGuestById(id);
	}

	@PostMapping("/new")
	public Guest newGuest(@RequestBody Guest guest) {
		return guestService.insertNewGuest(guest);
	}

	@PutMapping("/{id}")
	public Guest updateGuest(@PathVariable long id, @RequestBody Guest replacement) {
		return guestService.updateGuestById(id, replacement);
	}

	@DeleteMapping("/{id}")
	public void deleteGuest(@PathVariable long id) {
		guestService.deleteGuestById(id);
	}
}
