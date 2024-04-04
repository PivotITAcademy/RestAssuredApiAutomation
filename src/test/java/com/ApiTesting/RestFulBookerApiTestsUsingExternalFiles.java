package com.ApiTesting;

import org.hamcrest.core.IsNot;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;

import io.restassured.response.Response;
import pojo.BookingDates;
import pojo.CreateBookingRequest;
import utils.ExcelUtils;
import utils.JsonReader;

import static io.restassured.RestAssured.*;
//To use hamcrest matchers add below 2 imports
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/*
 * Given i have the baseurl, headers, params, request body
When i invoke the api
Then i should get an response and i will verify the status and response body
 */

public class RestFulBookerApiTestsUsingExternalFiles {

	Faker faker = new Faker();

	@BeforeClass
	public void setup() {
		// Set the base url
		baseURI = "https://restful-booker.herokuapp.com/";
	}

	@Test
	public void testCreateBookingUsingExternalFile() {

		String filePath = "src/test/resources/data/createBookingRequest.json";
		
		JsonObject jsonObject = JsonReader.readJson(filePath);
		
		
		given().basePath("booking").header("Content-Type", "application/json").body(jsonObject).log().all()
				.when().post().then().log().all().statusCode(200);
	}
	
//	@Test
//	public void testAPiUsingDataFromExcel() {
//		
//		
//		
//		
//		
//		given().basePath("booking").header("Content-Type", "application/json").body(createBookingRequest).log().all()
//				.when().post().then().log().all().statusCode(200).body("bookingid", isA(Integer.class)).body("booking.firstname",equalTo(createBookingRequest.getFirstname()))
//				.body("booking.lastname", equalTo(createBookingRequest.getLastname())).body("booking.totalprice", greaterThan(100))
//				.body("booking.bookingdates.checkin", equalTo(createBookingRequest.getBookingDates().getCheckin()));
//	}
	
	
}
