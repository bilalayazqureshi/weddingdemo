package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

class WeddingEventWebE2E {

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	private static String baseUrl = "http://localhost:" + port;
	private static WebDriver driver;

	@BeforeAll
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new ChromeDriver();
	}

	@AfterAll
	public static void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	void test_CreateEventAndGuest() {
		// --- CREATE EVENT ---
		driver.get(baseUrl + "/new");
		driver.findElement(By.name("name")).sendKeys("My Party");
		WebElement dateField = driver.findElement(By.name("date"));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='2025-06-15';", dateField);
		driver.findElement(By.name("location")).sendKeys("The Beach");
		driver.findElement(By.name("btn_submit")).click();

		// verify event shows up on the events list
		driver.get(baseUrl + "/");
		WebElement eventTable = driver.findElement(By.id("event_table"));
		assertThat(eventTable.getText()).contains("My Party", "2025-06-15", "The Beach");

		// --- CREATE GUEST ---
		driver.get(baseUrl + "/guests");
		driver.findElement(By.linkText("New guest")).click();
		driver.findElement(By.name("name")).sendKeys("Alice");
		driver.findElement(By.name("email")).sendKeys("alice@example.com");

		// select the event we just created
		WebElement eventSelect = driver.findElement(By.id("event"));
		WebElement option = eventSelect.findElement(By.xpath("//option[text()='My Party']"));
		option.click();

		driver.findElement(By.name("btn_submit")).click();

		// verify guest shows on the guest list
		driver.get(baseUrl + "/guests");
		WebElement guestTable = driver.findElement(By.id("guest_table"));
		assertThat(guestTable.getText()).contains("Alice", "alice@example.com", "My Party");
	}

	@Test
	void test_DeleteEvent() {
		driver.get(baseUrl + "/");
		String both = driver.findElement(By.tagName("body")).getText();
		assertThat(both).contains("My Party");

		WebElement myPartyRow = driver.findElement(By.xpath("//tr[td/text() = 'My Party']"));
		myPartyRow.findElement(By.cssSelector("a.btn.danger")).click();

		String conf = driver.findElement(By.tagName("h1")).getText();
		assertThat(conf).contains("Event with ID");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("btn_all_events"))).click();

		String afterDelete = driver.findElement(By.tagName("body")).getText();
		assertThat(afterDelete).doesNotContain("My Party");
	}
	

}
