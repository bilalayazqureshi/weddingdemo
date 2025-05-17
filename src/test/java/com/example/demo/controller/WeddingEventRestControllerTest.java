package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.WeddingEvent;
import com.example.demo.service.WeddingEventService;

@RunWith(SpringRunner.class)
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
				.thenReturn(List.of(new WeddingEvent(1L, "Smith Wedding", "2025-06-20", "New York"),
						new WeddingEvent(2L, "Johnson Wedding", "2025-07-15", "Los Angeles")));

		// Perform GET request and verify response JSON
		this.mvc.perform(get("/api/weddingevents").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("["
						+ "{\"id\":1,\"name\":\"Smith Wedding\",\"date\":\"2025-06-20\",\"location\":\"New York\"},"
						+ "{\"id\":2,\"name\":\"Johnson Wedding\",\"date\":\"2025-07-15\",\"location\":\"Los Angeles\"}"
						+ "]"));
	}
}
