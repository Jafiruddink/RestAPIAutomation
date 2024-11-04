package webServices;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

public class RequestResponseSpecTest {
	
	@Test
	public void reqResSpecBuilder() {
		
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
		
		RequestSpecification request = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		
		ResponseSpecification response = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		RequestSpecification res = given().spec(request).body(place);
		
		//Add Place API
		String responce = res.when().post("maps/api/place/add/json")
		.then().spec(response).extract().response().asString();
		
		JsonPath path = new JsonPath(responce);
		String placeId = path.get("place_id");
		System.out.println(placeId);
	}

}
