package com.report;

import java.io.File;
import java.util.HashMap;

import org.testng.ITestContext;

import org.testng.ITestListener;

import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class Listener implements ITestListener {
	
	static HashMap<String, String> TestDetails = new HashMap<>();
	String status;
	String testName;
	public  ExtentReports extent;
	public  ExtentTest logger;
	@Override

	public void onStart(ITestContext arg0) {
		testName=arg0.getName();
		System.out.println("Start Of Execution(TEST)->" + arg0.getName());

	}

	@Override

	public void onTestStart(ITestResult arg0) {
		
		System.out.println("Test Started->" + arg0.getName());

	}

	@Override

	public void onTestSuccess(ITestResult arg0) {
		status="Test Status = Pass";
		System.out.println("Test Pass->" + arg0.getName());

	}

	@Override

	public void onTestFailure(ITestResult arg0) {
		status= "Test Status = Fail";
		System.out.println("Test Failed->" + arg0.getName());

	}

	@Override

	public void onTestSkipped(ITestResult arg0) {
		status="Test Status = Skip";
		System.out.println("Test Skipped->" + arg0.getName());

	}

	@Override

	public void onFinish(ITestContext arg0) {

		System.out.println("END Of Execution(TEST)->" + arg0.getName());

	}

	@Override

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {

		// TODO Auto-generated method stub

	}

	public  String logStatus(String description, String actual, String expected) {
		String statuswithdetails =  description + ":" + actual + ":" + expected + ":"
				+ status;
		TestDetails.put(testName,statuswithdetails);
		
		return status;
	}
	@BeforeSuite
	public void report_setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/STMExtentReport.html", true);
		extent.addSystemInfo("Host Name", "API Testing").addSystemInfo("Environment", "API Automation Testing")
				.addSystemInfo("User Name", "Nandhini");
		extent.loadConfig(new File(System.getProperty("user.dir") + "src\\testXML\\extent.xml"));

	}
	@AfterSuite
	public void endReport() {
		extent.flush();
		extent.close();
	}
}
