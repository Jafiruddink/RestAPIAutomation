package ecommorceApp;

import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import payload.pojo.EcomLogin;
import payload.pojo.EcomLoginResponce;
import utility.Base;
import utility.Constants;
import utility.ExcelUtils;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EcomServicesTest extends Base{
	
	static String token;
	static String userId;
	static String productId;
	static String orderId;
	Constants resource;
	ExcelUtils util = new ExcelUtils();

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
	public void addProduct() throws IOException {
		ArrayList<String> data = util.GetData("EcomService", "addProduct");
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("productName", data.get(1));
		map.put("productAddedBy", userId);
		map.put("productCategory", data.get(2));
		map.put("productSubCategory", data.get(3));
		map.put("productPrice", data.get(4));
		map.put("productDescription", data.get(5));
		map.put("productFor", data.get(6));
		
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
	public void placeOrder() throws IOException {
		resource = Constants.valueOf("CreateOrder");
		ArrayList<String> data = util.GetData("EcomService", "placeOrder");
		
		Map<String,Object> orderMap = new HashMap<String, Object>();
		orderMap.put("country", data.get(1));
		orderMap.put("productOrderedId", productId);
		
		Map<String,Object> placeOrderMap = new HashMap<String, Object>();
		placeOrderMap.put("orders", orderMap);
		
		RequestSpecification requestPayload = given().spec(requestSpec())
				.header("Authorization", token)
				.header("Content-Type","application/json")
				.body(placeOrderMap);
		
		String response = requestPayload.when().post(resource.getResource()).then().log().all()
				.assertThat().statusCode(201).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String message = path.getString("message");
		
		orderId = path.getString("orders[0]");
		System.out.println(orderId+"++++++");
		
		Assert.assertEquals("Order Placed Successfully", message);
		
	}
	
	@Test(priority = 3, enabled = false)
	public void getOrderDetails() throws FileNotFoundException {
		resource = Constants.valueOf("GetOrderDetails");
		
		RequestSpecification requestPayload = given().spec(requestSpec())
				.header("Authorization", token)
				.queryParam("id", orderId);
				
		String response = requestPayload.when().get(resource.getResource()).then()
				.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String message = path.getString("message");
		String actualProductId = path.getString("data._id");
		
		Assert.assertEquals("Orders fetched for customer Successfully", message);
		Assert.assertTrue(actualProductId.equals(productId));
 	}
	
	@Test(priority = 4)
	public void deleteProduct() throws FileNotFoundException {
		resource = Constants.valueOf("DeleteProduct");
		
		RequestSpecification requestPayload = given().spec(requestSpec())
				.header("Authorization", token)
				.pathParam("key", productId);
		
		String response = requestPayload.when().delete(resource.getResource()+"/{key}")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String message = path.getString("message");
		
		Assert.assertEquals("Product Deleted Successfully", message);
	}
	
	@Test(priority = 5, enabled = false)
	public void deleteOrder() throws FileNotFoundException {
		resource = Constants.valueOf("DeleteOrder");
		
		RequestSpecification requestPayload = given().spec(requestSpec())
				.header("Authorization", token)
				.pathParam("key", orderId);
		
		String response = requestPayload.when().delete(resource.getResource()+"/{key}")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath path = new JsonPath(response);
		String message = path.getString("message");
		
		Assert.assertEquals("Orders Deleted Successfully", message);
	}
}
