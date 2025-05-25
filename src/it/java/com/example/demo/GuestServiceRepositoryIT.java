package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.demo.model.Guest;
import com.example.demo.repositories.GuestRepository;
import com.example.demo.service.GuestService;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GuestServiceRepositoryIT {

	@SuppressWarnings("resource")
	@Container
	static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0").withDatabaseName("testdb")
			.withUsername("testuser").withPassword("testpass");

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
	}

	@Autowired
	private GuestService guestService;

	@Autowired
	private GuestRepository guestRepository;

	@Test
	void testInsertNewGuest() {
		Guest saved = guestService.insertNewGuest(new Guest(null, "Bob", "bob@123.com"));
		assertThat(saved.getId()).isNotNull();
		assertThat(guestRepository.findById(saved.getId())).isPresent();
	}

	@Test
	void testGetAllGuests() {
		guestRepository.deleteAll();

		guestService.insertNewGuest(new Guest(null, "Charlie", "charlie@example.com"));
		guestService.insertNewGuest(new Guest(null, "Dana", "dana@example.com"));

		List<Guest> all = guestService.getAllGuests();
		assertThat(all).hasSize(2).extracting(Guest::getName).containsExactlyInAnyOrder("Charlie", "Dana");
	}

	@Test
	void testUpdateGuestById() {
		Guest original = guestService.insertNewGuest(new Guest(null, "Eve", "eve@old.com"));

		Guest updated = guestService.updateGuestById(original.getId(), new Guest(null, "Eve", "eve@new.com"));

		assertThat(updated.getEmail()).isEqualTo("eve@new.com");
		Guest fromDb = guestRepository.findById(original.getId()).orElseThrow();
		assertThat(fromDb.getEmail()).isEqualTo("eve@new.com");
	}

	@Test
	void testDeleteGuestById() {
		Guest g = guestService.insertNewGuest(new Guest(null, "Frank", "frank@example.com"));

		guestService.deleteGuestById(g.getId());

		assertThat(guestRepository.findById(g.getId())).isEmpty();
	}

}
