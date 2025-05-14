package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Guest;

@RestController
public class GuestRestController {
	@GetMapping("/api/guests")
	public List<Guest> allGuests() {
		return Collections.emptyList();
	}
}
