package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Guest;
import com.example.demo.model.WeddingEvent;
import com.example.demo.service.GuestService;
import com.example.demo.service.WeddingEventService;

@Controller
@RequestMapping("/guests")
public class GuestWebController {

	@Autowired
	private GuestService guestService;

	@Autowired
	private WeddingEventService weddingEventService;

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String GUEST_ATTRIBUTE = "guest";
	private static final String GUESTS_ATTRIBUTE = "guests";

	@GetMapping
	public String listGuests(Model model) {
		List<Guest> allGuests = guestService.getAllGuests();
		model.addAttribute(GUESTS_ATTRIBUTE, allGuests);
		model.addAttribute(MESSAGE_ATTRIBUTE, allGuests.isEmpty() ? "No guest" : "");
		return "guest";
	}

	@GetMapping("/edit/{id}")
	public String editGuest(@PathVariable long id, Model model) {
		Guest guest = guestService.getGuestById(id);
		List<WeddingEvent> weddingEvent = weddingEventService.getAllWeddingEvents();
		model.addAttribute(GUEST_ATTRIBUTE, guest);
		model.addAttribute("events", weddingEvent);
		model.addAttribute(MESSAGE_ATTRIBUTE, guest == null ? "No guest found with id: " + id : "");
		return "edit_guest";
	}

	@GetMapping("/new")
	public String newGuest(Model model) {
		model.addAttribute(GUEST_ATTRIBUTE, new Guest());
		List<WeddingEvent> weddingEvent = weddingEventService.getAllWeddingEvents();
		model.addAttribute("events", weddingEvent);
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		return "edit_guest";
	}

	@PostMapping("/save")
	public String saveGuest(Guest guest) {
		// guest.getEvent().getId() is now non-null
		if (guest.getEvent() != null && guest.getEvent().getId() != null) {
			// look up the real event object
			WeddingEvent e = weddingEventService.getWeddingEventById(guest.getEvent().getId());
			guest.setEvent(e);
		}

		if (guest.getId() == null) {
			guestService.insertNewGuest(guest);
		} else {
			guestService.updateGuestById(guest.getId(), guest);
		}
		return "redirect:/guests";
	}

	@GetMapping("/delete/{id}")
	public String deleteGuest(@PathVariable long id, Model model) {
		guestService.deleteGuestById(id);
		model.addAttribute("deletedId", id);
		return "delete_guest";
	}

}
