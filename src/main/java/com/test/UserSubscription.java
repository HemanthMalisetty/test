package com.test;

import java.io.File;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.helper.commonHelper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserSubscription {
	RequestSpecification httpRequest;

	ExtentReports extent;
	ExtentTest logger;

	@BeforeClass
	public void report_setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/UserSubscription.html", true);
		extent.addSystemInfo("Host Name", "API Testing").addSystemInfo("Environment", "API Automation Testing")
				.addSystemInfo("User Name", "mahantesh.sapgole");
		extent.loadConfig(new File(System.getProperty("user.dir") + "/testXML/extent.xml"));

	}

	@Test(priority = 1, description = "CreateUserSubcription")
	public void CreateUserSubcription() {
		try {
			logger = extent.startTest("Test Id : US-1 TestScenario : CreateUserSubcription");
			String CreateUserSubcription_body = "{\r\n" + "  \"autoRenew\": true,\r\n" + "  \"billingPlanId\": 1,\r\n"
					+ "  \"catalogSubscriptionId\": 1,\r\n" + "  \"comments\": [\r\n" + "    {\r\n"
					+ "      \"comment\": \"US-Created\"\r\n" + "    }\r\n" + "  ],\r\n"
					+ "  \"delayedStartDate\": \"2019-06-16\",\r\n" + "  \"earlyAutoRenew\": true,\r\n"
					+ "  \"giftInfo\": {\r\n" + "    \"birthMonth\": 1,\r\n" + "    \"birthYear\": 2011,\r\n"
					+ "    \"gender\": \"Male\",\r\n" + "    \"message\": \"HappyBirthday\"\r\n" + "  },\r\n"
					+ "  \"orderId\": 1,\r\n" + "  \"packageListId\": 1,\r\n" + "  \"paymentMethodId\": \"1\",\r\n"
					+ "  \"productId\": 1,\r\n" + "  \"reactivationEnabled\": true,\r\n" + "  \"storeId\": 1,\r\n"
					+ "  \"subscriptionStartDate\": \"2019-05-19\",\r\n"
					+ "  \"userSubscriptionState\": \"active\",\r\n" + "  \"userSubscriptionStatus\": \"active\"\r\n"
					+ "}";

			Response response = commonHelper.getInstance().doPostRequest("/user/1/subscriptions",
					CreateUserSubcription_body, "US");
			int statusCode = response.getStatusCode();
			if (statusCode == 201) {
				logger.log(LogStatus.PASS, " Able to create UserSubscription and Verify Response Status code",
						"Status code is : " + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify whether the UserSubscription are getting created successfully",
						"UserSubscription are NOT getting created properly : " + statusCode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	

	@AfterTest
	public void endReport() {
		extent.endTest(logger);
		extent.flush();

	}
}
