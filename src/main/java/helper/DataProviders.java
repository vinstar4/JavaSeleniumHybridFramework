package helper;

import org.testng.annotations.DataProvider;

public class DataProviders 
{
	@DataProvider(name="logindetails")
	public static Object[][] getData()
	{
		Object[][] arr=ExcelReader.getData("login");
		
		return arr;
	}

}
