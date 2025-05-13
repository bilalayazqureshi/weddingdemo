package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.WeddingEvent;
import com.example.demo.repository.WeddingEventRepository;

class WeddingEventServiceTest {

	@Mock
	private WeddingEventRepository weddingEventRepository;

	private WeddingEventService weddingEventService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		weddingEventService = new WeddingEventService(weddingEventRepository);
	}

	@Test
	public void testGetWeddingEventById() {
		WeddingEvent weddingEvent = new WeddingEvent(1L, "My Wedding", "2025-06-25", "Beachside Resort");
		when(weddingEventRepository.findById(1L)).thenReturn(Optional.of(weddingEvent));

		WeddingEvent result = weddingEventService.getWeddingEventById(1L);

		assertNotNull(result);
		assertEquals("My Wedding", result.getName());
		assertEquals("2025-06-25", result.getDate());
		assertEquals("Beachside Resort", result.getLocation());
		verify(weddingEventRepository, times(1)).findById(1L);
	}

	@Test
	public void testGetWeddingEventByIdNotFound() {
		when(weddingEventRepository.findById(1L)).thenReturn(Optional.empty());

		WeddingEvent result = weddingEventService.getWeddingEventById(1L);

		assertNull(result);
		verify(weddingEventRepository, times(1)).findById(1L);
	}

	@Test
	public void testCreateWeddingEvent() {
		WeddingEvent weddingEvent = new WeddingEvent(null, "Summer Wedding", "2025-07-25", "Mountain Resort");
		WeddingEvent savedWeddingEvent = new WeddingEvent(1L, "Summer Wedding", "2025-07-25", "Mountain Resort");

		when(weddingEventRepository.save(weddingEvent)).thenReturn(savedWeddingEvent);

		WeddingEvent result = weddingEventService.insertNewWeddingEvent(weddingEvent);

		assertNotNull(result);
		assertEquals("Summer Wedding", result.getName());
		assertEquals("2025-07-25", result.getDate());
		assertEquals("Mountain Resort", result.getLocation());
		verify(weddingEventRepository, times(1)).save(weddingEvent);
	}

	@Test
	public void testUpdateWeddingEvent() {
		WeddingEvent existingWeddingEvent = new WeddingEvent(1L, "My Wedding", "2025-06-25", "Beachside Resort");
		WeddingEvent updatedWeddingEvent = new WeddingEvent(1L, "Updated Wedding", "2025-06-25", "Beachside Resort");

		when(weddingEventRepository.findById(1L)).thenReturn(Optional.of(existingWeddingEvent));
		when(weddingEventRepository.save(updatedWeddingEvent)).thenReturn(updatedWeddingEvent);

		WeddingEvent result = weddingEventService.updateWeddingEventById(1L, updatedWeddingEvent);

		assertNotNull(result);
		assertEquals("Updated Wedding", result.getName());
		verify(weddingEventRepository, times(1)).save(updatedWeddingEvent);
	}

	@Test
	public void testDeleteWeddingEvent() {
		long id = 1L;
		doNothing().when(weddingEventRepository).deleteById(id);

		weddingEventService.deleteWeddingEventById(id);

		verify(weddingEventRepository, times(1)).deleteById(id);
	}

	@Test
	public void testGetAllWeddingEvents() {
		WeddingEvent weddingEvent1 = new WeddingEvent(1L, "Wedding 1", "2025-06-25", "Location 1");
		WeddingEvent weddingEvent2 = new WeddingEvent(2L, "Wedding 2", "2025-07-25", "Location 2");
		when(weddingEventRepository.findAll()).thenReturn(List.of(weddingEvent1, weddingEvent2));

		List<WeddingEvent> weddingEvents = weddingEventService.getAllWeddingEvents();

		assertNotNull(weddingEvents);
		assertEquals(2, weddingEvents.size());
		verify(weddingEventRepository, times(1)).findAll();
	}
}
