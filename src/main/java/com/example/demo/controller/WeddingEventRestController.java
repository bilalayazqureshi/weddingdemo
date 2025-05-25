package com.example.demo.controller;

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

	@GetMapping("/{id}")
	public WeddingEvent weddingEvent(@PathVariable long id) {
		return weddingEventService.getWeddingEventById(id);
	}

	@PostMapping("/new")
	public WeddingEvent create(@RequestBody WeddingEvent evt) {
		return weddingEventService.insertNewWeddingEvent(evt);
	}

	@PutMapping("/{id}")
	public WeddingEvent update(@PathVariable long id, @RequestBody WeddingEvent evt) {
		return weddingEventService.updateWeddingEventById(id, evt);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		weddingEventService.deleteWeddingEventById(id);
	}
}
