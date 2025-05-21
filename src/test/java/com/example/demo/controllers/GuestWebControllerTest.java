package com.example.demo.controllers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Guest;
import com.example.demo.services.GuestService;

@WebMvcTest(controllers = GuestWebController.class)
class GuestWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private GuestService guestService;

	@Test
	void testStatus200_ListView() throws Exception {
		mvc.perform(get("/guests")).andExpect(status().is2xxSuccessful());
	}

	@Test
	void testReturnGuestView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/guests")).andReturn().getModelAndView(), "guest");
	}

	@Test
	void test_ListView_ShowsGuests() throws Exception {
		List<Guest> guests = asList(new Guest(1L, "Alice", "alice@example.com"));
		when(guestService.getAllGuests()).thenReturn(guests);

		mvc.perform(get("/guests")).andExpect(view().name("guest")).andExpect(model().attribute("guests", guests))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void test_ListView_ShowsMessageWhenNoGuests() throws Exception {
		when(guestService.getAllGuests()).thenReturn(emptyList());

		mvc.perform(get("/guests")).andExpect(view().name("guest")).andExpect(model().attribute("guests", emptyList()))
				.andExpect(model().attribute("message", "No guest"));
	}

	@Test
	void test_EditGuest_WhenGuestIsFound() throws Exception {
		Guest guest = new Guest(1L, "Bob", "bob@example.com");
		when(guestService.getGuestById(1L)).thenReturn(guest);

		mvc.perform(get("/guests/edit/1")).andExpect(view().name("edit_guest")).andExpect(model().attribute("guest", guest))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void test_EditGuest_WhenGuestIsNotFound() throws Exception {
		when(guestService.getGuestById(1L)).thenReturn(null);

		mvc.perform(get("/guests/edit/1")).andExpect(view().name("edit_guest"))
				.andExpect(model().attribute("guest", nullValue()))
				.andExpect(model().attribute("message", "No guest found with id: 1"));
	}

	@Test
	void test_EditNewGuest() throws Exception {
		mvc.perform(get("/guests/new")).andExpect(view().name("guest"))
				.andExpect(model().attribute("guest", new Guest())).andExpect(model().attribute("message", ""));
		verifyNoMoreInteractions(guestService);
	}

	@Test
	void test_PostGuestWithoutId_ShouldInsertNewGuest() throws Exception {
		mvc.perform(post("/guests/save").param("name", "Charlie").param("email", "charlie@example.com"))
				.andExpect(view().name("redirect:/guests"));

		verify(guestService).insertNewGuest(new Guest(null, "Charlie", "charlie@example.com"));
	}

	@Test
	void test_PostGuestWithId_ShouldUpdateExistingGuest() throws Exception {
		mvc.perform(
				post("/guests/save").param("id", "2").param("name", "Charlie").param("email", "charlie@example.com"))
				.andExpect(view().name("redirect:/guests"));

		verify(guestService).updateGuestById(2L, new Guest(2L, "Charlie", "charlie@example.com"));
	}

	@Test
	void test_DeleteGuest() throws Exception {
		mvc.perform(delete("/guests/delete/3")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/guests"));

		verify(guestService).deleteGuestById(3L);
	}

}
