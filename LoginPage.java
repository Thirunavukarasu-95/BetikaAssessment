import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

// Verify that invalid login credentials result in unsuccessful login
public class LoginPage extends ActionClass {
    @Test(enabled = false)
    public void validLoginPage() {
        WebDriver driver = chrome();
        ActionClass actionClass = new ActionClass();
        ActionClass.Excelpath();
        ActionClass.Sheetname();
        String url = readDataFromExcel(getSheet(), 1, 1);
        System.out.println(readDataFromExcel(getSheet(), 1, 1));

        // Valid Login
        driver.get(url);
        actionClass.explicitlyWait(driver, By.xpath("//a[contains(text(),'Login')]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='text']")).sendKeys("254735638271");
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='password']")).sendKeys("2020");
        actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Login')]")).click();
        maximizeWindow(url, driver);
     // Capture screenshot on verification failure
        actionClass.captureScreenshot(driver, "ValidLoginPageCase1");

        // Assertion for successful login
        String expectedTitle = "Betika | Best Online Sports Betting in Kenya";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Valid login was not successful. Title mismatch.");
        System.out.println("Actual Title (Valid Login): " + actualTitle);
    }
    
    //Phone Number is incorrect and password is correct
    @Test(enabled = false)
    public void invalidLoginPageCase1() {
        WebDriver driver = chrome();
        ActionClass actionClass = new ActionClass();
        ActionClass.Excelpath();
        ActionClass.Sheetname();
        String url = readDataFromExcel(getSheet(), 1, 1);
        System.out.println(readDataFromExcel(getSheet(), 1, 1));

        // Invalid Login
        driver.get(url);
        actionClass.explicitlyWait(driver, By.xpath("//a[contains(text(),'Login')]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='text']")).sendKeys("254735638270");
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='password']")).sendKeys("2020");
        actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Login')]")).click();
        maximizeWindow(url, driver);
     // Capture screenshot on verification failure
        actionClass.captureScreenshot(driver, "InvalidLoginPageCase1");

        // Assertion for unsuccessful login
        String expectedErrorMessage = "This phone number does not exist, please register a new account"; 
        String actualErrorMessage = actionClass.explicitlyWait(driver, By.xpath("//p[contains(.,'This phone number does not exist, please register a new account')]")).getText();
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Invalid login did not produce the expected error message.");
        System.out.println("Actual Error Message (Invalid Login): " + actualErrorMessage);
    }
    
    //Phone Number is correct and password is Incorrect
    @Test(enabled = false)
    public void invalidLoginPageCase2() {
        WebDriver driver = chrome();
        ActionClass actionClass = new ActionClass();
        ActionClass.Excelpath();
        ActionClass.Sheetname();
        String url = readDataFromExcel(getSheet(), 1, 1);
        System.out.println(readDataFromExcel(getSheet(), 1, 1));

        // Invalid Login
        driver.get(url);
        actionClass.explicitlyWait(driver, By.xpath("//a[contains(text(),'Login')]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='text']")).sendKeys("254735638271");
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='password']")).sendKeys("2021");
        actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Login')]")).click();
        maximizeWindow(url, driver);
     // Capture screenshot on verification failure
        actionClass.captureScreenshot(driver, "InvalidLoginPageCase2");

        // Assertion for unsuccessful login
        String expectedErrorMessage = "The mobile and password provided do not match"; 
        String actualErrorMessage = actionClass.explicitlyWait(driver, By.xpath("//p[contains(.,'The mobile and password provided do not match')]")).getText();
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Invalid login did not produce the expected error message.");
        System.out.println("Actual Error Message (Invalid Login): " + actualErrorMessage);
    }
    
    //Both phone number and Password id Incorrect
    @Test(enabled = true)
    public void invalidLoginPageCase3() {
        WebDriver driver = chrome();
        ActionClass actionClass = new ActionClass();
        ActionClass.Excelpath();
        ActionClass.Sheetname();
        String url = readDataFromExcel(getSheet(), 1, 1);
        System.out.println(readDataFromExcel(getSheet(), 1, 1));

        // Invalid Login
        driver.get(url);
        actionClass.explicitlyWait(driver, By.xpath("//a[contains(text(),'Login')]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='text']")).sendKeys("254735638270");
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='password']")).sendKeys("2021");
        actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Login')]")).click();
        maximizeWindow(url, driver);
     // Capture screenshot on verification failure
        actionClass.captureScreenshot(driver, "InvalidLoginPageCase3");

        // Assertion for unsuccessful login
        String expectedErrorMessage = "The mobile and password provided do not match"; 
        String actualErrorMessage = actionClass.explicitlyWait(driver, By.xpath("//p[contains(.,'The mobile and password provided do not match')]")).getText();
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Invalid login did not produce the expected error message.");
        System.out.println("Actual Error Message (Invalid Login): " + actualErrorMessage);
    }
}
