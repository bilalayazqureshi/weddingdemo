package com.example.demo;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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

import com.example.demo.model.Guest;
import com.example.demo.repositories.GuestRepository;

import io.restassured.response.Response;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GuestRestControllerIT {

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
	private GuestRepository guestRepository;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setup() {
		io.restassured.RestAssured.port = port;
		guestRepository.deleteAll();
		guestRepository.flush();
	}

	@Test
	void testNewGuest() {
		Guest guest = new Guest(null, "Test Guest", "test@example.com");

		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(guest).when()
				.post("/api/guests/new");

		Guest saved = response.getBody().as(Guest.class);

		assertThat(saved.getId()).isNotNull();
		assertThat(guestRepository.findById(saved.getId())).contains(saved);
	}

	@Test
	void testGetGuestById() {
		Guest saved = guestRepository.save(new Guest(null, "John Guest", "john@example.com"));

		Guest fetched = given().when().get("/api/guests/" + saved.getId()).then().statusCode(200).extract()
				.as(Guest.class);

		assertThat(fetched.getId()).isEqualTo(saved.getId());
		assertThat(fetched.getName()).isEqualTo("John Guest");
		assertThat(fetched.getEmail()).isEqualTo("john@example.com");
	}

	@Test
	void testUpdateGuest() {
		Guest saved = guestRepository.save(new Guest(null, "Old Name", "old@example.com"));
		Guest updated = new Guest(null, "New Name", "new@example.com");

		Guest result = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when()
				.put("/api/guests/" + saved.getId()).then().statusCode(200).extract().as(Guest.class);

		assertThat(result.getName()).isEqualTo("New Name");
		assertThat(result.getEmail()).isEqualTo("new@example.com");
	}

	@Test
	void testDeleteGuest() {
		Guest saved = guestRepository.save(new Guest(null, "Delete Me", "del@example.com"));

		given().when().delete("/api/guests/" + saved.getId()).then().statusCode(200);

		assertThat(guestRepository.findById(saved.getId())).isEmpty();
	}

}
