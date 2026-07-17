package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import factory.BrowserFactory;
import helper.ConfigReader;

public class BaseClass 
{
	public WebDriver driver;
	
	@BeforeClass
	public void setupApplication()
	{
		System.out.println("**** Executing Before Class For Setup ****");
		
		String browser=ConfigReader.getValue("browser");
		
		driver=BrowserFactory.startBrowser(browser);
	}

	@AfterClass
	public void tearDownApplication()
	{
		System.out.println("**** Executing After Class For TearDown ****");
		
		driver.quit();
	}
	
}
