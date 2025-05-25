package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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

import com.example.demo.model.WeddingEvent;
import com.example.demo.repositories.WeddingEventRepository;
import com.example.demo.service.WeddingEventService;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class WeddingEventServiceRepositoryIT {

	@SuppressWarnings("resource")
	@Container
	static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("testdb")
			.withUsername("testuser")
			.withPassword("testpass");

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
	}

	@Autowired
	private WeddingEventService weddingEventService;

	@Autowired
	private WeddingEventRepository weddingEventRepository;

	@Test
	void testInsertNewWeddingEvent() {
		WeddingEvent saved = weddingEventService.insertNewWeddingEvent(
				new WeddingEvent(null, "Simple Wedding", LocalDate.of(2025, 6, 1), "Florence"));
		assertThat(saved.getId()).isNotNull();
		assertThat(weddingEventRepository.findById(saved.getId())).isPresent();
	}

	@Test
	void testGetAllWeddingEvents() {
		weddingEventRepository.deleteAll();

		weddingEventService.insertNewWeddingEvent(
				new WeddingEvent(null, "Spring Wedding", LocalDate.of(2025, 4, 15), "Rome"));
		weddingEventService.insertNewWeddingEvent(
				new WeddingEvent(null, "Autumn Wedding", LocalDate.of(2025, 10, 5), "Milan"));

		List<WeddingEvent> all = weddingEventService.getAllWeddingEvents();
		assertThat(all).hasSize(2).extracting(WeddingEvent::getName).containsExactlyInAnyOrder("Spring Wedding", "Autumn Wedding");
	}

	@Test
	void testUpdateWeddingEventById() {
		WeddingEvent original = weddingEventService.insertNewWeddingEvent(
				new WeddingEvent(null, "Old Wedding", LocalDate.of(2025, 5, 10), "Naples"));

		WeddingEvent updated = weddingEventService.updateWeddingEventById(original.getId(),
				new WeddingEvent(null, "Updated Wedding", LocalDate.of(2025, 5, 10), "Naples"));

		assertThat(updated.getName()).isEqualTo("Updated Wedding");
		WeddingEvent fromDb = weddingEventRepository.findById(original.getId()).orElseThrow();
		assertThat(fromDb.getName()).isEqualTo("Updated Wedding");
	}

	@Test
	void testDeleteWeddingEventById() {
		WeddingEvent event = weddingEventService.insertNewWeddingEvent(
				new WeddingEvent(null, "To Delete", LocalDate.of(2025, 7, 7), "Verona"));

		weddingEventService.deleteWeddingEventById(event.getId());

		assertThat(weddingEventRepository.findById(event.getId())).isEmpty();
	}
}
