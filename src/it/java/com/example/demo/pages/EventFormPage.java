package com.example.demo.pages;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EventFormPage {
	private final WebDriver driver;
	private final WebDriverWait wait;

	public EventFormPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(d -> d.findElement(By.name("event_record")));
	}

	public EventFormPage setName(String name) {
		WebElement e = driver.findElement(By.id("name"));
		e.clear();
		e.sendKeys(name);
		return this;
	}

	public EventFormPage setDate(String isoDate) {
		WebElement e = driver.findElement(By.id("date"));
		e.clear();
		e.sendKeys(isoDate);
		return this;
	}

	public EventFormPage setLocation(String location) {
		WebElement e = driver.findElement(By.id("location"));
		e.clear();
		e.sendKeys(location);
		return this;
	}

	public EventListPage submit() {
		driver.findElement(By.cssSelector("button[type=submit]")).click();
		return new EventListPage(driver, 0);
	}

	public String attendingGuestsText() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div > ul > li")));

		List<WebElement> items = driver.findElements(By.cssSelector("div > ul > li"));
		if (items.isEmpty()) {
			return "";
		}
		return items.stream().map(WebElement::getText).collect(Collectors.joining("\n"));
	}
}
