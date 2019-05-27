package com.helper;

import java.io.IOException;
import java.util.HashMap;

import com.utilities.DataColumnMapping;
import com.utilities.RandomNumber;
import com.utilities.excelReader;
import com.utilities.propertyReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class paymentService {
	String sheetName = "PaymentService";
	HashMap<String, String> values = new HashMap<>();
	RequestSpecification httpRequest;
private static final paymentService paymentserviceobject=new paymentService();
private paymentService() {
	
}
	public static paymentService getInstance() {
		return paymentserviceobject;
	}
	
	public void servicesetUp() {
		try {

			RestAssured.baseURI = propertyReader.readingProperty("payment-BaseURI");
			httpRequest = RestAssured.given();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//Log.assertLog(false, e.getMessage());
		}

	} 
	public String  getPaymentServices() {
		Response response1 = null;
		try {
			response1 = commonHelper.getInstance().doGetRequest("/billing-plans","PS");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res=response1.getBody().asString();
		return res;
		
	}
	public Response createSalesTransaction() throws IOException {
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
				+ customerLastName + "\"\r\n" + "  },\r\n" + " \"orderId\":\"" + orderId + "\",\r\n"+ "\"transactionSource\":\"CSR\"}";
		System.out.println("Request :" + Request);
		Response response1 = commonHelper.getInstance().doPostRequest("/transactions", Request, "PS");
		String responseString = response1.getBody().asString();
		return response1;
	}
}
