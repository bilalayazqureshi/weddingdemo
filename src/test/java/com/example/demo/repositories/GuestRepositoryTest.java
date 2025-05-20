package com.example.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Guest;

@DataJpaTest
class GuestRepositoryTest {
	@Autowired
	private GuestRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void firstLearningTest() {
		Guest guest = new Guest(null, "test", "test@example.com");
		Guest saved = repository.save(guest);
		Collection<Guest> guests = repository.findAll();
		assertThat(guests).containsExactly(saved);
	}

	@Test
	public void secondLearningTest() {
		Guest guest = new Guest(null, "test", "test@example.com");
		Guest saved = entityManager.persistFlushFind(guest);
		Collection<Guest> guests = repository.findAll();
		assertThat(guests).containsExactly(saved);
	}

	@Test
	public void test_findByName() {
		Guest saved = entityManager.persistFlushFind(new Guest(null, "test", "test@example.com"));
		Guest found = repository.findByName("test");
		assertThat(found).isEqualTo(saved);
	}

	@Test
	public void test_findByNameAndEmail() {
		entityManager.persistFlushFind(new Guest(null, "test", "test@example.com"));
		Guest g = entityManager.persistFlushFind(new Guest(null, "test", "other@example.com"));
		List<Guest> found = repository.findByNameAndEmail("test", "other@example.com");
		assertThat(found).containsExactly(g);
	}

	@Test
	public void test_findByNameOrEmail() {
		Guest g1 = entityManager.persistFlushFind(new Guest(null, "test", "a@example.com"));
		Guest g2 = entityManager.persistFlushFind(new Guest(null, "another", "b@example.com"));
		entityManager.persistFlushFind(new Guest(null, "noMatch", "c@other.com"));
		List<Guest> found = repository.findByNameOrEmail("test", "b@example.com");
		assertThat(found).containsExactly(g1, g2);
	}

	@Test
	public void test_findByEmailEndingWith() {
		Guest g1 = entityManager.persistFlushFind(new Guest(null, "test", "a@example.com"));
		Guest g2 = entityManager.persistFlushFind(new Guest(null, "another", "b@example.com"));
		entityManager.persistFlushFind(new Guest(null, "no", "c@other.com"));
		List<Guest> found = repository.findByEmailEndingWith("@example.com");
		assertThat(found).containsExactly(g1, g2);
	}

	@Test
	void testCreateGuest() {
		Guest guest = new Guest(null, "alice", "alice@example.com");
		Guest saved = repository.save(guest);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getName()).isEqualTo("alice");
		assertThat(saved.getEmail()).isEqualTo("alice@example.com");
	}

	@Test
	void testReadGuestById() {
		Guest guest = entityManager.persistFlushFind(new Guest(null, "bob", "bob@example.com"));

		Optional<Guest> foundOpt = repository.findById(guest.getId());
		assertThat(foundOpt).isPresent();

		Guest found = foundOpt.get();
		assertThat(found.getName()).isEqualTo("bob");
		assertThat(found.getEmail()).isEqualTo("bob@example.com");
	}

	@Test
	void testUpdateGuest() {
		Guest guest = entityManager.persistFlushFind(new Guest(null, "carol", "carol@example.com"));

		guest.setName("caroline");
		guest.setEmail("caroline@example.com");
		Guest updated = repository.save(guest);
		entityManager.flush();
		entityManager.clear();

		Guest reloaded = repository.findById(updated.getId()).orElseThrow();
		assertThat(reloaded.getName()).isEqualTo("caroline");
		assertThat(reloaded.getEmail()).isEqualTo("caroline@example.com");
	}

	@Test
	void testDeleteGuest() {
		Guest guest = entityManager.persistFlushFind(new Guest(null, "dave", "dave@example.com"));

		repository.deleteById(guest.getId());
		entityManager.flush();

		boolean exists = repository.existsById(guest.getId());
		assertThat(exists).isFalse();
	}
}
