package com.visteoncloud.tusc.sample;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

	final static String USER_ID = "Tsenoslav";
	static DBClient dbClient = null;
	
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		
		// get logger
		LambdaLogger logger = context.getLogger();
		
		if(dbClient == null) {
			dbClient = new DBClient();
		}
		logger.log("Received request with method " + input.getHttpMethod());
		logger.log(input.getBody());
		
		APIGatewayProxyResponseEvent response;
		
		String method = input.getHttpMethod();
		if (method.equalsIgnoreCase("get")) {
			
			response = handleGet(input.getQueryStringParameters());
			
		} else if (method.equalsIgnoreCase("post")) {
			
			response = handlePost(input.getBody());
			
		} else {
			response = new APIGatewayProxyResponseEvent();
			response.setStatusCode(400);
			JSONObject responseBody = new JSONObject();
			responseBody.put("status", "error");
			responseBody.put("errorMessage", "Unsupported method: " + method);
			response.setBody(responseBody.toString());
		}
		return response;
	}
	private APIGatewayProxyResponseEvent handlePost(String body) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		JSONObject responseBody = new JSONObject();
		
		try {
			JSONArray requestBody = new JSONArray(body);
			HashMap<BigInteger, Float> dbData = new HashMap<BigInteger, Float>();
			for(int i=0; i<requestBody.length(); i++) {
				JSONObject obj = requestBody.getJSONObject(i);
				BigInteger time = obj.getBigInteger("time");
				Float value = obj.getFloat("value");
				dbData.put(time, value);
			}
			dbClient.createItems(USER_ID, dbData);
			responseBody.put("status", "ok");
			responseBody.put("data", dbData);
			response.setStatusCode(200);
			response.setBody(responseBody.toString(2));
		}
		catch (Exception e){
			responseBody.put("status","error");
			responseBody.put("errorMessage", e.toString());
			response.setStatusCode(400);
			response.setBody(responseBody.toString(2));
		}
		return response;
	}
	private APIGatewayProxyResponseEvent handleGet(Map<String, String> queryStringParameters) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		JSONObject responseBody = new JSONObject();
		try {
			BigInteger from = new BigInteger(queryStringParameters.get("from"));
			BigInteger to = new BigInteger(queryStringParameters.get("to"));

			JSONArray data = new JSONArray();
			HashMap<BigInteger, Float> result = dbClient.getItems(USER_ID, from, to);
			Iterator<Entry<BigInteger, Float>> it = result.entrySet().iterator();
			while (it.hasNext()) {
				Entry<BigInteger, Float> entry = it.next();
				JSONObject item = new JSONObject();
				item.put("time", entry.getKey());
				item.put("value", entry.getValue());
				data.put(item);
			}
			
			responseBody.put("status", "ok");
			responseBody.put("data", data);
			
			response.setStatusCode(200);
			response.setBody(responseBody.toString(2));
		}
		catch (Exception e) {

			responseBody.put("status", "error");
			responseBody.put("errorMessage", e.toString());
			
			response.setStatusCode(400);
			response.setBody(responseBody.toString());

		}
		
		
		return response;
	}

}

/*	
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
		
		// create and return response
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		response.setBody(responseBody.toString());
		return response;
		
		}
}
*/
	