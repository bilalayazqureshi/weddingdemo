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

import java.util.List;

import org.junit.jupiter.api.Test; // Use JUnit 5's Test annotation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Guest;
import com.example.demo.service.GuestService;

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

	@Test
	public void testOneGuestByIdWithExistingGuest() throws Exception {
		when(guestService.getGuestById(anyLong())).thenReturn(new Guest(1L, "John Doe", "john.doe@example.com"));

		this.mvc.perform(get("/api/guests/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("John Doe")))
				.andExpect(jsonPath("$.email", is("john.doe@example.com")));
	}

	@Test
	public void testOneGuestByIdWithNotFoundGuest() throws Exception {
		when(guestService.getGuestById(anyLong())).thenReturn(null);

		this.mvc.perform(get("/api/guests/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("")); // no body when not found
	}

	@Test
	public void testCreateGuest() throws Exception {
		when(guestService.insertNewGuest(any(Guest.class)))
				.thenReturn(new Guest(3L, "Bob Brown", "bob.brown@example.com"));

		String newGuestJson = """
				{
				  "name":"Bob Brown",
				  "email":"bob.brown@example.com"
				}
				""";

		this.mvc.perform(post("/api/guests/new").contentType(MediaType.APPLICATION_JSON).content(newGuestJson))

				.andExpect(jsonPath("$.id", is(3))).andExpect(jsonPath("$.name", is("Bob Brown")))
				.andExpect(jsonPath("$.email", is("bob.brown@example.com")));
	}

	@Test
	public void testUpdateGuestExisting() throws Exception {
		when(guestService.updateGuestById(anyLong(), any(Guest.class)))
				.thenReturn(new Guest(1L, "John Doe Jr.", "john.jr@example.com"));

		String updateJson = "{\"name\":\"John Doe Jr.\",\"email\":\"john.jr@example.com\"}";

		this.mvc.perform(put("/api/guests/1").contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("John Doe Jr.")))
				.andExpect(jsonPath("$.email", is("john.jr@example.com")));
	}

	@Test
	public void testUpdateGuestNotFound() throws Exception {
		when(guestService.updateGuestById(anyLong(), any(Guest.class))).thenReturn(null);

		String updateJson = "{\"name\":\"Nobody\",\"email\":\"none@example.com\"}";

		this.mvc.perform(put("/api/guests/99").contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

	@Test
	public void testDeleteGuest() throws Exception {
		doNothing().when(guestService).deleteGuestById(anyLong());

		this.mvc.perform(delete("/api/guests/1")).andExpect(status().isOk()).andExpect(content().string(""));

		verify(guestService).deleteGuestById(1L);
	}

}
