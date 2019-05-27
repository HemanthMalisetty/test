package com.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.helper.SMS_US;
import com.helper.commonHelper;
import com.helper.paymentService;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.utilities.DataColumnMapping;
import com.utilities.RandomNumber;
import com.utilities.excelReader;
import com.utilities.propertyReader;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PaymentService {
	propertyReader prop;

	String sheetName = "PaymentService";
	HashMap<String, String> values = new HashMap<>();
	public static ExtentTest logger;
	public static com.relevantcodes.extentreports.ExtentReports extent;

	@BeforeClass
	public void report_setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/PaymentServiceReport.html", true);
		extent.addSystemInfo("Host Name", "API Testing").addSystemInfo("Environment", "API Automation Testing")
				.addSystemInfo("User Name", "Nandhini");
		extent.loadConfig(new File(System.getProperty("user.dir") + "/testXML/extent.xml"));

	}

	@Test(priority = 1, description = "verifythepaymentservice", groups = { "smoke", "regression" })
	public void verifythepaymentservice() {
		try {
			logger = extent.startTest(
					"Test ID : PS-1 TestScenario : Verify the payment service is able to fetch the list of billing plans from Braintree.");
			Response response1 = commonHelper.getInstance().doGetRequest("/billing-plans", "PS");
			int statuscode = response1.getStatusCode();
			if (statuscode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);

			} else {
				logger.log(LogStatus.FAIL, " Verify whether the paymentservice is working",
						"payment service failed   with Status code :" + statuscode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 2, description = "verifyNoofPlansreceived", groups = { "smoke", "regression" })
	public void verifyNoofPlansreceived() {
		try {
			logger = extent.startTest(
					"Test ID : PS-2 TestScenario : Verify the number of billing plans displayed in Payment service API response is equal to the number of billing plans available in Braintree.");
			values = excelReader.readExcel(sheetName, "verifyNoofPlansreceived");
			int noofplans_braintree = Integer.parseInt(values.get(DataColumnMapping.NOOFPLANSINBRAINTREE));
			Response response1 = commonHelper.getInstance().doGetRequest("/billing-plans", "PS");
			int statuscode = response1.getStatusCode();
			int noofplans = response1.jsonPath().getList("active").size();
			values = excelReader.readExcel(sheetName, "verifythepaymentservice");
			Assert.assertNotSame(noofplans == noofplans_braintree,
					"Plans In the Braintree is  equal to the plan we received and the number is " + noofplans,
					"Plans In the Braintree is  NOT equal to the plan we received and the number is " + noofplans);
			if (noofplans == noofplans_braintree) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS,
						" Verify whether the No of Plans is brain tree is equal to No of plans in response is same",
						"No of plans in response and in Brain Tree  is same and the no of plan count is : "
								+ noofplans);
			} else {
				logger.log(LogStatus.FAIL,
						" Verify whether the No of Plans is brain tree is equal to No of plans in response is same",
						"No of plans in response and in Braintree is NOT same and the no of plan count in  Response is : "
								+ noofplans + "and no of plan in brain tree is " + noofplans_braintree
								+ " with Status code :" + statuscode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 3, description = "verifyBillplansContainsAddons", groups = { "smoke", "regression" })
	public void verifyBillplansContainsAddons() {
		try {
			logger = extent.startTest(
					"Test ID :PS-3 TestScenario : Verify Billing plans contains Add-ons and Discounts details for available plans.");
			values = excelReader.readExcel(sheetName, "verifyBillplansContainsAddons");
			String BillId = values.get(DataColumnMapping.PAYMENTPROVIDERID);
			Response response1 = commonHelper.getInstance().doGetRequest("/billing-plans/" + BillId, "PS");
			int statuscode = response1.getStatusCode();
			if (response1.jsonPath().getList("addOns") != null) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS, " Verify whether add - ons are added to the billing provider id " + BillId,
						"Add On's are displayed successfully and the details of add-ons are "
								+ response1.jsonPath().getList("addOns"));
			} else {
				logger.log(LogStatus.FAIL, " Verify whether add - ons are added to the billing provider id " + BillId,
						"Add On's are NOT displayed for the billing provider id " + BillId
								+ " and status code for this request is " + statuscode);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 4, description = "Verify respective billing details will be displayed in the response by passing the ID thru API call.", groups = {
			"smoke", "regression" })
	public void verifythepaymentserviceforthebillpayid() {
		try {
			logger = extent.startTest(
					"Test ID:PS-4 TestScenario : Verify respective billing details will be displayed in the response by passing the ID thru API call. ");
			values = excelReader.readExcel(sheetName, "verifythepaymentserviceforthebillpayid");
			String planid = values.get(DataColumnMapping.PAYMENTPROVIDERID);
			Response response1 = commonHelper.getInstance().doGetRequest("/billing-plans/" + planid, "PS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			System.out.println("Status code  :" + statuscode + " response :" + response1);
			if (statuscode == 200) {
				logger.log(LogStatus.PASS, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.PASS,
						" Verify whether the Payment Details Are getting displayed properly for id " + planid,
						"Payment Details displayed properly with response : " + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code", "Status code is : " + statuscode);
				logger.log(LogStatus.FAIL,
						" Verify whether the Payment Details Are getting displayed properly for id " + planid,
						"Payment Details is NOT  displayed properly , response for ref : " + responseString);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 5, description = "Verify response of the payment service by providing the invalid billing plan id to fetch the billing details.", groups = {
			"smoke", "regression" })
	public void verifythepaymentservicefortheInvalidbillpayid() {
		try {
			logger = extent.startTest(
					"Test ID : PS-5 TestScenario : Verify response of the payment service by providing the invalid billing plan id to fetch the billing details.");
			values = excelReader.readExcel(sheetName, "verifythepaymentservicefortheInvalidbillpayid");
			String planid = values.get(DataColumnMapping.PAYMENTPROVIDERID);
			Response response1 = commonHelper.getInstance().doGetRequest("/billing-plans" + planid, "PS");
			int statuscode = response1.getStatusCode();
			if (statuscode == 404) {
				logger.log(LogStatus.PASS, " Verify Response Status code for Invalid BillPlan ID",
						"Status code is : " + statuscode + " is returned from service end as expected");
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code for Invalid BillPlan ID",
						"Status code is : " + statuscode + " is returned from service end is NOT as expected");

			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 6, description = "Verify create Sale Transaction", groups = { "smoke", "regression" })
	public void createsaleTransaction_OldCustomer_NewCreaditcardRegistration() {
		try {
			logger = extent.startTest(
					"Test ID :PS-6 TestScenario : Verify create Sale Transaction for the old customer with new cc");
			values = excelReader.readExcel(sheetName, "createsaleTransaction_OldCustomer_NewCreaditcardRegistration");
			String Bpid = values.get(DataColumnMapping.BILLPLANID);
			String paymentmethodnonce = values.get(DataColumnMapping.PAYMENTMETHODNONCE);
			String countrycodealpha2 = values.get(DataColumnMapping.COUNRYCODEALPHA2);
			String customerId = values.get(DataColumnMapping.CUSTOMERID);
			String orderId = values.get(DataColumnMapping.ORDERID);
			String userSubscriptionId = values.get(DataColumnMapping.USERSUBSCRIPTIONID);
			String Request = "{\r\n" + " \"amount\": 100,\r\n" + " \"billingPlanId\": " + Bpid + ",\r\n"
					+ " \"creditCard\": {\r\n" + "   \"company\": \"test\",\r\n" + "   \"countryCodeAlpha2\":\""
					+ countrycodealpha2 + "\",\r\n" + "   \"default\": true,\r\n"
					+ "   \"firstName\": \"naramarch\",\r\n" + "   \"lastName\": \"25march\",\r\n"
					+ "   \"locality\": \"string\",\r\n" + "   \"paymentMethodNonce\": \"" + paymentmethodnonce
					+ "\",\r\n" + "   \"postalCode\": \"string\",\r\n" + "   \"region\": \"string\",\r\n"
					+ "   \"street1\": \"string\",\r\n" + "   \"street2\": \"string\"\r\n" + " },\r\n"
					+ " \"currencyIsoCode\": \"USD\",\r\n" + "\r\n" + "\"customerId\": \"" + customerId + "\",\r\n"
					+ " \"orderId\":\"" + orderId + "\",\r\n" + " \"userSubscriptionIds\": [\r\n" + userSubscriptionId
					+ " ]\r\n," + "\"transactionSource\":\"CSR\"}";
			System.out.println("Request :" + Request);
			Response response1 = commonHelper.getInstance().doPostRequest("/transactions", Request, "PS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code ", "Status code is : " + statuscode
						+ " is returned from service end as expected" + "response :" + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code ",
						"Status code is : " + statuscode + " is returned from service end is NOT as expected");

			}
			JsonPath jsonPathEvaluator = response1.jsonPath();
			String orderid_response = jsonPathEvaluator.get("orderId");

			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 7, description = "Update Billing plan Request", groups = { "smoke", "regression" })
	public void updateBPRequest() {
		try {
			logger = extent.startTest("Test ID :PS-7 TestScenario : Verify whether Billing plan Request is updating");
			values = excelReader.readExcel(sheetName, "verifyBillplansContainsAddons");
			String Request = "{\r\n" + "  \"active\": true\r\n" + "}";
			String BillId = values.get(DataColumnMapping.PAYMENTPROVIDERID);
			Response response1 = commonHelper.getInstance().doPutRequest("/billing-plans/" + BillId, Request, "PS");
			int statuscode = response1.getStatusCode();
			if (statuscode == 200) {

				logger.log(LogStatus.PASS, " Verify whether updating Billing Plan for " + BillId + "is working",
						"Billing plan updation is working as expected  ref : status code :  " + statuscode
								+ " response details : " + response1.getBody().asString());
			} else {
				logger.log(LogStatus.FAIL, " Verify whether updating Billing Plan for " + BillId + "is working",
						"Billing plan updation is NOT working as expected  ref : status code :  " + statuscode
								+ " response details : " + response1.getBody().asString());
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 8, description = "Verify create Sale Transaction with New customer and new Credit card", groups = {
			"smoke", "regression" })
	public void createsaleTransaction_NewCustomer_NewCreaditcardRegistration() {
		try {
			logger = extent.startTest(
					"Test ID :PS-8 TestScenario : Verify create Sale Transaction with New customer and new Credit card");
			values = excelReader.readExcel(sheetName, "createsaleTransaction");
			String firstName = RandomNumber.getalphabets();
			String paymentmethodnonce = values.get(DataColumnMapping.PAYMENTMETHODNONCE);
			String customerFirstName = RandomNumber.getalphabets();
			String customerLastName = RandomNumber.getalphabets();
			String countrycode = values.get(DataColumnMapping.COUNRYCODEALPHA2);
			String email = RandomNumber.getalphabets() + "@email.com";
			String orderId = values.get(DataColumnMapping.ORDERID);
			String Request = "{\r\n" + "  \"amount\": 100,\r\n" + "\r\n" + "  \"creditCard\": {\r\n"
					+ "    \"company\": \"compnara29\",\r\n" + "    \"countryCodeAlpha2\": \"" + countrycode + "\",\r\n"
					+ "    \"default\": true,\r\n" + "    \"firstName\": \"" + firstName + "\",\r\n"
					+ "    \"lastName\": \"nara29\",\r\n" + "    \"locality\": \"string\",\r\n"
					+ "    \"paymentMethodNonce\": \"" + paymentmethodnonce + "\",\r\n"
					+ "    \"postalCode\": \"string\",\r\n" + "    \"region\": \"string\",\r\n"
					+ "    \"street1\": \"string\",\r\n" + "    \"street2\": \"string\"\r\n" + "  },\r\n"
					+ "  \"currencyIsoCode\": \"USD\",\r\n" + "  \"customer\": {\r\n" + "    \"email\": \"" + email
					+ "\",\r\n" + "    \"firstName\": \"" + customerFirstName + "\",\r\n" + "    \"lastName\": \""
					+ customerLastName + "\"\r\n" + "  },\r\n" + " \"orderId\":\"" + orderId +"\" ," + "\"transactionSource\":\"CSR\"}";
			Response response1 = commonHelper.getInstance().doPostRequest("/transactions", Request, "PS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code ", "Status code is : " + statuscode
						+ " is returned from service end as expected" + "response :" + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code ",
						"Status code is : " + statuscode + " is returned from service end is NOT as expected");

			}
			JsonPath jsonPathEvaluator = response1.jsonPath();

			String orderid_response = jsonPathEvaluator.get("orderId");
			
			String firstName_response = jsonPathEvaluator.get("customer.firstName");
			String lastName_response = jsonPathEvaluator.get("customer.lastName");
			String amount_response = Integer.toString(Math.round(jsonPathEvaluator.getFloat("amount")));
			String emailid_response = jsonPathEvaluator.get("customer.email");
			String currencycode_response = jsonPathEvaluator.get("currencyIsoCode");

			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}
			if (firstName_response != null && firstName_response.equals(customerFirstName)) {
				logger.log(LogStatus.PASS, " Verify FirstName value in response",
						"First Name displayed in response is same we sent in request ,ref First Name: "
								+ firstName_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify First Name value in response",
						"First Name Is Not  displayed in response is same we sent in request ,ref First Name: "
								+ firstName_response);
			}
			if (lastName_response != null && lastName_response.equals(customerLastName)) {
				logger.log(LogStatus.PASS, " Verify Last Name value in response",
						"Last Name displayed in response is same we sent in request ,ref Last Name: "
								+ lastName_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Last Name value in response",
						"Last Name Is Not  displayed in response is same we sent in request ,ref Last Name: "
								+ lastName_response);
			}
			if (amount_response != null && amount_response.equals("100")) {
				logger.log(LogStatus.PASS, " Verify Amount value in response",
						"Amount is displayed in response is same we sent in request ,ref Amount: " + amount_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Amount value in response",
						"Amount Is Not  displayed in response is same we sent in request ,ref Amounts: "
								+ amount_response);
			}
			if (emailid_response != null && emailid_response.equals(email)) {
				logger.log(LogStatus.PASS, " Verify Email ID value in response",
						"Email id displayed in response is same we sent in request ,ref email Id: " + emailid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Email Id value in response",
						"Email id Is Not  displayed in response is same we sent in request ,ref Email: "
								+ emailid_response);
			}
			if (currencycode_response != null && currencycode_response.equals("USD")) {
				logger.log(LogStatus.PASS, " Verify Currency code value in response",
						"Currency code displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Currency code value in response",
						"Currency code Is Not  displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			}
		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 9, description = "Create Sale Transaction for Subscription with Payment method", groups = {
			"smoke", "regression" })
	public void createsaleTransactionforsubscriptionwithpaymentmethod() {
		try {
			logger = extent.startTest(
					"Test ID :PS-9 TestScenario : Create Sale Transaction for Subscription with Payment method");
			values = excelReader.readExcel(sheetName, "createsaleTransactionforsubscriptionwithpaymentmethod");
			String Bpid = values.get(DataColumnMapping.BILLPLANID);
			String orderId = values.get(DataColumnMapping.ORDERID);
			String userSubscriptionId = values.get(DataColumnMapping.USERSUBSCRIPTIONID);
			Response paymentresponse=paymentService.getInstance().createSalesTransaction();
			JsonPath ps_jsonPathEvaluator = paymentresponse.jsonPath();
			ArrayList paymentmethodId_array=ps_jsonPathEvaluator.get("customer.creditCards.id");
			String paymentmethodId = paymentmethodId_array.get(0).toString();
			//String paymentmethodId = values.get(DataColumnMapping.PAYMENTMETHODID);
			
			String Request = "{\r\n" + "  \"amount\": 100,\r\n" + "  \"paymentMethodId\": \"" + paymentmethodId
					+ "\",\r\n" + "  \"currencyIsoCode\": \"USD\",\r\n" + "  \"orderId\": \"" + orderId + "\",\r\n"
					+ "  \"userSubscriptions\": [\r\n" + "    {\r\n" + "      \"billingPlanId\": \"" + Bpid + "\",\r\n"
					+ "      \"userSubscriptionId\": \"" + userSubscriptionId + "\"\r\n" + "    },\r\n" + "    {\r\n"
					+ "      \"billingPlanId\":" + Bpid + ",\r\n" + "      \"userSubscriptionId\": "
					+ userSubscriptionId + "\r\n" + "    }\r\n" + "  ],\r\n" + "\"transactionSource\":\"CSR\"}";
			System.out.println("Request :" + Request);
			Response response1 = commonHelper.getInstance().doPostRequest("/transactions", Request, "PS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code ", "Status code is : " + statuscode
						+ " is returned from service end as expected" + "response :" + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code ",
						"Status code is : " + statuscode + " is returned from service end is NOT as expected");

			}
			JsonPath jsonPathEvaluator = response1.jsonPath();
			String currencycode_response = jsonPathEvaluator.get("currencyIsoCode");
			String orderid_response = jsonPathEvaluator.get("orderId");
			List userSubscription = jsonPathEvaluator.get("userSubscriptions.userSubscriptionId");
			String SubscriptionId_Response = userSubscription.get(0).toString();
			List BillPlanIds = jsonPathEvaluator.get("userSubscriptions.billingPlanId");
			String bpID_response = BillPlanIds.get(0).toString();
			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}
			if (currencycode_response != null && currencycode_response.equals("USD")) {
				logger.log(LogStatus.PASS, " Verify Currency code value in response",
						"Currency code displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Currency code value in response",
						"Currency code Is Not  displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			}
			if (SubscriptionId_Response != null && userSubscriptionId.equals(SubscriptionId_Response)) {
				logger.log(LogStatus.PASS, " Verify userSubscriptionId in response",
						"userSubscription Id displayed in response is same we sent in request ,ref userSubscriptionId : "
								+ SubscriptionId_Response);
			} else {
				logger.log(LogStatus.FAIL, " Verify userSubscriptionId in response",
						"userSubscription Id Is Not  displayed in response is same we sent in request ,ref userSubscriptionId: "
								+ SubscriptionId_Response);
			}
			if (bpID_response != null && Bpid.equals(bpID_response)) {
				logger.log(LogStatus.PASS, " Verify Billing Plan value in response",
						"Billing Plan displayed in response is same we sent in request ,ref Billing Plan: "
								+ bpID_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Billing Plan value in response",
						"Billing Plan Is Not  displayed in response is same we sent in request ,ref Billing Plan "
								+ bpID_response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 10, description = "Verify create Sale Transaction for simple product with payment method", groups = {
			"smoke", "regression" })
	public void createsaleTransactionforsimpleproductwithpaymentmethod() {
		try {
			logger = extent.startTest(
					"Test ID :PS-10 TestScenario : Verify create Sale Transaction for simple product with payment method");
			values = excelReader.readExcel(sheetName, "createsaleTransactionforsimpleproductwithpaymentmethod");
			Response paymentresponse=paymentService.getInstance().createSalesTransaction();
			JsonPath ps_jsonPathEvaluator = paymentresponse.jsonPath();
			ArrayList paymentmethodId_array=ps_jsonPathEvaluator.get("customer.creditCards.id");
			String paymentmethodId = paymentmethodId_array.get(0).toString();
			//String paymentmethodId = values.get(DataColumnMapping.PAYMENTMETHODID);
			String orderId = values.get(DataColumnMapping.ORDERID);
			String Request = "{\r\n" + "  \"amount\": 200,\r\n" + " \"paymentMethodId\": \"" + paymentmethodId
					+ "\",\r\n" + "  \"currencyIsoCode\": \"USD\",\r\n" + "  \"orderId\": \"" + orderId + "\",\r\n"
					+ "\"transactionSource\":\"CSR\"}";
			System.out.println("Request :" + Request);
			Response response1 = commonHelper.getInstance().doPostRequest("/transactions", Request, "PS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code ", "Status code is : " + statuscode
						+ " is returned from service end as expected" + "response :" + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code ",
						"Status code is : " + statuscode + " is returned from service end is NOT as expected");

			}
			JsonPath jsonPathEvaluator = response1.jsonPath();
			String orderid_response = jsonPathEvaluator.get("orderId");

			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}

		} catch (Exception e) {
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}

	@Test(priority = 11, description = "Verify create Sale Transaction with new customer and credit card", groups = {
			"smoke", "regression" })
	public void createnewsubscriptionTransactionwithnewcustomerandcreditcard() {
		try {
			logger = extent.startTest(
					"Test ID :PS-11 TestScenario : Verify create Sale Transaction with new customer and credit card");
			values = excelReader.readExcel(sheetName, "createsaleTransaction");
			String Bpid = values.get(DataColumnMapping.BILLPLANID);
			String firstName = RandomNumber.getalphabets();
			String countrycodealpha2 = values.get(DataColumnMapping.COUNRYCODEALPHA2);
			String customerFirstName = RandomNumber.getalphabets();
			String customerLastName = RandomNumber.getalphabets();
			String EmailId = RandomNumber.getalphabets() + "@test.com";
			String orderId = values.get(DataColumnMapping.ORDERID);
			String paymentmethodnonce = values.get(DataColumnMapping.PAYMENTMETHODNONCE);
			String userSubscriptionId = values.get(DataColumnMapping.USERSUBSCRIPTIONID);
			String Request = "{\r\n" + "  \"amount\": 100,\r\n" + "\r\n" + "  \"creditCard\": {\r\n"
					+ "    \"company\": \"compnara29\",\r\n" + "    \"countryCodeAlpha2\": \"" + countrycodealpha2
					+ "\",\r\n" + "    \"default\": true,\r\n" + "    \"firstName\": \"" + firstName + "\",\r\n"
					+ "    \"lastName\": \"" + customerLastName + "\",\r\n" + "    \"locality\": \"string\",\r\n"
					+ "    \"paymentMethodNonce\": \"" + paymentmethodnonce + "\",\r\n"
					+ "    \"postalCode\": \"string\",\r\n" + "    \"region\": \"string\",\r\n"
					+ "    \"street1\": \"string\",\r\n" + "    \"street2\": \"string\"\r\n" + "  },\r\n"
					+ "  \"currencyIsoCode\": \"USD\",\r\n" + "  \"customer\": {\r\n" + "    \"email\": \"" + EmailId
					+ "\",\r\n" + "    \"firstName\": \"" + customerFirstName + "\",\r\n" + "    \"lastName\": \""
					+ customerLastName + "\"\r\n" + "  },\r\n" + "  \"orderId\": \"" + orderId + "\",\r\n"
					+ " \"userSubscriptions\": [\r\n" + "    {\r\n" + "      \"billingPlanId\": " + Bpid + ",\r\n"
					+ "      \"userSubscriptionId\": " + userSubscriptionId + "\r\n" + "    },\r\n" + "    {\r\n"
					+ "      \"billingPlanId\": " + Bpid + ",\r\n" + "      \"userSubscriptionId\": "
					+ userSubscriptionId + "\r\n" + "    }\r\n" + "  ]\r\n," + "\"transactionSource\":\"CSR\"}";
			System.out.println("Request :" + Request);
			Response response1 = commonHelper.getInstance().doPostRequest("/transactions", Request, "PS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code ", "Status code is : " + statuscode
						+ " is returned from service end as expected" + "response :" + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code ",
						"Status code is : " + statuscode + " is returned from service end is NOT as expected");

			}
			JsonPath jsonPathEvaluator = response1.jsonPath();
			String orderid_response = jsonPathEvaluator.get("orderId");
			String firstName_response = jsonPathEvaluator.get("customer.firstName");
			String lastName_response = jsonPathEvaluator.get("customer.lastName");
			String amount_response = Integer.toString(Math.round(jsonPathEvaluator.getFloat("amount")));
			String emailid_response = jsonPathEvaluator.get("customer.email");
			String currencycode_response = jsonPathEvaluator.get("currencyIsoCode");
			ArrayList<String> countrycode_response = jsonPathEvaluator.get("customer.creditCards.countryCodeAlpha2");
			System.out.println("countrycode_response :" + countrycode_response);
			String SubscriptionId_Response;
			ArrayList userSubscription = jsonPathEvaluator.get("userSubscriptions.userSubscriptionId");
			SubscriptionId_Response = userSubscription.get(0).toString();

			ArrayList billingPlanId = jsonPathEvaluator.get("userSubscriptions.billingPlanId");
			String bpID_response = billingPlanId.get(0).toString();
			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}
			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}
			if (firstName_response != null && firstName_response.equals(customerFirstName)) {
				logger.log(LogStatus.PASS, " Verify FirstName value in response",
						"First Name displayed in response is same we sent in request ,ref First Name: "
								+ firstName_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify First Name value in response",
						"First Name Is Not  displayed in response is same we sent in request ,ref First Name: "
								+ firstName_response);
			}
			if (lastName_response != null && lastName_response.equals(customerLastName)) {
				logger.log(LogStatus.PASS, " Verify Last Name value in response",
						"Last Name displayed in response is same we sent in request ,ref Last Name: "
								+ lastName_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Last Name value in response",
						"Last Name Is Not  displayed in response is same we sent in request ,ref Last Name: "
								+ lastName_response);
			}
			if (amount_response != null && amount_response.equals("100")) {
				logger.log(LogStatus.PASS, " Verify Amount value in response",
						"Amount is displayed in response is same we sent in request ,ref Amount: " + amount_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Amount value in response",
						"Amount Is Not  displayed in response is same we sent in request ,ref Amounts: "
								+ amount_response);
			}
			if (emailid_response != null && emailid_response.equals(EmailId)) {
				logger.log(LogStatus.PASS, " Verify Email ID value in response",
						"Email id displayed in response is same we sent in request ,ref email Id: " + emailid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Email Id value in response",
						"Email id Is Not  displayed in response is same we sent in request ,ref Email: "
								+ emailid_response);
			}
			if (currencycode_response != null && currencycode_response.equals("USD")) {
				logger.log(LogStatus.PASS, " Verify Currency code value in response",
						"Currency code displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Currency code value in response",
						"Currency code Is Not  displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			}
			if (countrycode_response != null && countrycode_response.get(0).equals(countrycodealpha2)) {
				logger.log(LogStatus.PASS, " Verify countrycode  value in response",
						"countrycode  displayed in response is same we sent in request ,ref countrycode: "
								+ countrycode_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify countrycode value in response",
						"countrycode Is Not  displayed in response is same we sent in request ,ref countrycode: "
								+ countrycode_response);
			}
			if (SubscriptionId_Response != null && userSubscriptionId.equals(SubscriptionId_Response)) {
				logger.log(LogStatus.PASS, " Verify userSubscriptionId in response",
						"userSubscription Id displayed in response is same we sent in request ,ref userSubscriptionId : "
								+ SubscriptionId_Response);
			} else {
				logger.log(LogStatus.FAIL, " Verify userSubscriptionId in response",
						"userSubscription Id Is Not  displayed in response is same we sent in request ,ref userSubscriptionId: "
								+ SubscriptionId_Response);
			}
			if (bpID_response != null && Bpid.equals(bpID_response)) {
				logger.log(LogStatus.PASS, " Verify Billing Plan value in response",
						"Billing Plan displayed in response is same we sent in request ,ref Billing Plan: "
								+ bpID_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Billing Plan value in response",
						"Billing Plan Is Not  displayed in response is same we sent in request ,ref Billing Plan "
								+ bpID_response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@Test(priority = 11, description = "Validate Update transaction By Updating order Id and subscriptionDetails", groups = {
			"smoke", "regression" })
	public void updateTransaction() {
		try {
			logger = extent.startTest(
					"Test ID :PS-12 TestScenario : Validate Update transaction By Updating order Id,subscriptionDetails");
			values = excelReader.readExcel(sheetName, "createsaleTransaction");
			String Bpid = values.get(DataColumnMapping.BILLPLANID);
			String firstName = RandomNumber.getalphabets();
			String countrycodealpha2 = values.get(DataColumnMapping.COUNRYCODEALPHA2);
			String customerFirstName = RandomNumber.getalphabets();
			String customerLastName = RandomNumber.getalphabets();
			String EmailId = RandomNumber.getalphabets() + "@test.com";
			String orderId = values.get(DataColumnMapping.ORDERID);
			String paymentmethodnonce = values.get(DataColumnMapping.PAYMENTMETHODNONCE);
			String userSubscriptionId = values.get(DataColumnMapping.USERSUBSCRIPTIONID);
			String Request = "{\r\n" + "  \"amount\": 100,\r\n" + "\r\n" + "  \"creditCard\": {\r\n"
					+ "    \"company\": \"compnara29\",\r\n" + "    \"countryCodeAlpha2\": \"" + countrycodealpha2
					+ "\",\r\n" + "    \"default\": true,\r\n" + "    \"firstName\": \"" + firstName + "\",\r\n"
					+ "    \"lastName\": \"" + customerLastName + "\",\r\n" + "    \"locality\": \"string\",\r\n"
					+ "    \"paymentMethodNonce\": \"" + paymentmethodnonce + "\",\r\n"
					+ "    \"postalCode\": \"string\",\r\n" + "    \"region\": \"string\",\r\n"
					+ "    \"street1\": \"string\",\r\n" + "    \"street2\": \"string\"\r\n" + "  },\r\n"
					+ "  \"currencyIsoCode\": \"USD\",\r\n" + "  \"customer\": {\r\n" + "    \"email\": \"" + EmailId
					+ "\",\r\n" + "    \"firstName\": \"" + customerFirstName + "\",\r\n" + "    \"lastName\": \""
					+ customerLastName + "\"\r\n" + "  },\r\n" + "  \"orderId\": \"" + orderId + "\",\r\n"
					+ " \"userSubscriptions\": [\r\n" + "    {\r\n" + "      \"billingPlanId\": " + Bpid + ",\r\n"
					+ "      \"userSubscriptionId\": " + userSubscriptionId + "\r\n" + "    },\r\n" + "    {\r\n"
					+ "      \"billingPlanId\": " + Bpid + ",\r\n" + "      \"userSubscriptionId\": "
					+ userSubscriptionId + "\r\n" + "    }\r\n" + "  ]\r\n," + "\"transactionSource\":\"CSR\"}";
			System.out.println("Request :" + Request);
			Response response1 = commonHelper.getInstance().doPostRequest("/transactions", Request, "PS");
			int statuscode = response1.getStatusCode();
			String responseString = response1.getBody().asString();
			if (statuscode == 201) {
				logger.log(LogStatus.PASS, " Verify Response Status code ", "Status code is : " + statuscode
						+ " is returned from service end as expected" + "response :" + responseString);
			} else {
				logger.log(LogStatus.FAIL, " Verify Response Status code ",
						"Status code is : " + statuscode + " is returned from service end is NOT as expected");

			}
			JsonPath jsonPathEvaluator = response1.jsonPath();
			String orderid_response = jsonPathEvaluator.get("orderId");
			String firstName_response = jsonPathEvaluator.get("customer.firstName");
			String lastName_response = jsonPathEvaluator.get("customer.lastName");
			String amount_response = Integer.toString(Math.round(jsonPathEvaluator.getFloat("amount")));
			String emailid_response = jsonPathEvaluator.get("customer.email");
			String currencycode_response = jsonPathEvaluator.get("currencyIsoCode");
			ArrayList<String> countrycode_response = jsonPathEvaluator.get("customer.creditCards.countryCodeAlpha2");
			System.out.println("countrycode_response :" + countrycode_response);
			String SubscriptionId_Response;
			ArrayList userSubscription = jsonPathEvaluator.get("userSubscriptions.userSubscriptionId");
			SubscriptionId_Response = userSubscription.get(0).toString();
			Integer TransactionId=jsonPathEvaluator.get("id");
			ArrayList billingPlanId = jsonPathEvaluator.get("userSubscriptions.billingPlanId");
			String bpID_response = billingPlanId.get(0).toString();
			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}
			if (orderid_response != null && orderid_response.equals(orderId)) {
				logger.log(LogStatus.PASS, " Verify order Id value in response",
						"order id displayed in response is same we sent in request ,ref order Id: " + orderid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify order Id value in response",
						"order id Is Not  displayed in response is same we sent in request ,ref order Id: "
								+ orderid_response);
			}
			if (firstName_response != null && firstName_response.equals(customerFirstName)) {
				logger.log(LogStatus.PASS, " Verify FirstName value in response",
						"First Name displayed in response is same we sent in request ,ref First Name: "
								+ firstName_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify First Name value in response",
						"First Name Is Not  displayed in response is same we sent in request ,ref First Name: "
								+ firstName_response);
			}
			if (lastName_response != null && lastName_response.equals(customerLastName)) {
				logger.log(LogStatus.PASS, " Verify Last Name value in response",
						"Last Name displayed in response is same we sent in request ,ref Last Name: "
								+ lastName_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Last Name value in response",
						"Last Name Is Not  displayed in response is same we sent in request ,ref Last Name: "
								+ lastName_response);
			}
			if (amount_response != null && amount_response.equals("100")) {
				logger.log(LogStatus.PASS, " Verify Amount value in response",
						"Amount is displayed in response is same we sent in request ,ref Amount: " + amount_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Amount value in response",
						"Amount Is Not  displayed in response is same we sent in request ,ref Amounts: "
								+ amount_response);
			}
			if (emailid_response != null && emailid_response.equals(EmailId)) {
				logger.log(LogStatus.PASS, " Verify Email ID value in response",
						"Email id displayed in response is same we sent in request ,ref email Id: " + emailid_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Email Id value in response",
						"Email id Is Not  displayed in response is same we sent in request ,ref Email: "
								+ emailid_response);
			}
			if (currencycode_response != null && currencycode_response.equals("USD")) {
				logger.log(LogStatus.PASS, " Verify Currency code value in response",
						"Currency code displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Currency code value in response",
						"Currency code Is Not  displayed in response is same we sent in request ,ref Currency code: "
								+ currencycode_response);
			}
			if (countrycode_response != null && countrycode_response.get(0).equals(countrycodealpha2)) {
				logger.log(LogStatus.PASS, " Verify countrycode  value in response",
						"countrycode  displayed in response is same we sent in request ,ref countrycode: "
								+ countrycode_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify countrycode value in response",
						"countrycode Is Not  displayed in response is same we sent in request ,ref countrycode: "
								+ countrycode_response);
			}
			if (SubscriptionId_Response != null && userSubscriptionId.equals(SubscriptionId_Response)) {
				logger.log(LogStatus.PASS, " Verify userSubscriptionId in response",
						"userSubscription Id displayed in response is same we sent in request ,ref userSubscriptionId : "
								+ SubscriptionId_Response);
			} else {
				logger.log(LogStatus.FAIL, " Verify userSubscriptionId in response",
						"userSubscription Id Is Not  displayed in response is same we sent in request ,ref userSubscriptionId: "
								+ SubscriptionId_Response);
			}
			if (bpID_response != null && Bpid.equals(bpID_response)) {
				logger.log(LogStatus.PASS, " Verify Billing Plan value in response",
						"Billing Plan displayed in response is same we sent in request ,ref Billing Plan: "
								+ bpID_response);
			} else {
				logger.log(LogStatus.FAIL, " Verify Billing Plan value in response",
						"Billing Plan Is Not  displayed in response is same we sent in request ,ref Billing Plan "
								+ bpID_response);
			}
			Response res = SMS_US.getInstance().createUserSubscription();
			JsonPath ps_jsonPathEvaluator = res.jsonPath();
			// ArrayList
			// userSubscriptionId=ps_jsonPathEvaluator.get("customer.creditCards.id");
			Integer subscriptionid = ps_jsonPathEvaluator.get("id");
			String new_orderId=RandomNumber.getalphabets();
			String update_Request="{\r\n" + 
					"  \"orderId\": \""+new_orderId+"\",\r\n" + 
					"   \"userSubscriptions\": [\r\n" + 
					"    {\r\n" + 
					"      \"billingPlanId\": 3,\r\n" + 
					"      \"userSubscriptionId\": "+subscriptionid+"\r\n" + 
					"    }\r\n" + 
					"  ]\r\n" + 
					"}";
			Response update_response1 = commonHelper.getInstance().doPatchRequest("/transactions/"+TransactionId, update_Request, "PS");
			int update_statuscode = update_response1.getStatusCode();
			String update_responseString = update_response1.getBody().asString();
					if (update_statuscode == 200) {
						logger.log(LogStatus.PASS, " Verify Response Status code ", "Status code is : " + update_statuscode
								+ " is returned from service end after updating as expected for the transation id : "+TransactionId + "response :" + update_responseString);
					} else {
						logger.log(LogStatus.FAIL, " Verify Response Status code ",
								"Status code is : " + update_statuscode + " is returned from service end is NOT as expected");

					}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, " Verify Whether the Payment Service Working ",
					"Paymenet service failed and its Exception stach trace : " + e.getMessage());
		}
	}
	@AfterTest
	public void endReport() {
		extent.endTest(logger);
		extent.flush();
	}
}