package com.ApiTesting;

import static io.restassured.RestAssured.*;
//To use hamcrest matchers add below 2 imports
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;


import io.restassured.response.Response;
import pojo.BookingDates;
import pojo.CreateBookingRequest;
import utils.TokenGenerator;

public class RestfulBookerAPIWithAuth {

	Faker faker = new Faker();
	
	CreateBookingRequest createBookingRequest = new CreateBookingRequest();
	TestContext context = new TestContext();
	
	@BeforeMethod
	public void setup() {
		// Set the base url
		baseURI = "https://restful-booker.herokuapp.com/";
		basePath ="booking";
	}
	
	@Test
	public void testUpdateBooking() {
		
		String token = TokenGenerator.generateToken();
		System.out.println(token);
		
		//Create a booking and extract Booking id
		createBooking();
		
		createBookingRequest.setFirstname("vishnu");
		
		//Call the update API - updateBookingResponseSchema.json
		 given().basePath("booking").header("Content-Type","application/json").header("Accept","application/json").header("Cookie","token="+token)
					.pathParam("id", context.bookingId).body(createBookingRequest).log().all().when().put("/{id}").then().log().all().statusCode(200).
					body("firstname", equalTo(createBookingRequest.getFirstname()));
		
		
	}
	
	
	public void createBooking() {
		
		String bookingid =null;
		
		
		
		createBookingRequest.setFirstname(faker.name().firstName());
		createBookingRequest.setLastname(faker.name().lastName());
		createBookingRequest.setPrice(111);
		createBookingRequest.setDepositpaid(true);
		createBookingRequest.setBookingDates(new BookingDates("2018-01-01", "2018-01-05"));
		createBookingRequest.setAdditionalneeds("breakfast");
		
		context.response = given().basePath("booking").header("Content-Type","application/json").header("Accept","application/json").body(createBookingRequest).log().all().when().post();
	 	
	 	 System.out.println("Create booking response : "+context.response.asPrettyString());
	 	 
	 	context.bookingId = context.response.getBody().jsonPath().getString("bookingid");
	 			
		
	}
}
