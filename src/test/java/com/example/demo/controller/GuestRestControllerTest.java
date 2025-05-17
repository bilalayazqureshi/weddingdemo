package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test; // Use JUnit 5's Test annotation
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Guest;
import com.example.demo.service.GuestService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GuestRestController.class)
class GuestRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private GuestService guestService; // Mocking the service

	@Test
	public void testAllGuestsNotEmpty() throws Exception {
		// Mocking the service response
		when(guestService.getAllGuests()).thenReturn(List.of(new Guest(1L, "John Doe", "john.doe@example.com"),
				new Guest(2L, "Alice Smith", "alice.smith@example.com")));

		// Perform GET request and verify the results
		this.mvc.perform(get("/api/guests").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("[" + "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},"
						+ "{\"id\":2,\"name\":\"Alice Smith\",\"email\":\"alice.smith@example.com\"}" + "]"));
	}
}
