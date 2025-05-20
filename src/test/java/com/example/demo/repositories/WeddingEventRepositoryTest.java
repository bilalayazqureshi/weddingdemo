package com.example.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.WeddingEvent;

@DataJpaTest
class WeddingEventRepositoryTest {

	@Autowired
	private WeddingEventRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void firstLearningTest() {
		WeddingEvent event = new WeddingEvent(null, "test", LocalDate.of(2025, 1, 1), "testLocation");
		WeddingEvent saved = repository.save(event);
		Collection<WeddingEvent> events = repository.findAll();
		assertThat(events).containsExactly(saved);
	}

	@Test
	public void secondLearningTest() {
		WeddingEvent event = new WeddingEvent(null, "test", LocalDate.of(2025, 1, 1), "testLocation");
		WeddingEvent saved = entityManager.persistFlushFind(event);
		Collection<WeddingEvent> events = repository.findAll();
		assertThat(events).containsExactly(saved);
	}

	@Test
	public void test_findByTitle() {
		WeddingEvent saved = entityManager
				.persistFlushFind(new WeddingEvent(null, "test", LocalDate.of(2025, 1, 1), "testLocation"));
		WeddingEvent found = repository.findByName("test");
		assertThat(found).isEqualTo(saved);
	}

	@Test
	public void test_findByTitleAndLocation() {
		entityManager.persistFlushFind(new WeddingEvent(null, "test", LocalDate.of(2025, 1, 1), "loc1"));
		WeddingEvent evt = entityManager
				.persistFlushFind(new WeddingEvent(null, "test", LocalDate.of(2025, 1, 1), "loc2"));
		List<WeddingEvent> found = repository.findByNameAndLocation("test", "loc2");
		assertThat(found).containsExactly(evt);
	}

	@Test
	public void test_findByTitleOrLocation() {
		WeddingEvent e1 = entityManager
				.persistFlushFind(new WeddingEvent(null, "test", LocalDate.of(2025, 1, 1), "loc1"));
		WeddingEvent e2 = entityManager
				.persistFlushFind(new WeddingEvent(null, "another", LocalDate.of(2025, 1, 1), "loc2"));
		entityManager.persistFlushFind(new WeddingEvent(null, "noMatch", LocalDate.of(2025, 1, 1), "loc3"));
		List<WeddingEvent> found = repository.findByNameOrLocation("test", "loc2");
		assertThat(found).containsExactly(e1, e2);
	}

	@Test
	public void test_findAllByDateBefore() {
		WeddingEvent e1 = entityManager
				.persistFlushFind(new WeddingEvent(null, "e1", LocalDate.of(2025, 1, 1), "loc1"));
		WeddingEvent e2 = entityManager
				.persistFlushFind(new WeddingEvent(null, "e2", LocalDate.of(2025, 1, 2), "loc2"));
		entityManager.persistFlushFind(new WeddingEvent(null, "e3", LocalDate.of(2025, 1, 3), "loc3"));
		List<WeddingEvent> found = repository.findAllByDateBefore(LocalDate.of(2025, 1, 3));
		assertThat(found).containsExactly(e1, e2);
	}

	@Test
	public void testCreateEvent() {
		WeddingEvent event = new WeddingEvent(null, "alice", LocalDate.of(2025, 5, 20), "Rome");
		WeddingEvent saved = repository.save(event);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getName()).isEqualTo("alice");
		assertThat(saved.getLocation()).isEqualTo("Rome");
	}

	@Test
	public void testReadEventById() {
		WeddingEvent event = entityManager
				.persistFlushFind(new WeddingEvent(null, "bob", LocalDate.of(2025, 6, 15), "Venice"));

		Optional<WeddingEvent> foundOpt = repository.findById(event.getId());
		assertThat(foundOpt).isPresent();

		WeddingEvent found = foundOpt.get();
		assertThat(found.getName()).isEqualTo("bob");
		assertThat(found.getLocation()).isEqualTo("Venice");
	}

	@Test
	public void testUpdateEvent() {
		WeddingEvent event = entityManager
				.persistFlushFind(new WeddingEvent(null, "carol", LocalDate.of(2025, 7, 10), "Milan"));

		event.setName("caroline");
		event.setLocation("Turin");
		WeddingEvent updated = repository.save(event);
		entityManager.flush();
		entityManager.clear();

		WeddingEvent reloaded = repository.findById(updated.getId()).orElseThrow();
		assertThat(reloaded.getName()).isEqualTo("caroline");
		assertThat(reloaded.getLocation()).isEqualTo("Turin");
	}

	@Test
	public void testDeleteEvent() {
		WeddingEvent event = entityManager
				.persistFlushFind(new WeddingEvent(null, "dave", LocalDate.of(2025, 8, 5), "Florence"));

		repository.deleteById(event.getId());
		entityManager.flush();

		boolean exists = repository.existsById(event.getId());
		assertThat(exists).isFalse();
	}

}
