package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;

public class Base {
	
	public static RequestSpecification request;

	public String getProperty(String key) {
		Properties prop = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\jafir\\eclipse-workspace\\RestAPIAutomation\\src\\main\\resources\\Config.properties");
			prop.load(fis);		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}
	
	public RequestSpecification requestSpec() throws FileNotFoundException {
		
		if(request == null) {
			PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
			
			request = new RequestSpecBuilder().setBaseUri(getProperty("baseUrl"))
					.addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log)).build();
			
			return request;
		}
		return request;
		
	}
}
