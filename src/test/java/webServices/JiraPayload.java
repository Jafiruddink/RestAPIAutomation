package webServices;

public class JiraPayload {
	
	public static String createIssue(String summary, String desc) {
		String payload = "{\r\n"
				+ "    \"fields\": {\r\n"
				+ "       \"project\":\r\n"
				+ "       {\r\n"
				+ "          \"key\": \"SCRUM\"\r\n"
				+ "       },\r\n"
				+ "       \"summary\": \""+summary+"\",\r\n"
				+ "       \"description\": \""+desc+"\",\r\n"
				+ "       \"issuetype\": {\r\n"
				+ "          \"name\": \"Bug\"\r\n"
				+ "       }\r\n"
				+ "   }\r\n"
				+ "}";
		
		return payload;
	}

}
