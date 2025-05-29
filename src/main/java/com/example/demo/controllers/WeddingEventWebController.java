package com.example.demo.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.WeddingEvent;
import com.example.demo.service.GuestService;
import com.example.demo.service.WeddingEventService;

@Controller
@RequestMapping("/")
public class WeddingEventWebController {
	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String EVENT_ATTRIBUTE = "event";
	private static final String EVENTS_ATTRIBUTE = "events";

	private WeddingEventService weddingEventService;

	private GuestService guestService;

	public WeddingEventWebController(GuestService guestService, WeddingEventService weddingEventService) {
		this.guestService = guestService;
		this.weddingEventService = weddingEventService;
	}

	@GetMapping
	public String listEvents(Model model) {
		List<WeddingEvent> allEvents = weddingEventService.getAllWeddingEvents();
		model.addAttribute(EVENTS_ATTRIBUTE, allEvents);
		for (WeddingEvent ev : allEvents) {
			ev.setGuest(guestService.findGuestsForEvent(ev.getId()));
		}
		model.addAttribute(MESSAGE_ATTRIBUTE, allEvents.isEmpty() ? "No event" : "");
		return "index";
	}

	@GetMapping("/edit/{id}")
	public String editEvent(@PathVariable long id, Model model) {
		WeddingEvent evt = weddingEventService.getWeddingEventById(id);
		model.addAttribute(EVENT_ATTRIBUTE, evt);
		model.addAttribute(MESSAGE_ATTRIBUTE, evt == null ? "No event found with id: " + id : "");
		return "edit_event";
	}

	@GetMapping("/new")
	public String newEvent(Model model) {
		model.addAttribute(EVENT_ATTRIBUTE, new WeddingEvent());
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		return "edit_event";
	}

	@PostMapping("/save")
	public String saveEvent(WeddingEvent event) {
		if (event.getId() == null) {
			weddingEventService.insertNewWeddingEvent(event);
		} else {
			weddingEventService.updateWeddingEventById(event.getId(), event);
		}
		return "redirect:/";
	}

	@GetMapping("/delete/{id}")
	public String deleteEvent(@PathVariable long id, Model model) {
		weddingEventService.deleteWeddingEventById(id);
		model.addAttribute("deletedId", id);
		return "delete_event";
	}

}
