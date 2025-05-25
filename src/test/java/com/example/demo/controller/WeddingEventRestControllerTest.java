package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.WeddingEvent;
import com.example.demo.service.WeddingEventService;

@WebMvcTest(controllers = WeddingEventRestController.class)
class WeddingEventRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private WeddingEventService weddingEventService;

	@Test
	public void testAllWeddingEventsNotEmpty() throws Exception {
		// Mock service to return list of WeddingEvent
		when(weddingEventService.getAllWeddingEvents())
				.thenReturn(List.of(new WeddingEvent(1L, "Smith Wedding", LocalDate.of(2025, 1, 1), "New York"),
						new WeddingEvent(2L, "Johnson Wedding", LocalDate.of(2025, 1, 1), "Los Angeles")));

		// Perform GET request and verify response JSON
		this.mvc.perform(get("/api/weddingevents").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("["
						+ "{\"id\":1,\"name\":\"Smith Wedding\",\"date\":\"2025-01-01\",\"location\":\"New York\"},"
						+ "{\"id\":2,\"name\":\"Johnson Wedding\",\"date\":\"2025-01-01\",\"location\":\"Los Angeles\"}"
						+ "]"));
	}

	@Test
	public void testOneWeddingEventByIdWithExistingEvent() throws Exception {
		when(weddingEventService.getWeddingEventById(anyLong()))
				.thenReturn(new WeddingEvent(1L, "Smith Wedding", LocalDate.of(2025, 1, 1), "New York"));

		this.mvc.perform(get("/api/weddingevents/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Smith Wedding")))
				.andExpect(jsonPath("$.date", is("2025-01-01"))).andExpect(jsonPath("$.location", is("New York")));
	}

	@Test
	public void testOneWeddingEventByIdWithNotFoundEvent() throws Exception {
		when(weddingEventService.getWeddingEventById(anyLong())).thenReturn(null);

		this.mvc.perform(get("/api/weddingevents/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	public void testCreateWeddingEvent() throws Exception {
		when(weddingEventService.insertNewWeddingEvent(any(WeddingEvent.class)))
				.thenReturn(new WeddingEvent(3L, "Brown Wedding", LocalDate.of(2025, 8, 10), "Chicago"));

		String newEventJson = """
				{
				  "name":"Brown Wedding",
				  "date":"2025-08-10",
				  "location":"Chicago"
				}
				""";

		this.mvc.perform(post("/api/weddingevents/new").contentType(MediaType.APPLICATION_JSON).content(newEventJson))
				.andExpect(jsonPath("$.id", is(3))).andExpect(jsonPath("$.name", is("Brown Wedding")))
				.andExpect(jsonPath("$.date", is("2025-08-10"))).andExpect(jsonPath("$.location", is("Chicago")));
	}

	@Test
	public void testUpdateWeddingEventExisting() throws Exception {
		when(weddingEventService.updateWeddingEventById(anyLong(), any(WeddingEvent.class)))
				.thenReturn(new WeddingEvent(1L, "Smith Wedding VIP", LocalDate.of(2025, 06, 20), "New York"));

		String updateJson = """
				{
				  "name":"Smith Wedding VIP",
				  "date":"2025-06-20",
				  "location":"New York"
				}
				""";

		this.mvc.perform(put("/api/weddingevents/1").contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Smith Wedding VIP"))).andExpect(jsonPath("$.date", is("2025-06-20")))
				.andExpect(jsonPath("$.location", is("New York")));
	}

	@Test
	public void testUpdateWeddingEventNotFound() throws Exception {
		when(weddingEventService.updateWeddingEventById(anyLong(), any(WeddingEvent.class))).thenReturn(null);

		String updateJson = """
				{
				  "name":"Nonexistent",
				  "date":"2025-12-31",
				  "location":"Nowhere"
				}
				""";

		this.mvc.perform(put("/api/weddingevents/99").contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

	@Test
	public void testDeleteWeddingEvent() throws Exception {
		doNothing().when(weddingEventService).deleteWeddingEventById(anyLong());

		this.mvc.perform(delete("/api/weddingevents/1")).andExpect(status().isOk()).andExpect(content().string(""));

		verify(weddingEventService).deleteWeddingEventById(1L);
	}
}
