package com.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.helper.SMS_US;
import com.helper.commonHelper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import com.utilities.DataColumnMapping;
import com.utilities.RandomNumber;
import com.utilities.excelReader;
import com.utilities.propertyReader;

public class subscriptionManagementSystem {

	propertyReader prop;
	String sheetName = "SubscriptionManagementService";
	HashMap<String, String> values = new HashMap<>();
	public static ExtentTest logger;
	public static com.relevantcodes.extentreports.ExtentReports extent;
	Integer subscriptionid;

	@BeforeClass
	public void report_setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/subscriptionManagementReport.html",
				true);
		extent.addSystemInfo("Host Name", "API Testing").addSystemInfo("Environment", "API Automation Testing")
				.addSystemInfo("User Name", "Nandhini");
		extent.loadConfig(new File(System.getProperty("user.dir") + "/testXML/extent.xml"));

	}

	@Test(priority = 1, invocationCount = 1, description = "Verify Catalog subscription has been created successfully with all the default values.", groups = {
			"smoke", "regression" }, enabled = true)
	public void verifyCreateSubscription() {
		try {
			int storeid = RandomNumber.getNumericString(1, 10);
			String sku = Integer.toString(RandomNumber.getNumericString(1, 10));
			String createSubscription_body = "{\n" + "  \"active\":" + true + "," + "\n"
					+ "  \"allowCustomSecondMonthSelection\":" + true + "," + "\n"
					+ "  \"allowCustomerFirstMonthSelection\":" + true + "," + "\n" + "  \"billingPlanIds\": [\r\n"
					+ "    1\r\n" + "  ]," + "\n" + "  \"customFirstMonthRange\": \"Some name\"," + "\n"
					+ "  \"customSecondMonthRange\": \"Some name\"," + "\n" + "  \"firstCycleDay\":" + 0 + "," + "\n"
					+ "  \"firstCycleOutDay\":" + 0 + "," + "\n" + "  \"firstExpShippingDay\":" + 0 + "," + "\n"
					+ "  \"maxPackageCount\":" + 10 + "," + "\n" + "  \"packageListId\":" + 1 + "," + "\n"
					+ "  \"regCclDay\":" + 0 + "," + "\n" + "  \"regCclOutCatchUpDay\":" + 0 + "," + "\n"
					+ "  \"regCclOutDay\":" + 0 + "," + "\n" + "  \"regExpShippingDay\": " + 0 + "," + "\n"
					+ "  \"sku\": " + sku + "," + "\n" + "  \"storeId\": " + storeid + "," + "\n"
					+ "  \"subscriptionType\": \"Regular\"\n" + "}";

			logger = extent.startTest(
					"Test ID : CS-1 TestScenario : Verify Catalog subscription has been created successfully with all the default values.");
			Response response1 = commonHelper.getInstance().doPostRequest("/catalog/subscriptions",
					createSubscription_body, "SMS");
			System.out.println(response1.getBody().asString());
			int statuscode = response1.getStatusCode();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS, " the subscription is created with store id " + storeid,
						"Full response : " + response1.asString());
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" the subscription is NOT created with store id " + response1.asString() + " for reference");
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 2, description = "Verify Catalog subscription has not been created without providing SKU value in the request.", groups = {
			"smoke", "regression" }, enabled = true)
	public void verifyCreateSubscriptionwithoutSKU() {
		try {
			int storeid = +RandomNumber.getNumericString(5, 15);
			String createSubscription_body = "{\n" + "  \"active\":" + true + "," + "\n"
					+ "  \"allowCustomSecondMonthSelection\":" + true + "," + "\n"
					+ "  \"allowCustomerFirstMonthSelection\":" + true + "," + "\n" + "  \"billingPlanIds\": [\r\n"
					+ "    1\r\n" + "  ]," + "\n" + "  \"customFirstMonthRange\": \"Some name\"," + "\n"
					+ "  \"customSecondMonthRange\": \"Some name\"," + "\n" + "  \"firstCycleDay\":" + 0 + "," + "\n"
					+ "  \"firstCycleOutDay\":" + 0 + "," + "\n" + "  \"firstExpShippingDay\":" + 0 + "," + "\n"
					+ "  \"maxPackageCount\":" + 10 + "," + "\n" + "  \"packageListId\":" + 1 + "," + "\n"
					+ "  \"regCclDay\":" + 0 + "," + "\n" + "  \"regCclOutCatchUpDay\":" + 0 + "," + "\n"
					+ "  \"regCclOutDay\":" + 0 + "," + "\n" + "  \"regExpShippingDay\": " + 0 + "," + "\n"
					+ "  \"storeId\": " + storeid + "," + "\n" + "  \"subscriptionType\": \"Regular\"\n" + "}";

			logger = extent.startTest(
					"Test ID : CS-2 TestScenario : Verify Catalog subscription has not been created without providing SKU value in the request.");

			System.out.println(createSubscription_body);
			Response response1 = commonHelper.getInstance().doPostRequest("/catalog/subscriptions",
					createSubscription_body, "SMS");
			System.out.println(response1.getBody().asString());
			int statuscode = response1.getStatusCode();
			if (statuscode == 400) {
				logger.log(LogStatus.PASS,
						" the subscription is NOT created with store id when SKU is empty " + storeid,
						"Status code is : " + statuscode + "Full response : " + response1.asString());
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 3, description = "Verify Catalog subscription details retrieved successfully by sending the request by catalog subscription ID.", groups = {
			"smoke", "regression" }, enabled = true)
	public void getCatlogSubscriptionDetails() {
		try {
			logger = extent.startTest(
					"Test Id : CS-3 TestScenario : Verify Catalog subscription details retrieved successfully by sending the request by catalog subscription ID.");
			values = excelReader.readExcel(sheetName, "getCatlogSubscriptionDetails");
			String catlogsubscid = values.get(DataColumnMapping.CATLOGSUBSCRIPTIONID);
			Response response1 = commonHelper.getInstance().doGetRequest("/catalog/subscriptions/" + catlogsubscid,"SMS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 200) {
				logger.log(LogStatus.PASS,
						" Verify whether the Catlog Subscription Details Are getting displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details displayed properly with Status code is : " + statuscode
								+ "response : " + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" Verify whether the Catlog Subscription Details Are getting displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details is NOT  displayed properly , response for ref : "
								+ responseString);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 4, description = "Verify Catalogsubscription details can be updated by providing catalog subscription ID in the request.", groups = {
			"smoke", "regression" }, enabled = true)
	public void updateCatlogSubscriptionDetails() {
		try {
			logger = extent.startTest(
					"Test Id : CS-4 TestScenario : Verify CatalogSubscription details can be updated by providing catalog subscription ID in the request");
			int storeid = RandomNumber.getNumericString(1, 2);
			String sku = Integer.toString(RandomNumber.getNumericString(10, 100));
		
			String createSubscription_body = "{\n" + "  \"active\":" + true + "," + "\n"
					+ "  \"allowCustomSecondMonthSelection\":" + true + "," + "\n"
					+ "  \"allowCustomerFirstMonthSelection\":" + true + "," + "\n" + "  \"billingPlanIds\": [\r\n"
					+ "    1\r\n" + "  ]," + "\n" + "  \"customFirstMonthRange\": \"Some name\"," + "\n"
					+ "  \"customSecondMonthRange\": \"Some name\"," + "\n" + "  \"firstCycleDay\":" + 0 + "," + "\n"
					+ "  \"firstCycleOutDay\":" + 0 + "," + "\n" + "  \"firstExpShippingDay\":" + 0 + "," + "\n"
					+ "  \"maxPackageCount\":" + 10 + "," + "\n" + "  \"packageListId\":" + 1 + "," + "\n"
					+ "  \"regCclDay\":" + 0 + "," + "\n" + "  \"regCclOutCatchUpDay\":" + 0 + "," + "\n"
					+ "  \"regCclOutDay\":" + 0 + "," + "\n" + "  \"regExpShippingDay\": " + 0 + "," + "\n"
					+ "  \"sku\": " + sku + "," + "\n" + "  \"storeId\": 1"  + "," + "\n"
					+ "  \"subscriptionType\": \"Regular\"\n" + "}";
			System.out.println("creating sms request : "+createSubscription_body);
			Response response1 = commonHelper.getInstance().doPostRequest("/catalog/subscriptions",
					createSubscription_body, "SMS");
			int statuscode = response1.getStatusCode();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS, " the subscription is created with store id " + storeid,
						"Full response : " + response1.asString());
				JsonPath ps_jsonPathEvaluator = response1.jsonPath();
				// ArrayList
				// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
				String catlogsubscid = Integer.toString(ps_jsonPathEvaluator.get("id"));
			Response get_response = commonHelper.getInstance().doGetRequest("/catalog/subscriptions/" + catlogsubscid,
					"SMS");
			String request = get_response.body().asString().replace("\"billingPlanIds\":[1]", "\"billingPlanIds\":[1,2]");
			System.out.println("update Request :"+request);
			Response update_response = commonHelper.getInstance().doPutRequest("/catalog/subscriptions/" + catlogsubscid,
					request, "SMS");
			int update_statuscode = update_response.getStatusCode();
			String responseString = update_response.getBody().asString();
			if (update_statuscode == 200) {
				logger.log(LogStatus.PASS,
						" Verify whether the Catlog Subscription Details Are updating and  displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details displayed properly with Status code is : " + update_statuscode
								+ " response : " + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + update_statuscode);
				logger.log(LogStatus.FAIL,
						" Verify whether the Catlog Subscription Details Are updating and  displayed properly for id "
								+ catlogsubscid,
						"Catlog Subscription Details is not updated and  displayed properly with response : "
								+ responseString);
			}
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" the subscription is NOT created with store id " + response1.asString() + " for reference");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 5, description = "CreatePackageListAndVerifyUniqueIdentifier", enabled = true)
	public void CreatePackageListAndVerifyUniqueIdentifier() {
		try {
			logger = extent.startTest(
					"Test ID : PL-1 TestScenario : Verify able to create package_list and identifier is unique .");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			String packageList_body = "{\r\n" + "\"active\": true,\r\n" + "\"identifier\": \"" + identifierRandom
					+ "\",\r\n" + "\"packageListName\": \"march07_1\",\r\n"
					+ "\"packageListType\": \"NON_MAGAZINE\"\r\n" + "}";
			Response response = commonHelper.getInstance().doPostRequest("/catalog/package-lists", packageList_body,
					"PL");
			int statusCode = response.getStatusCode();
			if (statusCode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify whether the packages are getting created successfully",
						"packages are NOT getting created properly : " + statusCode);
			}
			Response response1 = commonHelper.getInstance().doPostRequest("/catalog/package-lists", packageList_body,
					"PL");
			int statusCode1 = response1.getStatusCode();
			if (statusCode1 == 400) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode1 + " Verified Identifier is unique");
			} else {
				logger.log(LogStatus.FAIL, " Verify if the packages are getting created successfully" + statusCode1
						+ " Verified Identifier is not unique");
			}

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 6, description = "VerifyTypeMagazineAndNonMagazine", enabled = true)
	public void VerifyTypeMagazineAndNonMagazine() {
		try {
			logger = extent.startTest(
					"Test Id : PL-2 TestScenario : Verify Type accepts only Magazine And NonMagazine URI :/catalog/package-lists");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			String request = "{\r\n" + "\"active\": true,\r\n" + "\"identifier\": \"" + identifierRandom + "\",\r\n"
					+ "\"packageListName\": \"march07_1\",\r\n" + "\"packageListType\": \"Basic\"\r\n" + "}";
			Response response = commonHelper.getInstance().doPostRequest("/catalog/package-lists", request, "PL");
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, 400);
			System.out.println("Verified Package list Type and which allows only 'NON_MAGAZINE, MAGAZINE'");

			if (statusCode == 400) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statusCode
						+ " Verified Package list TYPE and which allows only 'NON_MAGAZINE, MAGAZINE'");
			} else {
				logger.log(LogStatus.FAIL, " Verify if the packages are getting created successfully" + statusCode
						+ " Verified Package list TYPE accepts everything including 'NON_MAGAZINE, MAGAZINE'");
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}

	}

	@Test(priority = 7, description = "CreateUserSubcription", enabled = true)
	public void CreateUserSubcription() {
		try {
			logger = extent.startTest("Test Id : US-1 TestScenario : CreateUserSubcription");
			String subscriptionStartdate=commonHelper.getInstance().getDate();
			String CreateUserSubcription_body = "{\r\n" + 
					"\"autoRenew\": true,\r\n" + 
					"\"billingPlanId\": 1,\r\n" + 
					"\"catalogSubscriptionId\": 1,\r\n" + 
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
			System.out.println("catlog body :" + CreateUserSubcription_body);
			Response response = commonHelper.getInstance().doPostRequest("1/subscriptions", CreateUserSubcription_body,
					"US");
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

	@Test(priority = 8, description = "Retrieve Package List with id", enabled = true)
	public void VerifygetPackageList() {
		try {
			logger = extent.startTest("Test Id : PL-3 TestScenario : Verify Get Package List");
			values = excelReader.readExcel(sheetName, "VerifygetPackageList");
			String packageListId = values.get(DataColumnMapping.PACKAGELISTID);
			Response response = commonHelper.getInstance().doGetRequest("/catalog/package-lists/" + packageListId,
					"PL");
			String responseBody = response.getBody().asString();
			int statusCode = response.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  get package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "   get package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}

	}

	@Test(priority = 9, description = "Update Package List with id", enabled = true)
	public void VerifyUpdatePackageList() {
		try {
			logger = extent.startTest("Test Id : PL-4 TestScenario : Verify Update Package List with id");
			values = excelReader.readExcel(sheetName, "VerifyUpdatePackageList");
			String packageListId = values.get(DataColumnMapping.PACKAGELISTID);
			String packageShipDate = commonHelper.getInstance().postDate(3);
			String dontShipBeforeDate = commonHelper.getInstance().getDate();
			String id = Integer.toString(RandomNumber.getNumericString(1, 5));
			String Request = "{\r\n" + 
					"   \"packageListName\": \"April_Recent\",\r\n" + 
					"    \"identifier\": \"SKU_April_17\",\r\n" + 
					"    \"packageListType\": \"NON_MAGAZINE\",\r\n" + 
					"    \"createdAt\": \"2019-04-17T11:38:30.133Z\",\r\n" + 
					"    \"updatedAt\": \"2019-05-02T12:07:47.522Z\",\r\n" + 
					"    \"noOfPackages\": 3,\r\n" + 
					"    \"active\": true,\r\n" + 
					"    \"packageMappingsList\": [\r\n" + 
					"        {\r\n" + 
					"        	\"id\": 15,\r\n" + 
					"            \"packageMappingName\": \"April17_4\",\r\n" + 
					"            \"packageSkuId\": \"April17_pk4\",\r\n" + 
					"            \"sortOrder\": 0,\r\n" + 
					"            \"dontShipBeforeDate\": \""+dontShipBeforeDate+"\",\r\n" + 
					"            \r\n" + 
					"            \"isFirstPackage\": false\r\n" + 
					"        }\r\n" + 
					"    ]\r\n" + 
					"}";
			System.out.println(Request);
			Response response = commonHelper.getInstance().doPutRequest("/catalog/package-lists/" + packageListId,
					Request, "PL");
			String responseBody = response.getBody().asString();
			int statusCode = response.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update package list response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Package List Service Working ",
					"Package List service failed and its Exception stach trace : " + e.getMessage());
		}

	}

	@Test(priority = 10, description = "Get User Subscription", enabled = true)
	public void getUserSubcription() {
		try {
			logger = extent.startTest("Test Id : US-2 TestScenario : GetUserSubcription");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			String subscriptionid = Integer.toString(ps_jsonPathEvaluator.get("id"));
			String userid = Integer.toString(ps_jsonPathEvaluator.get("userId"));
			Response response1 = commonHelper.getInstance().doGetRequest(userid+"/subscriptions/" + subscriptionid, "US");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			System.out.println("Status code  :" + statuscode + " response :" + response1);
			if (statuscode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS,
						" Verify whether the Subscription Details Are getting displayed properly for id "
								+ subscriptionid,
						"Subscription Details displayed properly with response : " + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" Verify whether the Subscription Details Are getting displayed properly for id "
								+ subscriptionid,
						"Subscription Details is NOT  displayed properly , response for ref : " + responseString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 11, description = "Update User Subscription", enabled = true)
	public void updateUserSubcription() {
		try {
			logger = extent.startTest("Test Id : US-3 TestScenario : UpdateUserSubcription");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			System.out.println("user id :"+userid+" id :"+subscriptionid);
			ArrayList comment_info=ps_jsonPathEvaluator.get("comments.id");
			String commentsid=comment_info.get(0).toString();
			//ArrayList gift_temp=ps_jsonPathEvaluator.get("giftInfo.id");
			String giftinfo_id=Integer.toString(ps_jsonPathEvaluator.get("giftInfo.id"));
			String delayedstartdate=ps_jsonPathEvaluator.get("delayedStartDate");
			String subscriptionStartdate=commonHelper.getInstance().getDate();
			String Request="{\r\n" + 
					"  \"autoRenew\": true,\r\n" + 
					"  \"billingPlanId\": 1,\r\n" + 
					"  \"catalogSubscriptionId\": 1,\r\n" + 
					"  \"comments\": [\r\n" + 
					"    {\r\n" + 
					"       \"comment\": \"US-Created\",\r\n" + 
					"      \"id\":"+commentsid+"\r\n" + 
					"    }\r\n" + 
					"  ],\r\n" + 
					"  \"delayedStartDate\": \""+delayedstartdate+"\",\r\n" + 
					"  \"earlyAutoRenew\": true,\r\n" + 
					"  \"giftInfo\": {\r\n" + 
					"    \"birthMonth\": 1,\r\n" + 
					"    \"birthYear\": 2011,\r\n" + 
					"    \"gender\": \"Male\",\r\n" + 
					"    \"id\": "+giftinfo_id+",\r\n" + 
					"    \"message\": \"HappyBirthday\"\r\n" + 
					"  },\r\n" + 
					"  \"orderId\": 1,\r\n" + 
					"  \"packageListId\": 1,\r\n" + 
					" \"purchaseDate\": \""+subscriptionStartdate+"\",\r\n"+
					"  \"paymentMethodId\": \"1\",\r\n" + 
					"  \"productId\": 1,\r\n" + 
					"  \"reactivationEnabled\": false,\r\n" + 
					"  \"shippingAddressId\": \"1\",\r\n" + 
					"  \"storeId\": 1,\r\n" + 
					"  \"userSubscriptionState\": \"active\",\r\n" + 
					"  \"userSubscriptionStatus\": \"active\"\r\n" + 
					"}";
			System.out.println("update req :"+Request);
			Response response1 = commonHelper.getInstance().doPutRequest(userid+"/subscriptions/"+subscriptionid,Request, "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update user Subscription  response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update user Subscription response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 12, description = "Validate Immediate Billing Plan change functionality", enabled = true)
	public void patchimmediateBillingPlanChange() {
		try {
			logger = extent.startTest("Test Id : US-4 TestScenario : Validate Immediate Billing Plan change functionality");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			values = excelReader.readExcel(sheetName, "patchimmediateBillingPlanChange");
			String billingPlanId = values.get(DataColumnMapping.BILLPLANID);
			
			String Request="{\r\n" + 
					"  \"billingPlanId\": "+billingPlanId+",\r\n" + 
					"  \"isProrated\": true,\r\n" + 
					"  \"stack\": true\r\n" + 
					"}";
			System.out.println("update req :"+Request);
			Response response1 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/billing-plan",Request, "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update Billing plan id user Subscription  response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
				
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  Update billing plan id user Subscription response for the package list id "
								+ responseBody );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 13, description = "Validate cancel User Subscription ", enabled = true)
	public void cancelUserSubscription() {
		try {
			logger = extent.startTest("Test Id : US-5 TestScenario : Validate cancel User Subscription ");
			Response res = SMS_US.getInstance().createUserSubscription();
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			values = excelReader.readExcel(sheetName, "patchimmediateBillingPlanChange");
			Response response1 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/cancel","", "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code",
						"Status code is : " + statusCode + "  cancel   user Subscription  response for the user id "+userid+" and response "
								+ responseBody + " status code :" + statusCode);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode + "  cancel  user Subscription response for the package list id "
								+ responseBody + " status code :" + statusCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	
	@Test(priority = 14, description = "Validate Renewing User Subscription", enabled = true)
	public void renewUserSubscription(ITestContext tc) {
		try {
			logger = extent.startTest("Test Id : US-6 TestScenario : Validate Renewing User Subscription");
			String subscriptionStartdate=commonHelper.getInstance().getDate();
			String CreateUserSubcription_body ="{\r\n" + 
					"\"autoRenew\": true,\r\n" + 
					"\"billingPlanId\": 2,\r\n" + 
					"\"catalogSubscriptionId\": 156,\r\n" + 
					"\"comments\": [\r\n" + 
					"{ \"comment\": \"Comments Newly\" }\r\n" + 
					"],\r\n" + 
					"\"delayedStartDate\": \"2019-11-17\",\r\n" + 
					"\"earlyAutoRenew\": false,\r\n" + 
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
			Response res = commonHelper.getInstance().doPostRequest("30/subscriptions", CreateUserSubcription_body, "US");
			System.out.println("user sub res : " + res.body().asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			Integer userid = ps_jsonPathEvaluator.get("userId");
			Response response1 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/renew","", "US");
			String responseBody = response1.getBody().asString();
			int statusCode = response1.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code after Renewing",
						"Status code is : " + statusCode + "Renew user Subscription  response for the user id "+userid+"is successful" );
			} else {
				tc.setAttribute("status", "fail");
				logger.log(LogStatus.FAIL, " Verify Response Status code after renewing",
						"Status code is : " + statusCode + "Renew user Subscription response for the package list id  is failed and response details for ref"
								+ responseBody );
			}
			System.out.println(subscriptionid+" "+userid);
			Response response2 = commonHelper.getInstance().doPatchRequest(userid+"/subscriptions/"+subscriptionid+"/summary","", "US");
			int statusCode_summary = response2.getStatusCode();
			if (statusCode_summary == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code of the summary",
						"Status code is : " + statusCode_summary + "User Subscription summary response for the user id "+userid);
				JsonPath summary_jsonPathEvaluator = response2.jsonPath();
				String nextRenewalDate = summary_jsonPathEvaluator.get("nextRenewalDate");
				if(nextRenewalDate!=null) {
				logger.log(LogStatus.PASS, " Verify the presence of next renewal date",
						"Next Renewal date is present : "+nextRenewalDate+" Renewal is successful");
				}
				else {
					tc.setAttribute("status", "fail");
					logger.log(LogStatus.FAIL, " Verify the presence of next renewal date",
							"Next Renewal date is NOT present and  Renewal is NOT successful");
				}
				
			} else {
				tc.setAttribute("status", "fail");
				logger.log(LogStatus.FAIL, " Verify Response Status code",
						"Status code is : " + statusCode_summary + "User Subscription Summary response for the package list id "
								+ response2.body().asString());
			}
		} catch (Exception e) {
			tc.setAttribute("status", "fail");
			logger.log(LogStatus.FAIL, " Verify Whether the SMS Service Working ",
					"SMS service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	
	/***
	 * Test method to CREATE a Catalog Subscription
	 * 
	 * @author Shubha.Ravikumar
	 */
	
	@Test(priority = 15, description = "Create a Catalog Subscription", enabled = true)
	public void AddCatalogSubscription(ITestContext tc) {
		try {
			logger = extent.startTest("Test Id : NEX-1716, TestScenario : [SMS-API] Create a Catalog Subscription");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			
			String CreateUserSubcription_body ="{\r\n" + 
					"\"active\": true,\r\n" + 
					"\"allowCustomSecondMonthSelection\": true,\r\n" +
					"\"allowCustomerFirstMonthSelection\": true,\r\n" +
					"\"billingPlanIds\": [ 1,2 ],\r\n" + 	
					
					"\"customFirstMonthRange\": \"string\",\r\n" +
					"\"customSecondMonthRange\": \"string\",\r\n" +

					"\"maxPackageCount\": \"0\",\r\n" +
					"\"packageListId\": \"0\",\r\n" +
					"\"sku\": \"SSU" +identifierRandom+"\" ,\r\n" +
					
					"\"storeId\": 1,\r\n" + 
					"\"subscriptionType\": \"Regular\"\r\n" + 
					"}";
			Response res = commonHelper.getInstance().doPostRequest("/catalog/subscriptions", CreateUserSubcription_body, "SMS");
			System.out.println("Create a Catalog Subscription Response: " + res.asString());

			JsonPath ps_jsonPathEvaluator = res.jsonPath();			
			subscriptionid = ps_jsonPathEvaluator.get("id");
			
			int statusCode = res.getStatusCode();

			if (statusCode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code after Add Subscription",
						"Status code is : " + statusCode + "Create Catalog Subscription response for the id "+subscriptionid+" is successful" );
			} else {
				tc.setAttribute("status", "fail");
				logger.log(LogStatus.FAIL, " Verify Response Status code after Add Subscription",
						"Status code is : " + statusCode + "Create Catalog Subscription is failed and response details for ref"
								+ res.asString() );
			}
			
			
		} catch (Exception e) {
			tc.setAttribute("status", "fail");
			logger.log(LogStatus.FAIL, " Verify Create a Catalog Subscription",
					"Create a Catalog Subscription failed and its Exception stach trace : " + e.getMessage());
		}
	}
	
	/***
	 * Test method to UPDATE Catalog Subscription
	 * 
	 * @author Shubha.Ravikumar
	 */
	
	@Test(priority = 16, description = "Update a Catalog Subscription", enabled = true)
	public void UpdateCatalogSubscription(ITestContext tc) {
		try {
			logger = extent.startTest("Test Id : NEX-1717, TestScenario : [SMS-API] Update a Catalog Subscription");
			int identifierRandom = RandomNumber.getNumericString(1, 10000);
			
			String UpdateUserSubcription_body ="{\r\n" + 
					"\"active\": true,\r\n" + 
					"\"allowCustomSecondMonthSelection\": true,\r\n" +
					"\"allowCustomerFirstMonthSelection\": true,\r\n" +
					"\"billingPlanIds\": [ 1,2 ],\r\n" + 	
					
					"\"customFirstMonthRange\": \"string\",\r\n" +
					"\"customSecondMonthRange\": \"string\",\r\n" +

					"\"maxPackageCount\": \"0\",\r\n" +
					"\"packageListId\": \"0\",\r\n" +
					"\"sku\": \"SSU" +identifierRandom+"\" ,\r\n" +
					
					"\"storeId\": 1,\r\n" + 
					"\"subscriptionType\": \"Regular\"\r\n" + 
					"}";
			Response res = commonHelper.getInstance().doPutRequest("/catalog/subscriptions/"+subscriptionid, UpdateUserSubcription_body, "SMS");
			System.out.println("Update Catalog Subscription Response: " + res.asString());

			int statusCode = res.getStatusCode();

			if (statusCode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code after Update Subscription",
						"Status code is : " + statusCode + "Update Catalog Subscription response for the id "+subscriptionid+" is successful" );
			} else {
				tc.setAttribute("status", "fail");
				logger.log(LogStatus.FAIL, " Verify Response Status code after Update Subscription",
						"Status code is : " + statusCode + "Update Catalog Subscription is failed and response details for ref"
								+ res.asString() );
			}
			
			
		} catch (Exception e) {
			tc.setAttribute("status", "fail");
			logger.log(LogStatus.FAIL, " Verify Update Catalog Subscription",
					"Update Catalog Subscription failed and its Exception stach trace : " + e.getMessage());
		}
	}
	
	public static ExtentReports getextent() {
		return extent;
	}
	@AfterTest
	public void endReport() {
		
		extent.endTest(logger);
		extent.flush();
		
	}

	

}
