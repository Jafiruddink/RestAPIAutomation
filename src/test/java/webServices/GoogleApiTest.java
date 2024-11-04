package webServices;

import static io.restassured.RestAssured.*;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo.AddPlace;
import pojo.Location;

import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

public class GoogleApiTest {

	@Test(enabled = false)
	public void googlePlaceApi() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//Add Place API
		String responce = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(GooglePayload.addPayload())
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.body("status", equalTo("OK")).extract().response().asString();
		
		JsonPath path = new JsonPath(responce);
		String placeId = path.get("place_id");
		System.out.println(placeId);
		
		//Get Place API
		given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).body("name", equalTo("HM house"))
		.body("address", equalTo("29, side layout, cohen 09"));
		
		String address = "10 Summer walk, USA";
		
		//Update Place
		given().log().all().queryParam("key", "qaclick123")
		.header("Content-Type","application/json").body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+address+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		String getResponce = given().log().all().queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json")
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();		
		
		JsonPath path1 = new JsonPath(getResponce);
		String actualAddress = path1.get("address");
		System.out.println(actualAddress);
		
		Assert.assertEquals(address, actualAddress);
		
		//Delete Place
		given().log().all().queryParam("key", "qaclick123")
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\"\r\n"
				+ "}")
		.when().delete("maps/api/place/delete/json")
		.then().log().all().assertThat().statusCode(200).body("status", equalTo("OK"));
		
		//Get Place
		given().log().all().queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json")
				.then().log().all().assertThat().statusCode(404)
				.body("msg", equalTo("Get operation failed, looks like place_id  doesn't exists"));		

	}
	
//	@Test
//	public void getPlace() {
//		RestAssured.baseURI = "https://rahulshettyacademy.com";
//		given().log().all().queryParam("key", "qaclick123")
//		.header("Content-Type","application/json")
//		.when().get("maps/api/place/get/json")
//		.then().log().all().assertThat().statusCode(200);
//	}
	
	@Test
	public void addPlace() {
		
		List<String> type = Arrays.asList("shoe park", "shop");
		Location location = new Location();
		location.setLat(-38.383494);
		location.setLng(33.427362);
		
		AddPlace place = new AddPlace();
		place.setAccuracy(60);
		place.setLanguage("English-IN");
		place.setAddress("29, side layout, cohen 09");
		place.setPhone_number("(+91) 983 893 3937");
		place.setName("HM house");
		place.setWebsite("http://google.com");
		place.setTypes(type);
		place.setLocation(location);
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//Add Place API
		String responce = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(place)
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.body("status", equalTo("OK")).extract().response().asString();
		
		JsonPath path = new JsonPath(responce);
		String placeId = path.get("place_id");
		System.out.println(placeId);
	}
}
