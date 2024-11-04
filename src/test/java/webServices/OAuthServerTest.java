package webServices;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import pojo.CourseDetails;

public class OAuthServerTest {

	@Test
	public void authServer() {
		
		String response = given().formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.formParam("grant_type", "client_credentials").formParam("scope", "trust")
		.when().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String token = path.getString("access_token");
		System.out.println(token);
		
		//Get course details
		getCourseDetails(token);
	}
	
	public void getCourseDetails(String token) {
		List<String> actualCourse = new ArrayList<String>();
		List<String> expectedCourse = Arrays.asList("Selenium Webdriver Java", "Cypress", "Protractor");
		
		CourseDetails gc = given().queryParam("access_token", token)
		.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
		.as(CourseDetails.class);
		
		System.out.println(gc.getInstructor());
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getUrl());
		
		//Fetching the price for specific course.
		int count = gc.getCourses().getApi().size();
		for(int i=0; i<count; i++) {
			if(gc.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
				System.out.println(gc.getCourses().getApi().get(i).getPrice());
		}
		
		//Fetching all courses present in Web automation and validate with user data
		int count1 = gc.getCourses().getWebAutomation().size();
		for(int i=0; i<count1; i++) {
			actualCourse.add(gc.getCourses().getWebAutomation().get(i).getCourseTitle());
		}
		System.out.println(actualCourse);
		Assert.assertTrue(actualCourse.equals(expectedCourse));
		
		//Fetching all the courses and respective price for mobile
		int count2 = gc.getCourses().getApi().size();
		for(int i=0; i<count2; i++) {
			System.out.println(gc.getCourses().getApi().get(i).getCourseTitle());
			System.out.println(gc.getCourses().getApi().get(i).getPrice());
		}
		
		int count3 = gc.getCourses().getMobile().size();
		for(int i=0; i<count3; i++) {
			System.out.println(gc.getCourses().getMobile().get(i).getCourseTitle());
			System.out.println(gc.getCourses().getMobile().get(i).getPrice());
		}
		
	}
}
