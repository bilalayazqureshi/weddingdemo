package com.example.demo.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GuestFormPage {
	private final WebDriver driver;
	private final WebDriverWait wait;

	public GuestFormPage(WebDriver driver, String cameFromUrl) {
		this.driver = driver;
		// wait until form is present
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(d -> d.findElement(By.name("guest_record")));
	}

	/** Fill name */
	public GuestFormPage setName(String name) {
		WebElement e = driver.findElement(By.id("name"));
		e.clear();
		e.sendKeys(name);
		return this;
	}

	/** Fill email */
	public GuestFormPage setEmail(String email) {
		WebElement e = driver.findElement(By.id("email"));
		e.clear();
		e.sendKeys(email);
		return this;
	}

	/** Select event by its visible text */
	public GuestFormPage selectEvent(String eventName) {
		WebElement sel = driver.findElement(By.id("event"));
		new Select(sel).selectByVisibleText(eventName);
		return this;
	}

	/** Submit the form */
	public GuestListPage submit() {
		driver.findElement(By.cssSelector("button[type=submit]")).click();
		// back to list
		return new GuestListPage(driver, 0);
	}
}