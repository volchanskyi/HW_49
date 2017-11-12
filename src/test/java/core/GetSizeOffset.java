package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.safari.SafariDriver;

public class GetSizeOffset {
    static WebDriver driver;

    // declare the browser
    static String browser = "firefox";
    // declare the webpage (should be synchronized with the IDs in the testData
    // file (CSV) )
    static final String baseUrl = "http://alex.academy/exe/signup/v1/";
    // declare the Test Data File with the IDs, Sizes, Locations and index pages
    static String csvFile = "./test_data/csv/bat/elements_validation_firefox.csv";

    // ------------------------------------------
    // String csvFile = "./test_data/csv/bat/elements_validation_chrome.csv";
    // String csvFile = "./test_data/csv/bat/elements_validation_safari.csv";
    // ------------------------------------------------
    private static void quit() {
	driver.quit();
    }

    private static void validateTestData(String file) throws FileNotFoundException, IOException {
	String cvsLine = "";
	String[] a = null;
	BufferedReader br = new BufferedReader(new FileReader(file));
	while ((cvsLine = br.readLine()) != null) {
	    a = cvsLine.split(";");
	    anylizeSizeOffset(a[0], a[1], a[2], a[3], a[4]);
	}
	br.close();

    }

    public static boolean anylizeSizeOffset(String tc_id, String url, String elemId, String elemSize, String offSet) {
	getDriver(browser, url);
	if (!driver.findElements(By.id(elemId)).isEmpty()) {
	    int widthActual = driver.findElement(By.id(elemId)).getSize().getWidth();
	    int heightActual = driver.findElement(By.id(elemId)).getSize().getHeight();
	    int xOffsetActual = driver.findElement(By.id(elemId)).getLocation().getX();
	    int yOffsetActual = driver.findElement(By.id(elemId)).getLocation().getY();
	    int widthExpected = Integer
		    .parseInt(elemSize.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "").split(",")[0]);
	    int heightExpected = Integer
		    .parseInt(elemSize.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "").split(",")[1]);
	    int xOffsetExpected = Integer
		    .parseInt(offSet.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "").split(",")[0]);
	    int yOffsetExpected = Integer
		    .parseInt(offSet.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "").split(",")[1]);

	    if ((widthActual >= widthExpected - xOffsetActual && widthActual <= widthExpected + xOffsetActual)
		    && (heightActual >= heightExpected - yOffsetActual
			    && heightActual <= heightExpected + yOffsetActual)
		    && (xOffsetActual >= xOffsetExpected && xOffsetActual <= xOffsetExpected)
		    && (yOffsetActual >= yOffsetExpected && yOffsetActual <= yOffsetExpected)) {
		quit();
		return true;

	    } else {
		System.out.println("--------------------------------------");
		System.out.println(elemId);
		System.out.println(
			"Size Actual = " + widthActual + ", " + heightActual + "\n" + "Expected = " + elemSize);
		System.out.println(
			"Location Actual = " + xOffsetActual + ", " + yOffsetActual + "\n" + "Expected = " + offSet);
		System.out.println("--------------------------------------");
		quit();
		return false;
	    }
	} else {
	    System.out.println("Error. Probaly can`t find the id");
	    quit();
	    return false;
	}

    }

    public static void getDriver(String browser, String url) {
	Logger logger = Logger.getLogger("");
	logger.setLevel(Level.OFF);
	if (browser.equalsIgnoreCase("chrome")) {
	    String driverPath = "";
	    if (System.getProperty("os.name").toUpperCase().contains("MAC"))
		driverPath = "./resources/webdrivers/mac/chromedriver";
	    else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
		driverPath = "./resources/webdrivers/pc/chromedriver.exe";
	    else
		throw new IllegalArgumentException("Unknown OS");
	    System.setProperty("webdriver.chrome.driver", driverPath);
	    System.setProperty("webdriver.chrome.silentOutput", "true");
	    ChromeOptions option = new ChromeOptions();
	    option.addArguments("disable-infobars");
	    option.addArguments("--disable-notifications");
	    if (System.getProperty("os.name").toUpperCase().contains("MAC"))
		option.addArguments("-start-fullscreen");
	    else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
		option.addArguments("--start-maximized");
	    else
		throw new IllegalArgumentException("Unknown OS");
	    driver = new ChromeDriver(option);
	    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	else if (browser.equalsIgnoreCase("firefox")) {
	    String driverPath = "";
	    if (System.getProperty("os.name").toUpperCase().contains("MAC"))
		driverPath = "./resources/webdrivers/mac/geckodriver.sh";
	    else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
		driverPath = "./resources/webdrivers/pc/geckodriver.exe";
	    else
		throw new IllegalArgumentException("Unknown OS");

	    System.setProperty("webdriver.gecko.driver", driverPath);
	    driver = new FirefoxDriver();
	    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	    driver.manage().window().maximize();
	}

	else if (browser.equalsIgnoreCase("safari")) {
	    if (!System.getProperty("os.name").contains("Mac")) {
		throw new IllegalArgumentException("Safari is available only on Mac");
	    }
	    driver = new SafariDriver();
	    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	    driver.manage().window().maximize();
	} else {
	    driver = new HtmlUnitDriver();
	    ((HtmlUnitDriver) driver).setJavascriptEnabled(true);
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	driver.get(baseUrl + url);
    }

    public static void main(String[] args) throws InterruptedException, IOException {

	validateTestData(csvFile);

    }

}
