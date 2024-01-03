import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterUser extends ActionClass {
    @Test(enabled = true)
    public void registerUser() {
        ActionClass actionClass = new ActionClass();
        WebDriver driver = chrome();

        // Get the Excel path and sheet name
        ActionClass.Excelpath();
        ActionClass.Sheetname();

        // Read data from Excel to get URL
        String url = readDataFromExcel(getSheet(), 1, 1);
        System.out.println("Navigating to URL: " + url);
        driver.get(url);

        // Maximize the window
        maximizeWindow(url, driver);

        // Read UserName and Password from Excel
        String userNameWithBacktick = readDataFromExcel(getSheet(), 2, 1);

        // Remove the backtick if it exists for UserName
        String userName = userNameWithBacktick.replace("`", "");

        // Read password from Excel
        String passwordWithBacktick = readDataFromExcel(getSheet(), 3, 1);

        // Remove the backtick if it exists for Password
        String password = passwordWithBacktick.replace("`", "");

        // Click Register Button to Register a User
        actionClass.explicitlyWait(driver, By.xpath("//a[contains(text(),'Register')]")).click();

        // Enter UserName
        actionClass.explicitlyWait(driver, By.xpath("//div/div/div[2]/div/input")).sendKeys(userName);

        // Enter Password
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='password']")).sendKeys(password);

        // Enter Confirm Password
        actionClass.explicitlyWait(driver, By.xpath("(//input[@type='password'])[2]")).sendKeys(password);

        // Click Check Box
        actionClass.explicitlyWait(driver, By.xpath("//div[5]/div/span")).click();

        // Click Register Button
        actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Register')]")).click();
        
       // Assertion to check if registration produced the expected error message
        String expectedErrorMessage = "Mobile already in use please login or reset password";
        String actualErrorMessage = actionClass.explicitlyWait(driver, By.xpath("//p[contains(.,'Mobile already in use please login or reset password.')]")).getText().trim();

        // Check if the actual error message contains the expected error message
        Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage), "Registration did not produce the expected error message.");

        System.out.println("Actual Error Message: " + actualErrorMessage);

    }
}
