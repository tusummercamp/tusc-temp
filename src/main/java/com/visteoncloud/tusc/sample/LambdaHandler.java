package com.visteoncloud.tusc.sample;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {

	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		
		// get logger
		LambdaLogger logger = context.getLogger();
		
		// get body
		JSONObject body = new JSONObject(input.getBody());
		String foo = body.getString("foo");
		Integer baz = body.getInt("baz");
		
		// log body
		logger.log("Received request");
		logger.log("Method " + input.getHttpMethod());
		logger.log("Path " + input.getPath());
		logger.log("Raw body " + input.getBody());
		logger.log("foo " + foo);
		logger.log("baz " + baz);
		
		// TODO: handle request data here
		
		JSONObject responseBody = new JSONObject();
		responseBody.put("status", "ok");
		
		// create and return response
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
		response.setStatusCode(200);
		response.setBody(responseBody.toString());
		return response;
	}

}
