package com.example.demo.controllers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.model.Guest;
import com.example.demo.service.GuestService;
import com.example.demo.service.WeddingEventService;

@WebMvcTest(controllers = GuestWebController.class)
class GuestWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@SuppressWarnings("removal")
	@MockBean
	private GuestService guestService;

	@SuppressWarnings("removal")
	@MockBean
	private WeddingEventService weddingEventService;

	@Test
	void test_HomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/guests");
		assertThat(page.getTitleText()).isEqualTo("Guests");
	}

	@Test
	void testHomePageWithNoGuests() throws Exception {
		when(guestService.getAllGuests()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/guests");
		assertThat(page.getBody().getTextContent()).contains("No guest");
	}

	@Test
	void test_HomePage_ShouldProvideALinkForCreatingANewGuest() throws Exception {
		HtmlPage page = webClient.getPage("/guests");
		assertThat(page.getAnchorByText("New guest").getHrefAttribute()).isEqualTo("/guests/new");
	}

	@Test
	void test_HomePageWithGuests_ShouldShowThemInATable() throws Exception {
		// given
		Guest g1 = new Guest(1L, "Alice", "alice@example.com");
		Guest g2 = new Guest(2L, "Bob", "bob@example.com");
		when(guestService.getAllGuests()).thenReturn(asList(g1, g2));

		// when
		HtmlPage page = webClient.getPage("/guests");

		// then
		assertThat(page.getBody().getTextContent()).doesNotContain("No guest");

		HtmlTable table = page.getHtmlElementById("guest_table");
		String normalized = removeWindowsCR(table.asNormalizedText());

		assertThat(normalized).isEqualTo("""
				Guests
				ID\tName\tEmail\tEvent\tEdit\tDelete
				1\tAlice\talice@example.com\t—\tEdit\tDelete
				2\tBob\tbob@example.com\t—\tEdit\tDelete""");

		page.getAnchorByHref("/guests/edit/1");
		page.getAnchorByHref("/guests/edit/2");
	}

	@Test
	void testEditNonExistentGuest() throws Exception {
		when(guestService.getGuestById(1L)).thenReturn(null);
		HtmlPage page = webClient.getPage("/guests/edit/1");
		assertThat(page.getBody().getTextContent()).contains("No guest found with id: 1");
	}

	@Test
	void testEditExistentGuest() throws Exception {
		Guest original = new Guest(1L, "Original", "orig@example.com");
		when(guestService.getGuestById(1L)).thenReturn(original);

		HtmlPage page = webClient.getPage("/guests/edit/1");
		HtmlForm form = page.getFormByName("guest_record");

		form.getInputByValue("Original").setValueAttribute("Modified");
		form.getInputByValue("orig@example.com").setValueAttribute("mod@example.com");
		form.getButtonByName("btn_submit").click();

		verify(guestService).updateGuestById(1L, new Guest(1L, "Modified", "mod@example.com"));
	}

	@Test
	void testEditNewGuest() throws Exception {
		HtmlPage page = webClient.getPage("/guests/new");
		HtmlForm form = page.getFormByName("guest_record");

		form.getInputByName("name").setValueAttribute("NewGuest");
		form.getInputByName("email").setValueAttribute("newguest@example.com");
		form.getButtonByName("btn_submit").click();

		verify(guestService).insertNewGuest(new Guest(null, "NewGuest", "newguest@example.com"));
	}

	@Test
	void testDeleteGuest_ShouldDisplayConfirmationMessage() throws Exception {
		doNothing().when(guestService).deleteGuestById(3L);

		HtmlPage page = webClient.getPage("/guests/delete/3");

		verify(guestService, times(1)).deleteGuestById(3L);

		String content = page.getBody().getTextContent();
		assertThat(content).contains("Guest with ID 3 was deleted.");

		HtmlButton newButton = page.getElementByName("btn_new_guest");
		assertThat(newButton).isNotNull();

		HtmlButton allButton = page.getElementByName("btn_all_guests");
		assertThat(allButton).isNotNull();
	}

	private String removeWindowsCR(String s) {
		return s.replace("\r", "");
	}

}
