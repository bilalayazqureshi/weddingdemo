package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WeddingEvent;
import com.example.demo.service.WeddingEventService;

@RestController
@RequestMapping("/api/weddingevents")
public class WeddingEventRestController {

	@Autowired
	private WeddingEventService weddingEventService;

	@GetMapping
	public List<WeddingEvent> allWeddingEvents() {
		return weddingEventService.getAllWeddingEvents();
	}
	

}
