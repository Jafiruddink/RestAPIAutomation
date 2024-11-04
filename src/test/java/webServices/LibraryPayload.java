package webServices;

public class LibraryPayload {

	public static String addPayload(String isbn, String aisle) {
		String payload = "{\r\n"
				+ "\r\n"
				+ "\"name\":\"Learn Appium Automation with Java\",\r\n"
				+ "\"isbn\":\""+isbn+"\",\r\n"
				+ "\"aisle\":\""+aisle+"\",\r\n"
				+ "\"author\":\"John foe\"\r\n"
				+ "}\r\n"
				+ "";
		
		return payload;
	}
	
	public static String deletePayload(String id) {
		String payload = "{\r\n"
				+ " \r\n"
				+ "\"ID\" : \""+id+"\"\r\n"
				+ "\r\n"
				+ "} \r\n"
				+ "";
		
		return payload;
	}
}
