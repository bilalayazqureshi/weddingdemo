package com.example.demo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.demo.model.WeddingEvent;
import com.example.demo.repositories.WeddingEventRepository;

import io.restassured.response.Response;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class WeddingEventRestControllerIT {

	@SuppressWarnings("resource")
	@Container
	static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0").withDatabaseName("testdb")
			.withUsername("testuser").withPassword("testpass");

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysql::getJdbcUrl);
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
	}

	@Autowired
	private WeddingEventRepository weddingEventRepository;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		io.restassured.RestAssured.port = port;
		weddingEventRepository.deleteAll();
		weddingEventRepository.flush();
	}

	@Test
	void testNewWeddingEvent() {
		WeddingEvent event = new WeddingEvent(null, "Wedding Test", LocalDate.of(2025, 5, 20), "Rome");

		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(event).when()
				.post("/api/weddingevents/new");

		WeddingEvent saved = response.getBody().as(WeddingEvent.class);

		assertThat(saved.getId()).isNotNull();
		assertThat(weddingEventRepository.findById(saved.getId())).contains(saved);
	}

	@Test
	void testGetWeddingEventById() {
		WeddingEvent saved = weddingEventRepository
				.save(new WeddingEvent(null, "Wedding Fetch", LocalDate.of(2025, 6, 15), "Florence"));

		WeddingEvent fetched = given().when().get("/api/weddingevents/" + saved.getId()).then().statusCode(200)
				.extract().as(WeddingEvent.class);

		assertThat(fetched.getId()).isEqualTo(saved.getId());
		assertThat(fetched.getName()).isEqualTo("Wedding Fetch");
	}

	@Test
	void testUpdateWeddingEvent() {
		WeddingEvent saved = weddingEventRepository
				.save(new WeddingEvent(null, "Old Wedding", LocalDate.of(2025, 7, 1), "Milan"));

		WeddingEvent updated = new WeddingEvent(null, "Updated Wedding", LocalDate.of(2025, 7, 1), "Turin");

		WeddingEvent result = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when()
				.put("/api/weddingevents/" + saved.getId()).then().statusCode(200).extract().as(WeddingEvent.class);

		assertThat(result.getName()).isEqualTo("Updated Wedding");
		assertThat(result.getLocation()).isEqualTo("Turin");
	}

	@Test
	void testDeleteWeddingEvent() {
		WeddingEvent saved = weddingEventRepository
				.save(new WeddingEvent(null, "To Delete", LocalDate.of(2025, 8, 10), "Venice"));

		given().when().delete("/api/weddingevents/" + saved.getId()).then().statusCode(200);

		assertThat(weddingEventRepository.findById(saved.getId())).isEmpty();
	}
}
