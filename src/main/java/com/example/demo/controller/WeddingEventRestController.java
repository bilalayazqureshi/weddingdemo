package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.WeddingEvent;

@RestController
public class WeddingEventRestController {
	@GetMapping("/api/weddingEvent")
	public List<WeddingEvent> allWeddingEvents() {
		return Collections.emptyList();
	}
}
