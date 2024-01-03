import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


public class ActionClass {

    private static Workbook workbook;
    private static WebDriver driver;


    public void maximizeWindow(String browser, WebDriver driver) {
        if ("chrome".equalsIgnoreCase(browser) || "firefox".equalsIgnoreCase(browser)) {
            driver.manage().window().maximize();
        }
        // Add handling for other browsers if needed
    }
    public WebDriver getDriver(String browser) {
        if (driver == null) {
            if ("chrome".equalsIgnoreCase(browser)) {
                driver = chrome();
            } else if ("firefox".equalsIgnoreCase(browser)) {
                driver = firefox();
            } else {
                throw new IllegalArgumentException("Invalid browser: " + browser);
            }
        }
        return driver;
    }
    public void captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String dest = "U:\\Eclipse\\screenshot\\" + screenshotName + "_" + timeStamp + ".png";
            File destination = new File(dest);
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot taken: " + screenshotName);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }
    public static String getLoginDataSheetName() {
        return "Sheet2"; // Change this to the correct sheet name
    }
    @DataProvider(name = "loginTestData")
    public Object[][] getLoginTestData() {
        int rowCount = getLoginDataSheet().getPhysicalNumberOfRows();
        Object[][] data = new Object[rowCount - 1][4];

        for (int i = 1; i < rowCount; i++) {
            Row row = getLoginDataSheet().getRow(i);

            // Print cell type for debugging
            System.out.println("Browser cell type: " + row.getCell(0).getCellType());

            data[i - 1][0] = row.getCell(0).getStringCellValue(); // Browser
            data[i - 1][1] = row.getCell(1).getStringCellValue(); // URL
            data[i - 1][2] = row.getCell(2).getStringCellValue(); // UserName

            // Handle the special character in the password
            Cell passwordCell = row.getCell(3);
            passwordCell.setCellType(CellType.STRING);
            data[i - 1][3] = passwordCell.getStringCellValue(); // Password
        }

        return data;
    }


    public static WebDriver chrome() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }

    public static WebDriver firefox() {
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver();
    }

    public static String Excelpath() {
        return "U:\\Eclipse\\Data.xlsx";
    }

    public static String Sheetname() {
        return "Sheet1";
    }

    public static Sheet getSheet() {
        if (workbook == null) {
            initializeWorkbook();
        }
        return workbook.getSheet(Sheetname());
    }

    private static void initializeWorkbook() {
        try (FileInputStream fis = new FileInputStream(Excelpath())) {
            workbook = WorkbookFactory.create(fis);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

    public static String LoginData() {
        return "Sheet2";
    }
    public static Sheet getLoginDataSheet() {
        if (workbook == null) {
            initializeWorkbook();
        }
        String loginDataSheetName = getLoginDataSheetName();
        Sheet sheet = workbook.getSheet(loginDataSheetName);

        if (sheet == null) {
            throw new RuntimeException("Sheet not found. Check the sheet name: " + loginDataSheetName);
        }

        return sheet;
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


    public WebElement explicitlyWait(WebDriver driver, By locator, int... timeoutSeconds) {
        int timeout = (timeoutSeconds.length > 0) ? timeoutSeconds[0] : 10; // Default to 10 seconds if not provided
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public String readDataFromExcel(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        Cell cell = row.getCell(colNum);

        if (cell != null) {
            DataFormatter dataFormatter = new DataFormatter();
            return dataFormatter.formatCellValue(cell);
        } else {
            throw new IllegalArgumentException("Cell is null");
        }
    }

    public void performMouseClick(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.click(element).perform();
    }

    public void scrollIntoView(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void performMouseHover(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    public boolean verifyBetSuccess(WebDriver driver) {
        By confirmationMessageLocator = By.xpath("//div[@id='notifications-root']/div/div");
        WebElement confirmationMessage = explicitlyWait(driver, confirmationMessageLocator);
        return confirmationMessage.isDisplayed();
    }

    public String getClipboardContents() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void clickElementWithRetry(WebElement element, WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }
}
