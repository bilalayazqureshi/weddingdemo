package com.example.demo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.WeddingEvent;

@DataJpaTest
class WeddingEventJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	private static final Logger log = LoggerFactory.getLogger(WeddingEventJpaTest.class);

	@Test
	void testJpaMapping() {
		WeddingEvent saved = entityManager
				.persistFlushFind(new WeddingEvent(null, "test", LocalDate.of(2025, 1, 1), "testLocation"));

		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getDate()).isEqualTo(LocalDate.of(2025, 1, 1));
		assertThat(saved.getLocation()).isEqualTo("testLocation");
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();
		// just to see the generated identifier
		log.info("Saved: {}", saved);
	}

}
