package core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.testng.ITest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
 
public class ElementsValidationInChromeTest implements ITest {
    static WebDriver driver;
    static final String baseUrl = "http://alex.academy/exe/signup/v1/";
//    String csvFile = "./test_data/csv/bat/test.csv";
     String csvFile = "./test_data/csv/bat/elements_validation_chrome.csv";
    // String csvFile = "./test_data/csv/bat/elements_validation_firefox.csv";
    // String csvFile = "./test_data/csv/bat/elements_validation_safari.csv";
    private String test_name = "";

    public String getTestName() {
	return test_name;
    }

    private void setTestName(String a) {
	test_name = a;
    }

    @BeforeMethod(alwaysRun = true)
    public void bm(Method method, Object[] parameters) {
	setTestName(method.getName());
	Override a = method.getAnnotation(Override.class);
	String testCaseId = (String) parameters[a.id()];
	setTestName(testCaseId);
    }

    @DataProvider(name = "dp")
    public Iterator<String[]> a2d() throws InterruptedException, IOException {
	String cvsLine = "";
	String[] a = null;
	ArrayList<String[]> al = new ArrayList<>();
	BufferedReader br = new BufferedReader(new FileReader(csvFile));
	while ((cvsLine = br.readLine()) != null) {
	    a = cvsLine.split(";");
	    al.add(a);
	}
	br.close();
	return al.iterator();
    }
    @Override
    @Test(dataProvider = "dp")
    public void test(String tc_id, String url, String element_id, String element_size, String element_location) {

	getDriver("chrome", url);
	assertThat(isPresent(element_id, driver), equalTo(true));
	assertThat(size(element_id, driver), equalTo(element_size));
	assertThat(location(element_id, driver), equalTo(element_location));
    }

    @AfterMethod
    public void am() {
	driver.quit();
    }

    @AfterClass
    public void ac() {driver.quit();}

    
	public static void getDriver(String browser, String url) {
			Logger logger = Logger.getLogger(""); logger.setLevel(Level.OFF);
             if (browser.equalsIgnoreCase("chrome")) {
          		String driverPath = "";
   		     if (System.getProperty("os.name").toUpperCase().contains("MAC"))      driverPath = "./resources/webdrivers/mac/chromedriver";
   		else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))  driverPath = "./resources/webdrivers/pc/chromedriver.exe";
   		else throw new IllegalArgumentException("Unknown OS");
   			System.setProperty("webdriver.chrome.driver", driverPath);
   			System.setProperty("webdriver.chrome.silentOutput", "true");
   			ChromeOptions option = new ChromeOptions();
   			option.addArguments("disable-infobars"); 
   			option.addArguments("--disable-notifications");
   			if (System.getProperty("os.name").toUpperCase().contains("MAC"))
   				option.addArguments("-start-fullscreen");
   			else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
   				option.addArguments("--start-maximized");
   			else throw new IllegalArgumentException("Unknown OS");
   			driver = new ChromeDriver(option);
   			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);     
              }
      
             else if (browser.equalsIgnoreCase("firefox")) {
         		String driverPath = "";
       	     if (System.getProperty("os.name").toUpperCase().contains("MAC"))     driverPath = "./resources/webdrivers/mac/geckodriver.sh";
       	else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) driverPath = "./resources/webdrivers/pc/geckodriver.exe";
       	else throw new IllegalArgumentException("Unknown OS");
       			
       			System.setProperty("webdriver.gecko.driver", driverPath);
       			driver = new FirefoxDriver();
       			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
       			driver.manage().window().maximize();
             }
              
             else if (browser.equalsIgnoreCase("safari")) {
         		if (!System.getProperty("os.name").contains("Mac")) {throw new IllegalArgumentException("Safari is available only on Mac");}
	        		driver = new SafariDriver(); 
	        		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	        		driver.manage().window().maximize();
             }
               else {driver = new HtmlUnitDriver();
                    ((HtmlUnitDriver) driver).setJavascriptEnabled(true);
                     driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);}
              driver.get(baseUrl + url);}
 
	public static boolean isPresent(String element_id, WebDriver wd) {
              driver = wd;
              if (driver.findElements(By.id(element_id)).size() > 0) {return true;}
             else {return false;}}
  
	public static String size(String element_id, WebDriver wd) {
              driver = wd;
              String n = null;
              if (!driver.findElements(By.id(element_id)).isEmpty()) {
                   String s = driver.findElement(By.id(element_id)).getSize().toString(); return s;}
             else {return n;}}
  
	public static String location(String element_id, WebDriver wd) {
              driver = wd;
              String n = null;
              if (!driver.findElements(By.id(element_id)).isEmpty()) {
                  String l = driver.findElement(By.id(element_id)).getLocation().toString(); return l;}
             else {return n;}}
}
 