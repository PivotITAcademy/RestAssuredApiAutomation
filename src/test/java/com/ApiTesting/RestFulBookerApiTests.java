package com.ApiTesting;

import org.hamcrest.core.IsNot;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;

import io.restassured.response.Response;
import pojo.BookingDates;
import pojo.CreateBookingRequest;

import static io.restassured.RestAssured.*;
//To use hamcrest matchers add below 2 imports
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/*
 * Given i have the baseurl, headers, params, request body
When i invoke the api
Then i should get an response and i will verify the status and response body
 */

public class RestFulBookerApiTests {

	Faker faker = new Faker();

	@BeforeClass
	public void setup() {
		// Set the base url
		baseURI = "https://restful-booker.herokuapp.com/";
	}

	@Test
	public void testGetbooking() {

		given().basePath("booking").log().all().when().get().then().log().all().statusCode(200);
	}

	@Test
	public void testGetBookingWithQueryParams() {

		given().basePath("booking").param("firstname", "Jake").param("lastname", "Smith").log().all().when().get()
				.then().log().all().statusCode(200);
	}

	@Test
	public void testGetBookingWithPathParams() {

		given().basePath("booking").pathParam("id", 1668).log().all().when().get("/{id}").then().log().all()
				.statusCode(200);
	}

	@Test
	public void testCreateBooking() {

		String requestBody = "{\r\n" + "    \"firstname\" : \"Jim\",\r\n" + "    \"lastname\" : \"Brown\",\r\n"
				+ "    \"totalprice\" : 111,\r\n" + "    \"depositpaid\" : true,\r\n" + "    \"bookingdates\" : {\r\n"
				+ "        \"checkin\" : \"2018-01-01\",\r\n" + "        \"checkout\" : \"2019-01-01\"\r\n"
				+ "    },\r\n" + "    \"additionalneeds\" : \"Breakfast\"\r\n" + "}";

		
		given().basePath("booking").header("Content-Type", "application/json").body(requestBody).log().all().when()
				.post().then().log().all().statusCode(200);
	}

	@Test
	public void testCreateBookingUsingPojo() {

		CreateBookingRequest createBookingRequest = new CreateBookingRequest();
		createBookingRequest.setFirstname(faker.name().firstName());
		createBookingRequest.setLastname(faker.name().lastName());
		createBookingRequest.setPrice(111);
		createBookingRequest.setDepositpaid(true);
		createBookingRequest.setBookingDates(new BookingDates("2018-01-01", "2018-01-05"));
		createBookingRequest.setAdditionalneeds("breakfast");

		System.out.println(createBookingRequest.toString());
		
		
		
		given().basePath("booking").header("Content-Type", "application/json").body(createBookingRequest).log().all()
				.when().post().then().log().all().statusCode(200);
	}
	
	@Test
	public void testResponseBodyValidationsUsingHamcrestMatchers() {
		
		CreateBookingRequest createBookingRequest = new CreateBookingRequest();
		
		createBookingRequest.setFirstname(faker.name().firstName());
		createBookingRequest.setLastname(faker.name().lastName());
		createBookingRequest.setPrice(111);
		createBookingRequest.setDepositpaid(true);
		createBookingRequest.setBookingDates(new BookingDates("2018-01-01", "2018-01-05"));
		createBookingRequest.setAdditionalneeds("breakfast");

		System.out.println(createBookingRequest.toString());
		
		
		
		given().basePath("booking").header("Content-Type", "application/json").body(createBookingRequest).log().all()
				.when().post().then().log().all().statusCode(200).body("bookingid", isA(Integer.class)).body("booking.firstname",equalTo(createBookingRequest.getFirstname()))
				.body("booking.lastname", equalTo(createBookingRequest.getLastname())).body("booking.totalprice", greaterThan(100))
				.body("booking.bookingdates.checkin", equalTo(createBookingRequest.getBookingDates().getCheckin()));
	}
	
	@Test
	public void testResponseBodyValidationsUsingJsonpath() {
		
		CreateBookingRequest createBookingRequest = new CreateBookingRequest();
		
		createBookingRequest.setFirstname(faker.name().firstName());
		createBookingRequest.setLastname(faker.name().lastName());
		createBookingRequest.setPrice(111);
		createBookingRequest.setDepositpaid(true);
		createBookingRequest.setBookingDates(new BookingDates("2018-01-01", "2018-01-05"));
		createBookingRequest.setAdditionalneeds("breakfast");

		System.out.println(createBookingRequest.toString());
		
		Response response =null;
		
		response = given().basePath("booking").header("Content-Type", "application/json").body(createBookingRequest).log().all()
				.when().post();
		
		//Validating response body using Jsonpath
		Assert.assertNotNull(response.getBody().jsonPath().getString("bookingid"));
		Object bookingid = response.getBody().jsonPath().get("bookingid");
	
		Assert.assertTrue(bookingid instanceof Integer);
		
		Assert.assertEquals(response.getBody().jsonPath().getString("booking.firstname"), createBookingRequest.getFirstname());
		Assert.assertEquals(response.getBody().jsonPath().getString("booking.lastname"), createBookingRequest.getLastname());
		Assert.assertEquals(response.getBody().jsonPath().getDouble("booking.totalprice"), createBookingRequest.getPrice());
		Assert.assertTrue(response.getBody().jsonPath().getBoolean("booking.depositpaid"));
		Assert.assertEquals(response.getBody().jsonPath().getString("booking.additionalneeds"), createBookingRequest.getAdditionalneeds());
	}
}
