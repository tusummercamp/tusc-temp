package com.visteoncloud.tusc.sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

	private final String USER_AGENT = "Mozilla/5.0";
	
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		
		// get logger
		LambdaLogger logger = context.getLogger();
		
		// get body
		JSONObject body = new JSONObject(input.getBody());
		String foo = body.getString("foo");
		Integer baz = body.getInt("baz");
		Integer temp = body.getInt("temp");
		
		// log body
		logger.log("Received request");
		logger.log("Method " + input.getHttpMethod());
		logger.log("Path " + input.getPath());
		logger.log("Raw body " + input.getBody());
		logger.log("foo " + foo);
		logger.log("baz " + baz);
		logger.log("temp " + temp);
		
		// TODO: handle request data here
		
		JSONObject responseBody = new JSONObject();
		responseBody.put("status", "ok");
		responseBody.put("temp", "ok");
		
		LambdaHandler http = new LambdaHandler();
		System.out.println("Send HTTP Get request");
		try {
			http.sendGet();
		} catch (Exception e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
		
		System.out.println("Send HTTP Post request");
		try {
			http.sendPost();
		} catch (Exception e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
		// create and return response
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		response.setBody(responseBody.toString());
		return response;
		
		}
	
	
	//GET REQUEST
	private void sendGet() throws Exception{
		
		String url = "https://i90jji9q5j.execute-api.us-east-1.amazonaws.com/Prod/data";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' Request to URL :" + url);
		System.out.println("\nResponse code :" + responseCode);
		
		BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine())!= null) {
			response.append(inputLine);		
		}
		in.close();
		System.out.println(response.toString());
	}
	
	//POST REQUEST
	private void sendPost() throws Exception{
		String url = "https://i90jji9q5j.execute-api.us-east-1.amazonaws.com/Prod/data";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
		
		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		//Send POST Request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeChars(
				"[{ \"time\": 1562514130, \"value\": 42.3 }, { \"time\": 1562514131, \"value\": 42.4 }]"
					 );
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' Request to URL :" + url);
		System.out.println("\nPost parameters :" + urlParameters);
		System.out.println("\nResponse code :" + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
	}

}