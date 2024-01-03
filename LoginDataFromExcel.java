import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginDataFromExcel extends ActionClass {

    @Test(dataProvider = "loginTestData")
    public void loginTest(String phoneNumber, String password, String expectedErrorMessage, boolean isValid) {
        WebDriver driver = chrome();
        ActionClass actionClass = new ActionClass();
        String url = readDataFromExcel(getSheet(), 1, 1);

        // Remove backticks from phone number and password
        phoneNumber = phoneNumber.replace("`", "");
        password = password.replace("`", "");
        
     // Capture screenshot before verification
        actionClass.captureScreenshot(driver, "BeforeVerification");

        // Perform Login
        driver.get(url);
        actionClass.explicitlyWait(driver, By.xpath("//a[contains(text(),'Login')]")).click();
        
        // Handling StaleElementReferenceException
        WebElement phoneNumberElement = null;
        try {
            phoneNumberElement = actionClass.explicitlyWait(driver, By.xpath("//input[@type='text']"));
            phoneNumberElement.sendKeys(phoneNumber);
        } catch (StaleElementReferenceException e) {
            // Retry by re-locating the element
            phoneNumberElement = actionClass.explicitlyWait(driver, By.xpath("//input[@type='text']"));
            phoneNumberElement.sendKeys(phoneNumber);
        }

        WebElement passwordElement = actionClass.explicitlyWait(driver, By.xpath("//input[@type='password']"));
        passwordElement.sendKeys(password);

        WebElement loginButton = actionClass.explicitlyWait(driver, By.xpath("//button[contains(.,'Login')]"));
        loginButton.click();
        maximizeWindow(url, driver);
        // Verify the result
        if (isValid) {
            // Assertion for successful login
            String expectedTitle = "Betika | Best Online Sports Betting in Kenya";
            String actualTitle = driver.getTitle();
            Assert.assertEquals(actualTitle, expectedTitle, "Valid login was not successful. Title mismatch.");
            System.out.println("Actual Title (Valid Login): " + actualTitle);
        } else {
            // Assertion for unsuccessful login
        	// Capture screenshot on verification failure
            actionClass.captureScreenshot(driver, "VerificationFailure");
            String actualErrorMessage = actionClass.explicitlyWait(driver, By.xpath("//p[contains(.,'" + expectedErrorMessage + "')]")).getText();
            Assert.assertEquals(actualErrorMessage, expectedErrorMessage, "Invalid login did not produce the expected error message.");
            System.out.println("Actual Error Message (Invalid Login): " + actualErrorMessage);
            
        }

        // Close the driver
        driver.quit();
    }

    @DataProvider(name = "loginTestData")
    public Object[][] getLoginTestData() {
        String excelFilePath = ActionClass.Excelpath();
        Sheet sheet = ActionClass.getLoginDataSheet();

        if (sheet == null) {
            throw new RuntimeException("Sheet not found. Check the sheet name.");
        }

        int rowCount = sheet.getPhysicalNumberOfRows();
        Object[][] data = new Object[rowCount - 1][4]; // Assuming the first row is the header

        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);

            // Check if the row or any of the cells are null
            if (row != null && row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null && row.getCell(3) != null) {
                data[i - 1][0] = getCellValueAsString(row.getCell(0)); // Phone number
                data[i - 1][1] = getCellValueAsString(row.getCell(1)); // Password
                data[i - 1][2] = getCellValueAsString(row.getCell(2)); // Expected error message
                data[i - 1][3] = getCellValueAsBoolean(row.getCell(3)); // Is valid login
            } else {
                // Handle the case where a cell is null (provide a default value or throw an exception)
            }
        }

        return data;
    }
    public boolean getCellValueAsBoolean(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == CellType.BOOLEAN) {
                return cell.getBooleanCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Boolean.parseBoolean(cell.getStringCellValue());
            }
        }
        return false; // or handle the null case as needed
    }

    private String getCellValueAsString(Cell cell) {
        if (cell != null) {
            DataFormatter dataFormatter = new DataFormatter();
            return dataFormatter.formatCellValue(cell);
        } else {
            return ""; // or handle the null case as needed
        }
    }
}