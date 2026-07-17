package factory;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import helper.ConfigReader;

public class BrowserFactory 
{
	private static WebDriver driver = null;
	
	
	//getter method
	public static WebDriver getDriver()
	{
		return driver;
	}
	
	
	public static WebDriver startBrowser(String browser)
	{
		System.out.println("**** Starting Session On "+browser);
		
		
		
		if(browser.equalsIgnoreCase("Firefox"))
		{
			driver=new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("Chrome"))
		{
			ChromeOptions opt=new ChromeOptions();
			
			if(ConfigReader.getValue("headless").equalsIgnoreCase("true"))
			{	
				opt.addArguments("--headless=new");
			}
			
			driver=new ChromeDriver(opt);
		}
		else if(browser.equalsIgnoreCase("Edge"))
		{
			driver=new EdgeDriver();
		}
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(ConfigReader.getValue("pageloadtime")))); 
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(ConfigReader.getValue("implicitwait")))); 
		
		driver.get(ConfigReader.getValue("qaenv")+"/login");
		
		return driver;
	}

}
