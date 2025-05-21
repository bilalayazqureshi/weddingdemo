package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Guest;
import com.example.demo.services.GuestService;

@Controller
@RequestMapping("/guests")
public class GuestWebController {

	@Autowired
	private GuestService guestService;

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String GUEST_ATTRIBUTE = "guest";
	private static final String GUESTS_ATTRIBUTE = "guests";

	// --- LIST / READ ALL ---
	@GetMapping
	public String listGuests(Model model) {
		List<Guest> allGuests = guestService.getAllGuests();
		model.addAttribute(GUESTS_ATTRIBUTE, allGuests);
		model.addAttribute(MESSAGE_ATTRIBUTE, allGuests.isEmpty() ? "No guest" : "");
		return "guest";
	}


}
