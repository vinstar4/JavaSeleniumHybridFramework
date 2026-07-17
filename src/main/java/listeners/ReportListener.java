package listeners;

import java.io.File;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.chaintest.plugins.ChainTestListener;

import factory.BrowserFactory;
import helper.ConfigReader;
import helper.Utility;

public class ReportListener implements ITestListener
{
	 public void onTestSuccess(ITestResult result) 
	 {
		 
		 if(ConfigReader.getValue("screenshot_on_success").equalsIgnoreCase("true"))
		 {
			 ChainTestListener.embed(new File(Utility.screenshot(BrowserFactory.getDriver())), "image/png");
		 }
		 
		 ChainTestListener.log("Test Executed - Passed "+result.getMethod().getMethodName());	 
	 }
	 
	 public void onTestFailure(ITestResult result) 
	 {
		 
		 if(ConfigReader.getValue("screenshot_on_failure").equalsIgnoreCase("true"))
		 {
			 ChainTestListener.embed(new File(Utility.screenshot(BrowserFactory.getDriver())), "image/png"); 
		 }
		 
		 ChainTestListener.log("Test Executed - Failed "+result.getMethod().getMethodName());
		 
		 ChainTestListener.log("Exception Trace "+result.getThrowable().getMessage());
		 
	 }

	 public void onTestSkipped(ITestResult result) 
	 {
		 if(ConfigReader.getValue("screenshot_on_skip").equalsIgnoreCase("true"))
		 {
			 ChainTestListener.embed(new File(Utility.screenshot(BrowserFactory.getDriver())), "image/png"); 
		 }
		 
		 ChainTestListener.log("Test Executed - Skipped "+result.getMethod().getMethodName());  
     }
}
