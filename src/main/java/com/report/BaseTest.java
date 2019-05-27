package com.report;

import java.io.File;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.relevantcodes.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class BaseTest {
	public static ExtentHtmlReporter htmlReporter;
	public static com.relevantcodes.extentreports.ExtentReports extent;
	public static ExtentTest test;
	static HashMap<String, String> TestDetails = new HashMap<>();
	public static com.relevantcodes.extentreports.ExtentTest logger;

	@BeforeSuite
	public void setUp() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/STMExtentReport.html", true);
		extent.addSystemInfo("Host Name", "API Testing").addSystemInfo("Environment", "API Automation Testing")
				.addSystemInfo("User Name", "Nandhini");
		extent.loadConfig(new File(System.getProperty("user.dir") + "src\\testXML\\extent.xml"));

	}

	@AfterTest
	public void getResult(ITestResult result, String description, String actual, String expected) {
		extent.endTest(logger);
	}

	@AfterSuite
	public void teardown() {

		extent.flush();
		extent.close();
	}
}
