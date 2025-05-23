package com.example.demo.controllers;

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
import static java.util.Collections.emptyList;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.services.WeddingEventService;

import com.example.demo.model.WeddingEvent;

@WebMvcTest(controllers = WeddingEventWebController.class)
class WeddingEventWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private WeddingEventService weddingEventService;

	@Test
	void testStatus200_ListView() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	void testReturnEventView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).andReturn().getModelAndView(), "index");
	}

	@Test
	void test_ListView_ShowsEvents() throws Exception {
		List<WeddingEvent> events = asList(new WeddingEvent(1L, "E1", LocalDate.of(2025, 10, 10), "Rome"));
		when(weddingEventService.getAllEvents()).thenReturn(events);

		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("events", events))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void test_ListView_ShowsMessageWhenNoEvents() throws Exception {
		when(weddingEventService.getAllEvents()).thenReturn(emptyList());

		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("events", emptyList()))
				.andExpect(model().attribute("message", "No event"));
	}

	@Test
	void test_EditEvent_WhenFound() throws Exception {
		WeddingEvent evt = new WeddingEvent(2L, "E2", LocalDate.of(2025, 11, 5), "Venice");
		when(weddingEventService.getEventById(2L)).thenReturn(evt);

		mvc.perform(get("/edit/2")).andExpect(view().name("edit_event")).andExpect(model().attribute("event", evt))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void test_EditEvent_WhenNotFound() throws Exception {
		when(weddingEventService.getEventById(3L)).thenReturn(null);

		mvc.perform(get("/edit/3")).andExpect(view().name("edit_event"))
				.andExpect(model().attribute("event", nullValue()))
				.andExpect(model().attribute("message", "No event found with id: 3"));
	}

	@Test
	void test_EditNewEvent() throws Exception {
		mvc.perform(get("/new")).andExpect(view().name("edit_event"))
				.andExpect(model().attribute("event", new WeddingEvent())).andExpect(model().attribute("message", ""));
		verifyNoMoreInteractions(weddingEventService);
	}

	@Test
	void test_PostEventWithoutId_ShouldInsertNewEvent() throws Exception {
		mvc.perform(post("/save").param("name", "E3").param("date", "2025-12-12").param("location", "Milan"))
				.andExpect(view().name("redirect:/"));

		verify(weddingEventService).insertNewEvent(new WeddingEvent(null, "E3", LocalDate.of(2025, 12, 12), "Milan"));
	}

	@Test
	void test_PostEventWithId_ShouldUpdateExistingEvent() throws Exception {
		mvc.perform(post("/save").param("id", "4").param("name", "E4").param("date", "2025-12-25").param("location",
				"Florence")).andExpect(view().name("redirect:/"));

		verify(weddingEventService).updateEventById(4L,
				new WeddingEvent(4L, "E4", LocalDate.of(2025, 12, 25), "Florence"));
	}

	@Test
	void test_DeleteEvent() throws Exception {
		mvc.perform(get("/delete/5")).andExpect(status().isOk()).andExpect(view().name("delete_event"))
				.andExpect(model().attribute("deletedId", 5L));

		verify(weddingEventService).deleteEventById(5L);
	}
}
