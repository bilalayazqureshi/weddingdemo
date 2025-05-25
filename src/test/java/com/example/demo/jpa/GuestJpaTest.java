package com.example.demo.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Guest;
import org.slf4j.LoggerFactory;
@DataJpaTest
class GuestJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testJpaMapping() {
		Guest saved = entityManager.persistFlushFind(new Guest(null, "test", "test@example.com"));

		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getEmail()).isEqualTo("test@example.com");
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();
		// just to see the generated identifier
		LoggerFactory.getLogger(GuestJpaTest.class).info("Saved: " + saved.toString());
	}

}
