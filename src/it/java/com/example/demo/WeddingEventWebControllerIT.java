package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.demo.model.Guest;
import com.example.demo.model.WeddingEvent;
import com.example.demo.pages.EventFormPage;
import com.example.demo.pages.EventListPage;
import com.example.demo.repositories.GuestRepository;
import com.example.demo.repositories.WeddingEventRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WeddingEventWebControllerIT {

	@Autowired
	private GuestRepository guestRepository;
	@Autowired
	private WeddingEventRepository weddingEventRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private EventListPage listPage;

	@BeforeEach
	void setup() {
		driver = new HtmlUnitDriver();
		guestRepository.deleteAll();
		weddingEventRepository.deleteAll();
		listPage = new EventListPage(driver, port);
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

	@Test
	void test_HomePageEmpty_ShowsNoEvent() {
		// no events in DB
		listPage.open();
		assertThat(listPage.isEmpty()).isTrue();
	}

	@Test
	void test_HomePageWithEvents_ShowsThemInTable() {
		weddingEventRepository.save(new WeddingEvent(null, "E1", LocalDate.of(2025, 10, 10), "Rome"));
		weddingEventRepository.save(new WeddingEvent(null, "E2", LocalDate.of(2025, 11, 11), "Venice"));

		listPage.open();
		String txt = listPage.tableText();
		assertThat(txt).contains("1", "E1", "2025-10-10", "Rome", "0 guest", "Edit", "Delete", "2", "E2", "2025-11-11",
				"Venice", "0 guest", "Edit", "Delete");

	}

	@Test
	void test_CanCreateEventAndSeeIt() {
		EventFormPage form = listPage.open().clickNew();
		form.setName("MyEvent").setDate("2025-12-12").setLocation("Florence").submit();

		listPage.open();
		assertThat(listPage.tableText()).contains("MyEvent", "2025-12-12", "Florence");
	}

	@Test
	void test_CanUpdateEventAndSeeChanges() {
		WeddingEvent e = weddingEventRepository
				.save(new WeddingEvent(null, "Original", LocalDate.of(2025, 1, 1), "Berlin"));

		EventFormPage form = listPage.open().clickEdit(e.getId());
		form.setName("Updated").setDate("2025-02-02").setLocation("Munich").submit();

		listPage.open();
		assertThat(listPage.tableText()).contains("Updated", "2025-02-02", "Munich");
	}

	@Test
	void test_CanDeleteEventAndItDisappears() {
		WeddingEvent e = weddingEventRepository
				.save(new WeddingEvent(null, "ToDelete", LocalDate.of(2025, 3, 3), "Madrid"));

		listPage.open().clickDelete(e.getId());
		String h1 = driver.findElement(By.tagName("h1")).getText();
		assertThat(h1).contains("Event with ID " + e.getId() + " was deleted.");

		driver.findElement(By.cssSelector("form[action='/']")).click();
		listPage.open();
		if (listPage.isEmpty()) {
			assertThat(driver.findElement(By.xpath("//*[text()='No event']")).isDisplayed()).isTrue();
		} else {
			assertThat(listPage.tableText()).doesNotContain("ToDelete");
		}
	}

	@Test
	void test_ViewAttendingGuestsUnderEventForm() {
		WeddingEvent e = weddingEventRepository.save(new WeddingEvent(null, "Party", LocalDate.of(2025, 4, 4), "Oslo"));

		Guest g1 = new Guest();
		g1.setName("G1");
		g1.setEmail("g1@example.com");
		g1.setEvent(e);
		guestRepository.save(g1);

		Guest g2 = new Guest();
		g2.setName("G2");
		g2.setEmail("g2@example.com");
		g2.setEvent(e);
		guestRepository.save(g2);

		EventFormPage form = listPage.open().clickEdit(e.getId());
		String guestsText = form.attendingGuestsText();

		assertThat(guestsText).contains("G1", "g1@example.com").contains("G2", "g2@example.com");
	}

}
