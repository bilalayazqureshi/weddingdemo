package com.example.demo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Guest;

@DataJpaTest
class GuestJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	private static final Logger log = LoggerFactory.getLogger(GuestJpaTest.class);

	@Test
	void testJpaMapping() {
		Guest saved = entityManager.persistFlushFind(new Guest(null, "test", "test@example.com"));

		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getEmail()).isEqualTo("test@example.com");
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();
		// just to see the generated identifier
		log.info("Saved: {}", saved);
	}

}
