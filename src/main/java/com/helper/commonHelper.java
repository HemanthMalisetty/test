package com.helper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.utilities.propertyReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class commonHelper {
	private static final commonHelper commonHelperobject=new commonHelper();
	RequestSpecification httpRequest;
	private commonHelper() {
		
	}
		public static commonHelper getInstance() {
			
			return commonHelperobject;
		}
	public  Response doGetRequest(String endpoint,String module) throws IOException {
		httpRequest=null;
		switch(module) {
		case "SMS":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			httpRequest = RestAssured.given();
			RestAssured.defaultParser = Parser.JSON;

			return RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
					.get(endpoint).then().contentType(ContentType.JSON).extract().response();
			
		case "PS":
			RestAssured.baseURI = propertyReader.readingProperty("payment-BaseURI");
			RestAssured.defaultParser = Parser.JSON;

			return RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
					.get(endpoint).then().contentType(ContentType.JSON).extract().response();
		case "PL":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			RestAssured.defaultParser = Parser.JSON;

			return RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
					.get(endpoint).then().contentType(ContentType.JSON).extract().response();
		case "US":
			RestAssured.baseURI = propertyReader.readingProperty("User-Subscription-BaseURI");
			RestAssured.defaultParser = Parser.JSON;

			return RestAssured.given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).when()
					.get(endpoint).then().contentType(ContentType.JSON).extract().response();
		}
		return null;
		
	}
	public int countNoofActivePlans(List responselist) {
		int count = 0;
		for (int i = 0; i < responselist.size(); i++) {
			if ((responselist.get(i).toString()).contains("active=true")) {
				count++;
			}
		}
		return count;
	}
	public  Response doPostRequest(String endpoint,String request,String module) throws IOException {
		switch(module) {
		case "SMS":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .post(endpoint).
			            then().extract().response();
		
		case "PS":
			RestAssured.baseURI = propertyReader.readingProperty("payment-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .post(endpoint).
			            then().extract().response();
		
		case "PL":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .post(endpoint).
			            then().extract().response();
		case "US":
			RestAssured.baseURI = propertyReader.readingProperty("User-Subscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .post(endpoint).
			            then().extract().response();	
		}
		return null;
		 
		

	}
	public  Response doPutRequest(String endpoint,String request,String module) throws IOException {
		switch(module) {
		case "SMS":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .put(endpoint).
			            then().extract().response();
		
		case "PS":
			RestAssured.baseURI = propertyReader.readingProperty("payment-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .put(endpoint).
			            then().extract().response();
		case "PL":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .put(endpoint).
			            then().extract().response();
		case "US":
			RestAssured.baseURI = propertyReader.readingProperty("User-Subscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .put(endpoint).
			            then().extract().response();
		}
		return null;
		
		

	}
	public  Response doPatchRequest(String endpoint,String request,String module) throws IOException {
		switch(module) {
		case "SMS":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .patch(endpoint).
			            then().extract().response();
		
		case "PS":
			RestAssured.baseURI = propertyReader.readingProperty("payment-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .patch(endpoint).
			            then().extract().response();
		case "PL":
			RestAssured.baseURI = propertyReader.readingProperty("catloguesubscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .patch(endpoint).
			            then().extract().response();
		case "US":
			RestAssured.baseURI = propertyReader.readingProperty("User-Subscription-BaseURI");
			 return   RestAssured.given()
			            .contentType(ContentType.JSON)
			            .body(request)
			            .patch(endpoint).
			            then().extract().response();
		}
		return null;
		
		

	}
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}
	public String delayDate(int noofdaysdelay) {
		DateTimeFormatter format =
	            DateTimeFormatter.ofPattern("yyyy-MM-dd");

	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime then = now.minusDays(noofdaysdelay);

	        System.out.println(String.format(
	            "Now:  %s\nThen: %s",
	            now.format(format),
	            then.format(format)
	        ));
return String.format(
        "Now:  %s\nThen: %s",
        now.format(format),
        then.format(format));
	}
	public String postDate(int noofdaysafter) {
		DateTimeFormatter format =
	            DateTimeFormatter.ofPattern("yyyy-MM-dd");

	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime then = now.plusDays(noofdaysafter);

	        System.out.println(String.format(
	            "Now:  %s\nThen: %s",
	            now.format(format),
	            then.format(format)
	        ));
return String.format(
        then.format(format));
	}
}
