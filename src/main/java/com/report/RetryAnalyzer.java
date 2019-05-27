package com.report;


import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	int counter = 0;
	int retryLimit = 2;
	static String Status;
public void  getItestcontext(ITestContext tc) {
		
		 Status=tc.getAttribute("status").toString();
		System.out.println("Status :"+ Status);
	}
	@Override
	public boolean retry(ITestResult result) {
		//getItestcontext();
		if (Status.equals("fail")) {  
		if(counter < retryLimit)
		{
			counter++;
			return true;
		}
	}
		return false;
	}
}
