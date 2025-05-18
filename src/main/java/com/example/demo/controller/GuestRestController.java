package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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


}
