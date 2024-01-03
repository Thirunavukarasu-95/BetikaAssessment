import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BetPlacement extends ActionClass {
    private ActionClass actionClass;
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = chrome();
        actionClass = new ActionClass();
    }

    @Test(enabled = true)
    public void betPlacement() throws InterruptedException {
        String url = readDataFromExcel(getSheet(), 1, 1);
        System.out.println(readDataFromExcel(getSheet(), 1, 1));
        driver.get(url);
        actionClass.explicitlyWait(driver, By.xpath("//a[contains(text(),'Login')]")).click();
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='text']")).sendKeys("254735638271");
        actionClass.explicitlyWait(driver, By.xpath("//input[@type='password']")).sendKeys("2020");
        actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Login')]")).click();
        actionClass.maximizeWindow(null, driver);
        
     // Capture a screenshot
        actionClass.captureScreenshot(driver, "BetPlacementScreenshot");

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("sports-list")));

        // Locate the element for "Place Bet KES1" button
        By betButtonLocator = By.xpath("//span[contains(.,'Place Bet KES1')]");

        for (int i = 1; i <= 3; i++) {
            processGame(i, betButtonLocator);
        }
    }

    private void processGame(int gameNumber, By betButtonLocator) throws InterruptedException {
        WebElement gameElement = actionClass.explicitlyWait(driver, By.xpath("//div[" + gameNumber + "]/div/span"));
        clickElementWithRetry(gameElement, driver);
        System.out.println(driver.getCurrentUrl());

        try {
            WebElement oddValueElement = actionClass.explicitlyWait(driver,
                    By.xpath("//div[@class='prebet-match__odds__container']//button[1]"));
            clickElementWithRetry(oddValueElement, driver);

            Thread.sleep(3000);

            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement shareButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(.,'Share')]")));
            shareButton.click();

            WebElement copyLinkButton = actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Copy Link')]"));
            clickElementWithRetry(copyLinkButton, driver);

            String copiedLink = getClipboardContents();
            System.out.println("Game " + gameNumber + " Copied Share Link: " + copiedLink);
            actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Cancel')]")).click();

            // Check if "Place Bet KES1" button is clickable
            WebElement placeBetButton = actionClass.explicitlyWait(driver, betButtonLocator);
            if (isElementClickable(placeBetButton, driver)) {
                placeBetButton.click();

                boolean isBetSuccessful = verifyBetSuccess(driver);
                String assertionMessage = "Bet placement for Game " + gameNumber + " was " +
                        (isBetSuccessful ? "successful" : "not successful");
                System.out.println(assertionMessage);
                Assert.assertTrue(isBetSuccessful, assertionMessage);
            } else {
                // Print a message when "Place Bet KES1" button is not clickable
                System.out.println("Warning: 'Place Bet KES1' button is not clickable for Game " + gameNumber);
            }
        } catch (NoSuchElementException e) {
            // Handle the case when odds button is not found
            System.out.println("No bet games available for Game " + gameNumber);
        } catch (StaleElementReferenceException e) {
            // Handle the stale element exception by retrying the operation
            System.out.println("Caught StaleElementReferenceException. Retrying the operation for Game " + gameNumber);
            processGame(gameNumber, betButtonLocator);
        } catch (TimeoutException e) {
            // Handle the timeout exception (element not clickable)
            System.out.println("Warning: 'Place Bet KES1' button is not clickable for Game " + gameNumber);
        }
    }

    private boolean isElementClickable(WebElement element, WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
