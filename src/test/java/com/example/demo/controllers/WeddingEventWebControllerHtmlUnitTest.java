package com.example.demo.controllers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.model.WeddingEvent;
import com.example.demo.service.GuestService;
import com.example.demo.service.WeddingEventService;

@WebMvcTest(controllers = WeddingEventWebController.class)
class WeddingEventWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@SuppressWarnings("removal")
	@MockBean
	private WeddingEventService weddingEventService;

	@SuppressWarnings("removal")
	@MockBean
	private GuestService guestService;

	@Test
	void test_HomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Wedding Events");
	}

	@Test
	void testHomePageWithNoEvents() throws Exception {
		when(weddingEventService.getAllWeddingEvents()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).contains("No event");
	}

	@Test
	void test_HomePage_ShouldProvideALinkForCreatingANewEvent() throws Exception {
		HtmlPage page = webClient.getPage("/");
		HtmlAnchor newLink = page.getAnchorByText("New event");
		assertThat(newLink.getHrefAttribute()).isEqualTo("/new");
	}

	@Test
	void test_HomePageWithEvents_ShouldShowThemInATable() throws Exception {
		// given
		WeddingEvent e1 = new WeddingEvent(1L, "E1", LocalDate.of(2025, 10, 10), "Rome");
		WeddingEvent e2 = new WeddingEvent(2L, "E2", LocalDate.of(2025, 11, 11), "Venice");
		when(weddingEventService.getAllWeddingEvents()).thenReturn(asList(e1, e2));

		// when
		HtmlPage page = webClient.getPage("/");

		// then
		assertThat(page.getBody().getTextContent()).doesNotContain("No event");

		HtmlTable table = page.getHtmlElementById("event_table");
		String normalized = removeWindowsCR(table.asNormalizedText());

		assertThat(normalized).isEqualTo("Events\n" + "ID\tName\tDate\tLocation\tGuests\tEdit\tDelete\n"
				+ "1\tE1\t2025-10-10\tRome\t0 guest\tEdit\tDelete\n"
				+ "2\tE2\t2025-11-11\tVenice\t0 guest\tEdit\tDelete");

		page.getAnchorByHref("/edit/1");
		page.getAnchorByHref("/edit/2");
	}

	@Test
	void testEditNonExistentEvent() throws Exception {
		when(weddingEventService.getWeddingEventById(1L)).thenReturn(null);
		HtmlPage page = webClient.getPage("/edit/1");
		assertThat(page.getBody().getTextContent()).contains("No event found with id: 1");
	}

	@Test
	void testEditExistentEvent() throws Exception {
		WeddingEvent original = new WeddingEvent(1L, "orig", LocalDate.of(2025, 12, 12), "Milan");
		when(weddingEventService.getWeddingEventById(1L)).thenReturn(original);

		HtmlPage page = webClient.getPage("/edit/1");
		HtmlForm form = page.getFormByName("event_record");

		form.getInputByValue("orig").setValueAttribute("mod");
		form.getInputByValue("Milan").setValueAttribute("Turin");
		form.getButtonByName("btn_submit").click();

		verify(weddingEventService).updateWeddingEventById(1L,
				new WeddingEvent(1L, "mod", LocalDate.of(2025, 12, 12), "Turin"));
	}

	@Test
	void testEditNewEvent() throws Exception {
		HtmlPage page = webClient.getPage("/new");
		final HtmlForm form = page.getFormByName("event_record");

		form.getInputByName("name").setValueAttribute("NewName");
		form.getInputByName("date").setValueAttribute("2025-12-24");
		form.getInputByName("location").setValueAttribute("Florence");
		form.getButtonByName("btn_submit").click();

		verify(weddingEventService)
				.insertNewWeddingEvent(new WeddingEvent(null, "NewName", LocalDate.of(2025, 12, 24), "Florence"));
	}

	@Test
	void testDeleteEvent_ShouldDisplayConfirmationMessage() throws Exception {
		doNothing().when(weddingEventService).deleteWeddingEventById(3L);

		HtmlPage page = webClient.getPage("/delete/3");

		// verify service call
		verify(weddingEventService, times(1)).deleteWeddingEventById(3L);

		// confirmation text
		String content = page.getBody().getTextContent();
		assertThat(content).contains("Event with ID 3 was deleted.");

		// link to create new and view all
		HtmlButton newButton = page.getElementByName("btn_new_event");
		assertThat(newButton).isNotNull();

		HtmlButton allButton = page.getElementByName("btn_all_events");
		assertThat(allButton).isNotNull();
	}

	private String removeWindowsCR(String s) {
		return s.replace("\r", "");
	}
}
