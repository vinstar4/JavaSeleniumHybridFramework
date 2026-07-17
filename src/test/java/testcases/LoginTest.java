package testcases;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseClass;
import helper.DataProviders;
import pages.DashboardPage;
import pages.LoginPage;

public class LoginTest extends BaseClass
{
	@Test(description = "This test will cover valid login scenario",dataProvider = "logindetails",dataProviderClass = DataProviders.class)
	public void validLoginTest(String user,String pass)
	{
		LoginPage login=PageFactory.initElements(driver, LoginPage.class);
		
		DashboardPage  dashboard=login.loginToApplication(user, pass);
		
		Assert.assertTrue(dashboard.getWelcomeText().contains("Welcome"));	
		
	}	
}
