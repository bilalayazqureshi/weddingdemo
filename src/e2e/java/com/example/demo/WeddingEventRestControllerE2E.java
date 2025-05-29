package com.example.demo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

class WeddingEventRestControllerE2E {


	private static final String BASE_URI = "http://localhost";
	private static final int PORT = 8080;
	private static final String EVENT_ENDPOINT = "/api/weddingevents";

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = BASE_URI;
		RestAssured.port = PORT;
	}

	@Test
	void test_CreateEvent() {
		String newEventJson = """
				{
				  "name": "wedding",
				  "date": "2025-06-15",
				  "location": "The Beach"
				}
				""";

		createEvent(newEventJson).then().statusCode(200).contentType(ContentType.JSON).body("id", notNullValue())
				.body("name", equalTo("wedding")).body("date", equalTo("2025-06-15"))
				.body("location", equalTo("The Beach"));
	}


	private Response createEvent(String body) {
		return given().contentType(ContentType.JSON).body(body).when().post(EVENT_ENDPOINT + "/new");
	}

	
}
