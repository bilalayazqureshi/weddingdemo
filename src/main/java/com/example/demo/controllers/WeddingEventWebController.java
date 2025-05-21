package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.WeddingEvent;
import com.example.demo.services.WeddingEventService;

@Controller
@RequestMapping("/events")
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
		return "weddingevent";
	}

}
