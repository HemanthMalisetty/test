package com.helper;

import java.io.IOException;
import java.util.HashMap;

import com.utilities.propertyReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SMS_US {
	String sheetName = "PaymentService";
	HashMap<String, String> values = new HashMap<>();
	RequestSpecification httpRequest;
	private static final SMS_US SMSobject = new SMS_US();

	private SMS_US() {

	}

	public static SMS_US getInstance() {
		return SMSobject;
	}

	public void servicesetUp() {
		try {

			RestAssured.baseURI = propertyReader.readingProperty("User-Subscription-BaseURI");
			httpRequest = RestAssured.given();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			// Log.assertLog(false, e.getMessage());
		}

	}

	public Response createUserSubscription() throws IOException {
		String subscriptionStartdate=commonHelper.getInstance().getDate();
		String CreateUserSubcription_body = "{\r\n" + 
				"\"autoRenew\": true,\r\n" + 
				"\"billingPlanId\": 1,\r\n" + 
				"\"catalogSubscriptionId\": 156,\r\n" + 
				"\"comments\": [\r\n" + 
				"{ \"comment\": \"Comments Newly\" }\r\n" + 
				"],\r\n" + 
				"\"delayedStartDate\": \"2019-11-17\",\r\n" + 
				"\"earlyAutoRenew\": true,\r\n" + 
				"\"giftInfo\": { \"birthMonth\": 1, \"birthYear\": 90, \"gender\": \"Male\", \"message\": \"Gift Message\" },\r\n" + 
				"\"orderId\": 1,\r\n" + 
				"\"packageListId\": 1,\r\n" + 
				"\"paymentMethodId\": \"online\",\r\n" + 
				"\"productId\": 1,\r\n" + 
				"\"purchaseDate\": \""+subscriptionStartdate+"\",\r\n"+
				"\"reactivationEnabled\": true,\r\n" + 
				"\"shippingAddressId\": \"shipadd\",\r\n" + 
				"\"storeId\": 123,\r\n" + 
				"\"userSubscriptionState\": \"active\",\r\n" + 
				"\"userSubscriptionStatus\": \"active\"\r\n" + 
				"}";
System.out.println("req structure : "+CreateUserSubcription_body);
		Response response = commonHelper.getInstance().doPostRequest("1/subscriptions",
				CreateUserSubcription_body, "US");
		return response;

	}
}
