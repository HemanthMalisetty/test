package com.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.report.RetryAnalyzer;
 
public class testclass {
 
 @Test(retryAnalyzer = RetryAnalyzer.class)
 public void Test1()
 {
 Assert.assertEquals(false, true);
 }
 
 @Test
 public void Test2()
 {
 Assert.assertEquals(false, true);
 }
}
