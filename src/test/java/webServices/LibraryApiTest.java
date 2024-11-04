package webServices;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LibraryApiTest {
	
	@Test(enabled = false)
	public void libraryServices() {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String responce = given().log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\r\n"
				+ "\"name\":\"Learn Appium Automation with Java\",\r\n"
				+ "\"isbn\":\"ebcd\",\r\n"
				+ "\"aisle\":\"1227\",\r\n"
				+ "\"author\":\"John foe\"\r\n"
				+ "}")
		.when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.body("Msg", equalTo("successfully added"))
		.extract().response().asString();
		
		JsonPath path = new JsonPath(responce);
		String generatedId = path.get("ID");
		System.out.println(generatedId);
		
		//Get call
		String getResponce = given().log().all()
				.queryParam("ID", generatedId).when().get("Library/GetBook.php")
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();
		
		JsonPath path1 = new JsonPath(getResponce);
		String actualISBN = path1.getString("[0].isbn");
		String actualAisle = path1.getString("[0].aisle");
		System.out.println(actualISBN);
		System.out.println(actualAisle);
		
		Assert.assertEquals(generatedId, actualISBN+actualAisle);
		
		//Delete call
		given().log().all().header("Content-Type","application/json")
		.body("{\r\n"
				+ " \r\n"
				+ "\"ID\" : \""+generatedId+"\"\r\n"
				+ " \r\n"
				+ "} ")
		.when().post("Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200)
		.body("msg", equalTo("book is successfully deleted"));
		
		given().log().all()
		.queryParam("ID", generatedId).when().get("Library/GetBook.php")
		.then().log().all().assertThat().statusCode(404)
		.body("msg", equalTo("The book by requested bookid / author name does not exists!"));
	}

	//Adding books using parameterized data
	@Test(dataProvider = "booksData")
	public void addBooks(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		
		String responce = given().log().all().header("Content-Type", "application/json")
		.body(LibraryPayload.addPayload(isbn, aisle))
		.when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.body("Msg", equalTo("successfully added"))
		.extract().response().asString();
		
		JsonPath path = new JsonPath(responce);
		String generatedId = path.get("ID");
		System.out.println(generatedId);
		
		given().log().all().header("Content-Type","application/json")
		.body(LibraryPayload.deletePayload(generatedId))
		.when().post("Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200)
		.body("msg", equalTo("book is successfully deleted"));
	}
	
	@DataProvider(name="booksData")
	public Object[][] getData(){
		Object[][] data = {{"absd","2367"},{"rtqv","1278"},{"uias","1289"}};
		return data;
	}
}
