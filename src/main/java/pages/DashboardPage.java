package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;

public class DashboardPage extends BasePage
{

	public DashboardPage(WebDriver driver)
	{
		super(driver);
	}

	private By welcome=By.className("welcomeMessage");

	public String getWelcomeText()
	{
		return getText(welcome);
	}

}
