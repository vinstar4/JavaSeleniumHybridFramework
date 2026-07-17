package helper;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Utility 
{
	
	/*
	 * 	wait utility
	 *  screenshot
	 *  scrollto
	 *  javascriptexecutor
	 *  switch
	 *  	Alerts
	 *  	Multiple Windows
	 *  	Frames
	 *  	Shadow DOM
	 *  Action
	 *  RandomData Generation
	 *  Faker Lib
	 * 	 
	 * 
	 */
	
	
	public static String screenshot(WebDriver driver)
	{
		
		TakesScreenshot ts=(TakesScreenshot)driver;
		
		File src=ts.getScreenshotAs(OutputType.FILE);
		
		String path=System.getProperty("user.dir")+"/screenshots/Screenshot_"+System.currentTimeMillis()+".png";
		
		File destination=new File(path);
		
		try 
		{
			FileUtils.copyFile(src, destination);
			
		} catch (IOException e) 
		{
			System.out.println("Could not capture screenshot "+e.getMessage());
		}
		
		return path;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
}
