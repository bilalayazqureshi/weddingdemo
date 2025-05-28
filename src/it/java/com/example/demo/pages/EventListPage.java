package com.example.demo.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EventListPage {
	private final WebDriver driver;
	private final String url;
	private final WebDriverWait wait;

	public EventListPage(WebDriver driver, int port) {
		this.driver = driver;
		this.url = "http://localhost:" + port + "/";
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	}

	public EventListPage open() {
		driver.get(url);
		wait.until(d -> d.findElement(By.tagName("body")));
		return this;
	}

	public boolean isEmpty() {
		return driver.findElements(By.id("event_table")).isEmpty()
				&& !driver.findElements(By.xpath("//*[text()='No event']")).isEmpty();
	}

	public String tableText() {
		List<WebElement> tables = driver.findElements(By.id("event_table"));
		return tables.isEmpty() ? "" : tables.get(0).getText();
	}

	public EventFormPage clickNew() {
		driver.findElement(By.cssSelector("a[href='/new']")).click();
		return new EventFormPage(driver);
	}

	public EventFormPage clickEdit(long id) {
		driver.findElement(By.cssSelector("a[href='/edit/" + id + "']")).click();
		return new EventFormPage(driver);
	}

	public void clickDelete(long id) {
		driver.findElement(By.cssSelector("a[href='/delete/" + id + "']")).click();
	}
}
