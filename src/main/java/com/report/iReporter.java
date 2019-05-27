package com.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import java.util.Set;

import org.testng.IReporter;

import org.testng.IResultMap;

import org.testng.ISuite;

import org.testng.ISuiteResult;

import org.testng.ITestContext;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

public class iReporter extends TestListenerAdapter implements IReporter {
	PrintWriter mOut;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		Set<ITestResult> testsPassed = null;
		Set<ITestResult> testsFailed=null;
		Set<ITestResult> testsSkipped=null;
		System.out.println("String outputDirectory :" + outputDirectory);
		new File(outputDirectory).mkdirs();
		try {
			mOut = new PrintWriter(new BufferedWriter(new FileWriter(new File(outputDirectory, "custom-report.html"))));
		} catch (IOException e) {
			System.out.println("Error in creating writer: " + e);
		}
		startHtml();
		print("Suites run: " + suites.size());
		for (ISuite suite : suites) {
			print("Suite>" + suite.getName());
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (String testName : suiteResults.keySet()) {

				print("    Test>" + testName);
				ISuiteResult suiteResult = suiteResults.get(testName);
				ITestContext testContext = suiteResult.getTestContext();
				print("        Failed>" + testContext.getFailedTests().size());
				IResultMap failedResult = testContext.getFailedTests();
				testsFailed = failedResult.getAllResults();
				for (ITestResult testResult : testsFailed) {
					print("            " + testResult.getName());
					print("                " + testResult.getThrowable());
				}
				IResultMap passResult = testContext.getPassedTests();
				testsPassed = passResult.getAllResults();
				print("        Passed>" + testsPassed.size());
				for (ITestResult testResult : testsPassed) {
					print("            " + testResult.getName() + ">took "
							+ (testResult.getEndMillis() - testResult.getStartMillis()) + "ms");
				}
				IResultMap skippedResult = testContext.getSkippedTests();
				testsSkipped = skippedResult.getAllResults();
				print("        Skipped>" + testsSkipped.size());
				for (ITestResult testResult : testsSkipped) {
					print("            " + testResult.getName());
				}

			}
		}
		print_statusTableform(testsPassed.size(), testsFailed.size());
		endHtml();
		mOut.flush();
		mOut.close();
	}

	private void print_statusTableform(int noofpass, int nooffail) {
		HashMap<String, String> statuswithscenario = Listener.TestDetails;
		mOut.println("No Of TestCase Tested :" + noofpass + nooffail);
		mOut.println("<table>");
		mOut.println("<tr>");// table heading
		mOut.println("<th>Service Call</th>");
		mOut.println("<th>TestScenario</th>");
		mOut.println("<th>URI</th>");
		mOut.println("<th>Status</th>");
		mOut.println("<th>Response</th>");
		mOut.println("<th>Comment</th>");
		mOut.println("</tr>");
		for (int i = 0; i < statuswithscenario.size(); i++) {
			mOut.println("<tr>");// table heading
			mOut.println("<th>\"<%=statuswithscenario.get(\"Service Call\")%>\"</th>");
			mOut.println("<th>\"<%=statuswithscenario.get(\"TestScenario\")%>\"</th>");
			mOut.println("<th>\"<%=statuswithscenario.get(\"URI\")%>\"</th>");
			mOut.println("<th>\"<%=statuswithscenario.get(\"Status\")%>\"</th>");
			mOut.println("<th>\"<%=statuswithscenario.get(\"Response\")%>\"</th>");
			mOut.println("<th>\"<%=statuswithscenario.get(\"Comment\")%>\"</th>");
			mOut.println("</tr>");

		}
		mOut.println("</table>");
	}

	private void print(String text) {
		System.out.println(text);
		mOut.println(text + "");

	}

	private void startHtml() {
		mOut.println("");
		mOut.println("");
		mOut.println("TestNG Html Report Example");
		mOut.println("");
		mOut.println("");
	}

	private void endHtml() {
		mOut.println("");
	}
}
