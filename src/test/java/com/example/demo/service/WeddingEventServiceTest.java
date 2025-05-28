package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Guest;
import com.example.demo.model.WeddingEvent;
import com.example.demo.repositories.GuestRepository;
import com.example.demo.repositories.WeddingEventRepository;

class WeddingEventServiceTest {

	@Mock
	private WeddingEventRepository weddingEventRepository;

	@Mock
	private GuestRepository guestRepository;

	private WeddingEventService weddingEventService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		weddingEventService = new WeddingEventService(weddingEventRepository);
	}

	@Test
	void testGetWeddingEventById() {
		WeddingEvent weddingEvent = new WeddingEvent(1L, "My Wedding", LocalDate.of(2025, 1, 1), "Beachside Resort");
		when(weddingEventRepository.findById(1L)).thenReturn(Optional.of(weddingEvent));

		WeddingEvent result = weddingEventService.getWeddingEventById(1L);

		assertNotNull(result);
		assertEquals("My Wedding", result.getName());
		assertEquals(LocalDate.of(2025, 1, 1), result.getDate());
		assertEquals("Beachside Resort", result.getLocation());
		verify(weddingEventRepository, times(1)).findById(1L);
	}

	@Test
	void testGetWeddingEventByIdNotFound() {
		when(weddingEventRepository.findById(1L)).thenReturn(Optional.empty());

		WeddingEvent result = weddingEventService.getWeddingEventById(1L);

		assertNull(result);
		verify(weddingEventRepository, times(1)).findById(1L);
	}

	@Test
	void testCreateWeddingEvent() {
		WeddingEvent weddingEvent = new WeddingEvent(null, "Summer Wedding", LocalDate.of(2025, 1, 1),
				"Mountain Resort");
		WeddingEvent savedWeddingEvent = new WeddingEvent(1L, "Summer Wedding", LocalDate.of(2025, 1, 1),
				"Mountain Resort");

		when(weddingEventRepository.save(weddingEvent)).thenReturn(savedWeddingEvent);

		WeddingEvent result = weddingEventService.insertNewWeddingEvent(weddingEvent);

		assertNotNull(result);
		assertEquals("Summer Wedding", result.getName());
		assertEquals(LocalDate.of(2025, 1, 1), result.getDate());
		assertEquals("Mountain Resort", result.getLocation());
		verify(weddingEventRepository, times(1)).save(weddingEvent);
	}

	@Test
	void testUpdateWeddingEvent() {
		WeddingEvent existingWeddingEvent = new WeddingEvent(1L, "My Wedding", LocalDate.of(2025, 1, 1),
				"Beachside Resort");
		WeddingEvent updatedWeddingEvent = new WeddingEvent(1L, "Updated Wedding", LocalDate.of(2025, 2, 1),
				"Beachside Resort");

		when(weddingEventRepository.findById(1L)).thenReturn(Optional.of(existingWeddingEvent));
		when(weddingEventRepository.save(updatedWeddingEvent)).thenReturn(updatedWeddingEvent);

		WeddingEvent result = weddingEventService.updateWeddingEventById(1L, updatedWeddingEvent);

		assertNotNull(result);
		assertEquals("Updated Wedding", result.getName());
		verify(weddingEventRepository, times(1)).save(updatedWeddingEvent);
	}

	@Test
	void testDeleteWeddingEvent() {
		long id = 1L;
		doNothing().when(weddingEventRepository).deleteById(id);

		weddingEventService.deleteWeddingEventById(id);

		verify(weddingEventRepository, times(1)).deleteById(id);
	}

	@Test
	void testGetAllWeddingEvents() {
		WeddingEvent weddingEvent1 = new WeddingEvent(1L, "Wedding 1", LocalDate.of(2025, 1, 1), "Location 1");
		WeddingEvent weddingEvent2 = new WeddingEvent(2L, "Wedding 2", LocalDate.of(2025, 2, 23), "Location 2");
		when(weddingEventRepository.findAll()).thenReturn(List.of(weddingEvent1, weddingEvent2));

		List<WeddingEvent> weddingEvents = weddingEventService.getAllWeddingEvents();

		assertNotNull(weddingEvents);
		assertEquals(2, weddingEvents.size());
		verify(weddingEventRepository, times(1)).findAll();
	}

	@Test
	void testInsertNewWeddingEvent_setsNullIdBeforeSave() {
		WeddingEvent input = new WeddingEvent(99L, "Temp", LocalDate.of(2025, 8, 8), "Temp Place");
		WeddingEvent saved = new WeddingEvent(1L, "Temp", LocalDate.of(2025, 8, 8), "Temp Place");

		when(weddingEventRepository.save(any())).thenReturn(saved);

		WeddingEvent result = weddingEventService.insertNewWeddingEvent(input);

		ArgumentCaptor<WeddingEvent> captor = ArgumentCaptor.forClass(WeddingEvent.class);
		verify(weddingEventRepository).save(captor.capture());
		assertThat(captor.getValue().getId()).isNull(); // 💥 Kills the mutant removing setId(null)
		assertThat(result).isEqualTo(saved);
	}

	@Test
	void testUpdateWeddingEventById_setsCorrectIdBeforeSave() {
		WeddingEvent updated = new WeddingEvent(null, "Revised", LocalDate.of(2025, 9, 9), "Revised Place");
		WeddingEvent saved = new WeddingEvent(7L, "Revised", LocalDate.of(2025, 9, 9), "Revised Place");

		when(weddingEventRepository.save(any())).thenReturn(saved);

		WeddingEvent result = weddingEventService.updateWeddingEventById(7L, updated);

		ArgumentCaptor<WeddingEvent> captor = ArgumentCaptor.forClass(WeddingEvent.class);
		verify(weddingEventRepository).save(captor.capture());
		assertThat(captor.getValue().getId()).isEqualTo(7L); // 💥 Kills the mutant removing setId(id)
		assertThat(result).isEqualTo(saved);
	}

	@Test
	void testGetAllEventsWithGuests_returnsPopulatedList() {
		// given
		WeddingEvent e1 = new WeddingEvent(1L, "Party", LocalDate.of(2025, 4, 4), "Oslo");
		Guest g1 = new Guest(10L, "Alice", "alice@ex.com");
		g1.setEvent(e1);
		Guest g2 = new Guest(11L, "Bob", "bob@ex.com");
		g2.setEvent(e1);
		e1.setGuest(List.of(g1, g2));

		when(weddingEventRepository.findAllWithGuests()).thenReturn(List.of(e1));

		// when
		List<WeddingEvent> result = weddingEventService.getAllEventsWithGuests();

		// then
		assertNotNull(result);
		assertEquals(1, result.size());
		WeddingEvent fetched = result.get(0);
		assertEquals(1L, fetched.getId());
		assertEquals("Party", fetched.getName());
		assertNotNull(fetched.getGuest());
		assertEquals(2, fetched.getGuest().size());
		verify(weddingEventRepository, times(1)).findAllWithGuests();
	}

	@Test
	void testGetAllEventsWithGuests_returnsEmptyList() {
		// given
		when(weddingEventRepository.findAllWithGuests()).thenReturn(Collections.emptyList());

		// when
		List<WeddingEvent> result = weddingEventService.getAllEventsWithGuests();

		// then
		assertNotNull(result);
		assertEquals(0, result.size());
		verify(weddingEventRepository, times(1)).findAllWithGuests();
	}

}
