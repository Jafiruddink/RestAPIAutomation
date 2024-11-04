package webServices;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.annotations.Test;

public class JiraAPITest {
	
	@Test
	public void createIssue() {
		RestAssured.baseURI = "https://jafiruddinkhan.atlassian.net";
		
		String response = given().header("Content-Type","application/json")
				.header("Authorization","Basic amFmaXJ1ZGRpbmtoYW5Ab3V0bG9vay5jb206QVRBVFQzeEZmR0YwM0RqWEcyS1dfbDlMaFk4c243VW94XzlVaTlQaHhvVGZ2S3JsVk5WekxYMi1pWXBmbzNzaWVWT3FHVnNsLTIzNkthTWRmQzJteWZadTgyS3BXNG83REE0MlB2VWhFZ190MGljWVVjWWhUTnFKQUpnLUw0SkxGT3hJUUxyNnkzbUQ3RklKMDBHZmd0U1c4MXdvRFdWTS1udGhMVHJfSE83ZU15YUl2MFhkcDl3PTdCRTc5QzdG")
				.body(JiraPayload.createIssue("Link Issue - Automation", "Page link broken"))
				.when().post("rest/api/2/issue")
				.then().log().all().assertThat().statusCode(201)
				.extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String id = path.getString("id");
		System.out.println(id);
		
		//Add attachment
		given().pathParam("key", id).header("X-Atlassian-Token","no-check")
		.header("Authorization","Basic amFmaXJ1ZGRpbmtoYW5Ab3V0bG9vay5jb206QVRBVFQzeEZmR0YwM0RqWEcyS1dfbDlMaFk4c243VW94XzlVaTlQaHhvVGZ2S3JsVk5WekxYMi1pWXBmbzNzaWVWT3FHVnNsLTIzNkthTWRmQzJteWZadTgyS3BXNG83REE0MlB2VWhFZ190MGljWVVjWWhUTnFKQUpnLUw0SkxGT3hJUUxyNnkzbUQ3RklKMDBHZmd0U1c4MXdvRFdWTS1udGhMVHJfSE83ZU15YUl2MFhkcDl3PTdCRTc5QzdG")
		.multiPart("file", new File("C:\\Users\\jafir\\Downloads\\download.jpg"))
		.when().post("rest/api/2/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
	}

}
