package com.visteoncloud.tusc.sample;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class LambdaTester {

	public static void main(String[] args) {
		
		// create request body
		JSONObject requestBody = new JSONObject();
		requestBody.put("foo", "bar");
		requestBody.put("baz", 42);
		
		// create request
		APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
		request.setHttpMethod("get");
		request.setBody(requestBody.toString());
		request.setPath("/path");
		
		// create handler
		LambdaHandler handler = new LambdaHandler();
		APIGatewayProxyResponseEvent response = handler.handleRequest(request, new TestLambdaContext());

	}

}
