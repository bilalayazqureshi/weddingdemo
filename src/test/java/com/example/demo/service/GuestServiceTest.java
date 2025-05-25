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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Guest;
import com.example.demo.repositories.GuestRepository;

class GuestServiceTest {
	@Mock
	private GuestRepository guestRepository;

	private GuestService guestService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		guestService = new GuestService(guestRepository);
	}

	@Test
	public void testGetGuestById() {
		Guest guest = new Guest(1L, "John Doe", "johndoe@example.com");
		when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));

		Guest result = guestService.getGuestById(1L);

		assertNotNull(result);
		assertEquals("John Doe", result.getName());
		assertEquals("johndoe@example.com", result.getEmail());
		verify(guestRepository, times(1)).findById(1L);
	}

	@Test
	public void testGetGuestByIdNotFound() {
		when(guestRepository.findById(1L)).thenReturn(Optional.empty());

		Guest result = guestService.getGuestById(1L);

		assertNull(result);
		verify(guestRepository, times(1)).findById(1L);
	}

	@Test
	public void testInsertNewGuest() {
		Guest guest = new Guest(null, "Jane Smith", "janesmith@example.com");
		Guest savedGuest = new Guest(1L, "Jane Smith", "janesmith@example.com");

		when(guestRepository.save(guest)).thenReturn(savedGuest);

		Guest result = guestService.insertNewGuest(guest);

		assertNotNull(result);
		assertEquals("Jane Smith", result.getName());
		assertEquals("janesmith@example.com", result.getEmail());
		verify(guestRepository, times(1)).save(guest);
	}

	@Test
	public void testUpdateGuest() {
		Guest existingGuest = new Guest(1L, "John Doe", "johndoe@example.com");
		Guest updatedGuest = new Guest(1L, "John Doe Updated", "johndoeupdated@example.com");

		when(guestRepository.findById(1L)).thenReturn(Optional.of(existingGuest));
		when(guestRepository.save(updatedGuest)).thenReturn(updatedGuest);

		Guest result = guestService.updateGuestById(1L, updatedGuest);

		assertNotNull(result);
		assertEquals("John Doe Updated", result.getName());
		assertEquals("johndoeupdated@example.com", result.getEmail());
		verify(guestRepository, times(1)).save(updatedGuest);
	}

	@Test
	public void testDeleteGuest() {
		long id = 1L;
		doNothing().when(guestRepository).deleteById(id);

		guestService.deleteGuestById(id);

		verify(guestRepository, times(1)).deleteById(id);
	}

	@Test
	public void testGetAllGuests() {
		Guest guest1 = new Guest(1L, "Wedding Guest 1", "guest1@example.com");
		Guest guest2 = new Guest(2L, "Wedding Guest 2", "guest2@example.com");
		when(guestRepository.findAll()).thenReturn(List.of(guest1, guest2));

		List<Guest> guests = guestService.getAllGuests();

		assertNotNull(guests);
		assertEquals(2, guests.size());
		verify(guestRepository, times(1)).findAll();
	}

	@Test
	void insertNewGuest_mustNullOutId_beforeCallingSave() {
		// given a guest that already has an ID
		Guest input = new Guest(99L, "Jane Smith", "jane@example.com");
		Guest saved = new Guest(1L, "Jane Smith", "jane@example.com");
		when(guestRepository.save(any())).thenReturn(saved);

		// when
		Guest result = guestService.insertNewGuest(input);

		// then: the repository must have been called with id == null
		ArgumentCaptor<Guest> captor = ArgumentCaptor.forClass(Guest.class);
		verify(guestRepository).save(captor.capture());
		assertThat(captor.getValue().getId()).isNull(); // kills the setId(null) mutant
		assertThat(result).isSameAs(saved);
	}

	@Test
	void updateGuestById_mustSetCorrectId_beforeCallingSave() {
		// given an input guest without ID
		Guest input = new Guest(null, "Bob", "bob@example.com");
		Guest saved = new Guest(5L, "Bob", "bob@example.com");
		when(guestRepository.save(any())).thenReturn(saved);

		// when
		Guest result = guestService.updateGuestById(5L, input);

		// then: repository.save must receive a Guest with id == 5L
		ArgumentCaptor<Guest> captor = ArgumentCaptor.forClass(Guest.class);
		verify(guestRepository).save(captor.capture());
		assertThat(captor.getValue().getId()).isEqualTo(5L); // kills the setId(id) mutant
		assertThat(result).isSameAs(saved);
	}
}
