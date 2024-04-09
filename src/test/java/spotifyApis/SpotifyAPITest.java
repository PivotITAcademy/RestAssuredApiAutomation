package spotifyApis;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import utils.PropertyUtil;

import static io.restassured.RestAssured.*;
//To use hamcrest matchers add below 2 imports
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class SpotifyAPITest {

	@BeforeMethod
	public void setup() {
		baseURI = "https://api.spotify.com";
		basePath = "v1/artists";

	}

	@Test
	public void testSpotifyAPI() {
		String accessToken = SpotifyTokenGenerator.generateAuthToken();
		accessToken = "Bearer " + accessToken;

		String artistId = PropertyUtil.getProperty("src/test/resources/config.properties", "artistid");

		given().header("Authorization", accessToken).pathParam("id", artistId).log().all().when().get("/{id}").then().log().all()
				.statusCode(200);

	}
}
