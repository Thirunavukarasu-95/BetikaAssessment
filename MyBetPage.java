import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;


public class MyBetPage extends ActionClass {

    @Test
    public void validLoginPage() throws InterruptedException {
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
        actionClass.captureScreenshot(driver, "ValidLoginPage");
        
        // Refresh the page after login
        driver.navigate().refresh();

        // Assertion for successful login
        String expectedTitle = "Betika | Best Online Sports Betting in Kenya";
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Valid login was not successful. Title mismatch.");
        System.out.println("Actual Title (Valid Login): " + actualTitle);

        // Perform additional scenarios
        placeBet(driver);
        verifyRebet(driver);
        verifyCashout(driver);
        verifyCancelBet(driver);
    }

    public void placeBet(WebDriver driver) {
        ActionClass actionClass = new ActionClass();

        // Find and select the game
        By betAmountLocator = By.xpath("//div[5]/div/span");
        actionClass.explicitlyWait(driver, betAmountLocator).click();

        // Wait for the odds to be clickable
        By oddValueLocator = By.xpath("//div[2]/div[2]/div/button/span");
        WebElement oddValue = actionClass.explicitlyWait(driver, oddValueLocator, 20);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", oddValue);

        // Click the odd value
        oddValue.click();

        // Scroll to the bottom of the page using Actions class
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();

        // Wait for the "Place Bet KES1" button to be clickable
        WebDriverWait wait = new WebDriverWait(driver, 30);
        By placeBetButtonLocator = By.xpath("//button[contains(.,'Place Bet KES1')]");
        WebElement placeBetButton = wait.until(ExpectedConditions.elementToBeClickable(placeBetButtonLocator));

        // Scroll to the element to ensure it's in view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", placeBetButton);

        // Click the "Place Bet KES1" button
        placeBetButton.click();
        
     // Capture screenshot on verification 
        actionClass.captureScreenshot(driver, "placeBet");

        // Add assertion for successful bet placement (if needed)
        By confirmationMessageLocator = By.xpath("//body/div[5]/div/div");
        WebElement confirmationMessage = actionClass.explicitlyWait(driver, confirmationMessageLocator);

        // Assertion for bet placement success (if needed)
        Assert.assertTrue(confirmationMessage.isDisplayed(), "Bet placement was not successful.");

        // Print a success message for reference
        System.out.println("Bet placement was successful!");
    }

    public void verifyRebet(WebDriver driver) {
        // Implement the logic to verify users can Rebet
        ActionClass actionClass = new ActionClass(); // Initialize actionClass
        // Implement the logic to place a bet (as per your requirements)
        actionClass.explicitlyWait(driver, By.xpath("//span[contains(.,'My Bets')]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//div[4]/div/div[2]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Rebet')]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//div[3]/div[4]/div/div/div/span")).click();

        // Add assertion for successful bet placement
        By confirmationMessageLocator = By.xpath("//div[@id='notifications-root']/div/div");
        WebElement confirmationMessage = actionClass.explicitlyWait(driver, confirmationMessageLocator);

        if (confirmationMessage.isDisplayed()) {
            System.out.println("ReBet placement was successful!");
        } else {
            System.out.println("ReBet placement was not successful.");
        }
        // Capture screenshot on verification 
        actionClass.captureScreenshot(driver, "verifyRebet");

        // You can still keep the assertion for better test reliability
        Assert.assertTrue(confirmationMessage.isDisplayed(), "ReBet placement was not successful.");
    }

    public void verifyCashout(WebDriver driver) {
        ActionClass actionClass = new ActionClass();

        // Navigate to the My Bets page
        actionClass.explicitlyWait(driver, By.xpath("//span[contains(.,'My Bets')]")).click();

        // Click on the Cashout button
        actionClass.explicitlyWait(driver, By.xpath("//span[contains(.,'Cashout')]")).click();

        // Click on the Request Cashout button
        actionClass.explicitlyWait(driver, By.xpath("//span[contains(.,'Request Cashout')]")).click();
        
        // Capture screenshot on verification 
        actionClass.captureScreenshot(driver, "verifyCashout");

        // Check if the cashout is unavailable message is displayed
        By cashoutMessageLocator = By.xpath("//small[contains(.,'Cashout unavailable. Please try again in a few minutes')]");
        By cashoutMessageLocator1 = By.xpath("//small[contains(.,'Cashout unavailable because the bet amount is less than KSh 1')]");
        WebElement cashoutMessage = actionClass.explicitlyWait(driver, cashoutMessageLocator);
        WebElement cashoutMessage1 = actionClass.explicitlyWait(driver, cashoutMessageLocator1);
        if (cashoutMessage1.isDisplayed()) {
            // If the message is displayed, close the cashout
            actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'×')]")).click();
            System.out.println("Cashout unavailable because the bet amount is less than KSh 1.");
        }
        if (cashoutMessage.isDisplayed()) {
            // If the message is displayed, close the cashout
            actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'×')]")).click();
            System.out.println("Cashout is unavailable. Closing the cashout.");
        } else {
        	actionClass.explicitlyWait(driver, By.xpath("//span[contains(.,'Request Cashout')]")).click();
            // Print a success message for reference
            System.out.println(actionClass.explicitlyWait(driver, By.xpath("//small[contains(.,'Cashout offer expired')]")));
        }
    }
    public void verifyCancelBet(WebDriver driver) {
        // Implement the logic to verify users can Cancel a bet
        // For example, find the Cancel Bet button, click it, and assert the expected behavior
    }
}
