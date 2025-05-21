package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.demo.model.WeddingEvent;

class tempTest {

	@Test
	public void testWeddingEventCreation() {
		WeddingEvent weddingEvent = new WeddingEvent(1L, "My Wedding", LocalDate.of(2025, 1, 1), "Beachside Resort");

		assertNotNull(weddingEvent);
		assertEquals(1L, weddingEvent.getId());
		assertEquals("My Wedding", weddingEvent.getName());
		assertEquals(LocalDate.of(2025, 1, 1), weddingEvent.getDate());
		assertEquals("Beachside Resort", weddingEvent.getLocation());
	}
}
