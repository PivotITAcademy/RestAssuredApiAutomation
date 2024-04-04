package com.ApiTesting;

import static io.restassured.RestAssured.*;
//To use hamcrest matchers add below 2 imports
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import utils.TokenGenerator;

public class RestfulBookerAPIWithAuth {

	Faker faker = new Faker();

	@BeforeClass
	public void setup() {
		// Set the base url
		baseURI = "https://restful-booker.herokuapp.com/";
		basePath ="booking";
	}
	
	@Test
	public void testUpdateBooking() {
		
		String token = TokenGenerator.generateToken();
		System.out.println(token);
		
		
	}

}
