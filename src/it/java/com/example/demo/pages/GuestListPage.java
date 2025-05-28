package com.example.demo.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GuestListPage {
	private final WebDriver driver;
	private final String url;
	private final WebDriverWait wait;

	public GuestListPage(WebDriver driver, int port) {
		this.driver = driver;
		this.url = "http://localhost:" + port + "/guests";
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	}

	public GuestListPage open() {
		driver.get(url);
		wait.until(d -> d.findElement(By.tagName("body")));
		return this;
	}

	public boolean isEmpty() {
		return !driver.findElements(By.id("guest_table")).isEmpty() ? false
				: !driver.findElements(By.xpath("//*[text()='No guest']")).isEmpty();
	}

	public String tableText() {
		List<WebElement> tables = driver.findElements(By.id("guest_table"));
		return tables.isEmpty() ? "" : tables.get(0).getText();
	}

	public GuestFormPage clickNew() {
		driver.findElement(By.cssSelector("a[href='/guests/new']")).click();
		return new GuestFormPage(driver, url);
	}

	public GuestFormPage clickEdit(long id) {
		driver.findElement(By.cssSelector("a[href='/guests/edit/" + id + "']")).click();
		return new GuestFormPage(driver, url);
	}

	public void clickDelete(long id) {
		driver.findElement(By.cssSelector("a[href='/guests/delete/" + id + "']")).click();
	}
}