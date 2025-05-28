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
import com.example.demo.pages.GuestFormPage;
import com.example.demo.pages.GuestListPage;
import com.example.demo.repositories.GuestRepository;
import com.example.demo.repositories.WeddingEventRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GuestWebControllerIT {

	@Autowired
	private GuestRepository guestRepository;
	@Autowired
	private WeddingEventRepository weddingEventRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private GuestListPage listPage;

	@BeforeEach
	void setup() {
		driver = new HtmlUnitDriver();
		guestRepository.deleteAll();
		weddingEventRepository.deleteAll();
		listPage = new GuestListPage(driver, port);
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

	@Test
	void test_HomePageWithGuests_ShowsEventNameInTable() {
		WeddingEvent ev = weddingEventRepository
				.save(new WeddingEvent(null, "Anniversary", LocalDate.of(2025, 8, 8), "Paris"));

		Guest g = new Guest();
		g.setName("Alice");
		g.setEmail("alice@example.com");
		g.setEvent(ev);
		guestRepository.save(g);

		listPage.open();
		String txt = listPage.tableText();

		assertThat(txt).contains("Alice", "alice@example.com", "Anniversary", "Edit", "Delete");
	}

	@Test
	void test_CanCreateGuestViaFormAndSeeEvent() {
		weddingEventRepository
				.save(new WeddingEvent(null, "Engagement", LocalDate.of(2025, 9, 9), "London"));

		GuestFormPage form = listPage.open().clickNew();
		form.setName("Bob").setEmail("bob@example.com").selectEvent("Engagement").submit();

		listPage.open();
		String txt = listPage.tableText();
		assertThat(txt).contains("Bob", "bob@example.com", "Engagement");
	}

	@Test
	void test_CanUpdateGuestAndSeeChangesOnList() {
		WeddingEvent ev1 = weddingEventRepository
				.save(new WeddingEvent(null, "Engagement Party", LocalDate.of(2025, 9, 9), "London"));
		weddingEventRepository
				.save(new WeddingEvent(null, "Anniversary Gala", LocalDate.of(2025, 10, 10), "Paris"));

		Guest g = new Guest(null, "Bob", "bob@example.com");
		g.setEvent(ev1);
		guestRepository.save(g);

		GuestFormPage form = listPage.open().clickEdit(g.getId());
		form.selectEvent("Anniversary Gala").submit();

		listPage.open();
		assertThat(listPage.tableText()).contains("Bob", "bob@example.com", "Anniversary Gala");
	}

	@Test
	void test_CanDeleteGuestAndItDisappearsFromList() {
		WeddingEvent ev = weddingEventRepository
				.save(new WeddingEvent(null, "Farewell", LocalDate.of(2025, 12, 12), "Venice"));
		Guest g = new Guest();
		g.setName("Carol");
		g.setEmail("carol@example.com");
		g.setEvent(ev);
		guestRepository.save(g);

		listPage.open().clickDelete(g.getId());
		assertThat(driver.findElement(By.tagName("h1")).getText())
				.contains("Guest with ID " + g.getId() + " was deleted.");

		driver.findElement(By.cssSelector("form[action='/guests'] button")).click();
		listPage.open();
		if (listPage.isEmpty()) {
			assertThat(driver.findElement(By.xpath("//*[text()='No guest']")).isDisplayed()).isTrue();
		} else {
			assertThat(listPage.tableText()).doesNotContain("Carol", "carol@example.com");
		}
	}
}