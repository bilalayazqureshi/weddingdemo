package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.demo.model.WeddingEvent;

class tempTest {

	@Test
	public void testWeddingEventCreation() {
		WeddingEvent weddingEvent = new WeddingEvent(1L, "My Wedding", "2025-06-25", "Beachside Resort");

		assertNotNull(weddingEvent);
		assertEquals(1L, weddingEvent.getId());
		assertEquals("My Wedding", weddingEvent.getName());
		assertEquals("2025-06-25", weddingEvent.getDate());
		assertEquals("Beachside Resort", weddingEvent.getLocation());
	}
}
