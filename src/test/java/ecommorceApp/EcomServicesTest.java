package ecommorceApp;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import payload.pojo.EcomLogin;
import payload.pojo.EcomLoginResponce;
import utility.Base;
import utility.Constants;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EcomServicesTest extends Base{
	
	static String token;
	static String userId;
	static String productId;
	Constants resource;

	@Test(priority = 0)
	public void login() throws FileNotFoundException {
		EcomLogin eLogin = new EcomLogin();
		eLogin.setUserEmail("jak@domain.com");
		eLogin.setUserPassword("Test@123");
		
		resource = Constants.valueOf("Login");
		
		RequestSpecification requestPayload = given().spec(requestSpec()).body(eLogin).header("Content-Type","application/json");
		
		EcomLoginResponce response = requestPayload.when().post(resource.getResource())
				.then().assertThat().statusCode(200).extract().response().as(EcomLoginResponce.class);
		
		token = response.getToken();
		userId = response.getUserId();
		System.out.println(token);
		
		Assert.assertEquals(response.getMessage(), "Login Successfully");
	}
	
	@Test(priority = 1)
	public void addProduct() throws FileNotFoundException {
		Map<String,String> map = new HashMap<String, String>();
		map.put("productName", "ABC");
		map.put("productAddedBy", userId);
		map.put("productCategory", "fashion");
		map.put("productSubCategory", "shirts");
		map.put("productPrice", "11500");
		map.put("productDescription", "Addias Originals");
		map.put("productFor", "man");
		
		resource = Constants.valueOf("AddProduct");
		
		RequestSpecification requestPayload = given().spec(requestSpec())
				.header("Authorization", token)
				.formParams(map).multiPart("productImage", new File("C:\\Users\\jafir\\Downloads\\shirt.jpg"));
		
		String response = requestPayload.when().post(resource.getResource())
				.then().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		productId = path.getString("productId");
		System.out.println(productId);
		
		String message = path.getString("message");
		
		Assert.assertEquals("Product Added Successfully", message);
	}
	
	@Test(priority = 2)
	public void placeOrder() throws FileNotFoundException {
		resource = Constants.valueOf("CreateOrder");
		
		RequestSpecification requestPayload = given().spec(requestSpec())
				.header("Authorization", token)
				.header("Content-Type","application/json")
				.body("{\r\n"
						+ "    \"orders\": [\r\n"
						+ "        {\r\n"
						+ "            \"country\": \"India\",\r\n"
						+ "            \"productOrderedId\": \""+productId+"\"\r\n"
						+ "        }\r\n"
						+ "    ]\r\n"
						+ "}");
		
		String response = requestPayload.when().post(resource.getResource()).then()
				.assertThat().statusCode(201).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String message = path.getString("message");
		
		Assert.assertEquals("Order Placed Successfully", message);
		
	}
	
	@Test(priority = 3)
	public void deleteProduct() throws FileNotFoundException {
		resource = Constants.valueOf("DeleteOrder");
		
		RequestSpecification requestPayload = given().spec(requestSpec())
				.header("Authorization", token)
				.pathParam("key", productId);
		
		String response = requestPayload.when().delete(resource.getResource()+"/{key}")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String message = path.getString("message");
		
		Assert.assertEquals("Product Deleted Successfully", message);
	}
}
