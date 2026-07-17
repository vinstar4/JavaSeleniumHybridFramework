package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import base.BasePage;

public class LoginPage extends BasePage
{
	public LoginPage(WebDriver driver)
	{
		super(driver);
	}

	private By username=By.xpath("//input[@placeholder='Enter Email']");

	private By password=By.xpath("//input[@placeholder='Enter Password']");

	private By signIn=By.xpath("//button[text()='Sign in']");

	public DashboardPage loginToApplication(String uname,String pass)
	{
		type(username, uname);
		
		type(password,pass);
		
		click(signIn);
		
		DashboardPage dashboard=PageFactory.initElements(driver, DashboardPage.class);

		return dashboard;
	}

}
