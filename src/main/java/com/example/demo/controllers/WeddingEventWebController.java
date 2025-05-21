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

import com.example.demo.model.WeddingEvent;
import com.example.demo.services.WeddingEventService;

@Controller
@RequestMapping("/")
public class WeddingEventWebController {
	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String EVENT_ATTRIBUTE = "event";
	private static final String EVENTS_ATTRIBUTE = "events";

	@Autowired
	private WeddingEventService weddingEventService;

	@GetMapping
	public String listEvents(Model model) {
		List<WeddingEvent> allEvents = weddingEventService.getAllEvents();
		model.addAttribute(EVENTS_ATTRIBUTE, allEvents);
		model.addAttribute(MESSAGE_ATTRIBUTE, allEvents.isEmpty() ? "No event" : "");
		return "index";
	}

	@GetMapping("/edit/{id}")
	public String editEvent(@PathVariable long id, Model model) {
		WeddingEvent evt = weddingEventService.getEventById(id);
		model.addAttribute(EVENT_ATTRIBUTE, evt);
		model.addAttribute(MESSAGE_ATTRIBUTE, evt == null ? "No event found with id: " + id : "");
		return "edit_event";
	}

	@GetMapping("/new")
	public String newEvent(Model model) {
		model.addAttribute(EVENT_ATTRIBUTE, new WeddingEvent());
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		return "index";
	}

	@PostMapping("/save")
	public String saveEvent(WeddingEvent event) {
		if (event.getId() == null) {
			weddingEventService.insertNewEvent(event);
		} else {
			weddingEventService.updateEventById(event.getId(), event);
		}
		return "redirect:/index";
	}

	@DeleteMapping("/delete/{id}")
	public String deleteEvent(@PathVariable long id) {
		weddingEventService.deleteEventById(id);
		return "redirect:/index";
	}

}
