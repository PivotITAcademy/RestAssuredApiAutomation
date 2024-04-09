package spotifyApis;

import static io.restassured.RestAssured.*;
//To use hamcrest matchers add below 2 imports
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.time.Instant;
import java.util.Base64;

import org.testng.annotations.BeforeMethod;

import io.restassured.response.Response;

public class SpotifyTokenGenerator {

	@BeforeMethod
	public void setup() {
		baseURI = "https://accounts.spotify.com/";
		basePath = "api/token";

	}

	public static String generateAuthToken() {
		
		String accessToken=null;
		int expiryTime=0;
		
		String clientCredentials = "0beb3fb0fa3340768947ee50fd2e10c0:f97ae1455185433ba900d30acd20cc6b";
		
		String encodedCredentials = "Basic "+Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		
		Instant expectedExpiryTime = null;
		
		if(expectedExpiryTime==null||Instant.now().isAfter(expectedExpiryTime)) {
			
		
		Response response = given().baseUri("https://accounts.spotify.com/").basePath("api/token").header("Authorization",encodedCredentials).header("Content-Type","application/x-www-form-urlencoded")
		.formParam("grant_type", "client_credentials").log().all().when().post();
		
		System.out.println(response.asPrettyString());
		
		accessToken= response.body().jsonPath().getString("access_token");
		
		 expiryTime= response.body().jsonPath().getInt("expires_in");
		 
		 expectedExpiryTime = Instant.now().plusSeconds(expiryTime);
		 
		}
		
		return accessToken;
		
	}

}
