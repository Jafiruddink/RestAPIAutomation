package webServices;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParsingTest {
	
	JsonPath path = new JsonPath(CoursePricePayload.coursePayload());
	
	@Test
	public void getNumberOfCourses() {
		int noOfCourse = path.getInt("courses.size()");
		System.out.println(noOfCourse);
	}
	
	@Test
	public void printPurchageAmount() {
		int amount = path.getInt("dashboard.purchaseAmount");
		System.out.println(amount);
	}
	
	@Test
	public void printTitleOfFirstCourse() {
		String title = path.get("courses[0].title");
		System.out.println(title);
	}
	
	@Test
	public void printCourseTitleAndPrice() {
		int noOfCourse = path.getInt("courses.size()");
		for(int i=0; i<noOfCourse; i++) {
			String title = path.get("courses["+i+"].title");
			System.out.println(title);
			int price = path.getInt("courses["+i+"].price");
			System.out.println(price);
		}
	}
		
		@Test
		public void printNoOfCopiesSold() {
			int noOfCourse = path.getInt("courses.size()");
			
			for(int i=0; i<noOfCourse; i++) {
				String title = path.get("courses["+i+"].title");
				
				if(title.equalsIgnoreCase("RPA")) {
					int price = path.getInt("courses["+i+"].price");
					System.out.println("Price of RPA : " + price);
				}
			}	
			
		}
		
		@Test
		public void sumOfPriceMatchesPurchageAmount() {
			int noOfCourse = path.getInt("courses.size()");
			int amount = path.getInt("dashboard.purchaseAmount");
			int sum = 0;
			
			for(int i=0; i<noOfCourse; i++) {
				int price = path.getInt("courses["+i+"].price");
				int copies = path.getInt("courses["+i+"].copies");
				
				sum = sum + (price * copies);
			}	
			System.out.println("Sum of all courses : "+ sum);
			
			Assert.assertEquals(sum, amount);
		}

}
