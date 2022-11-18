package cl.set.markito.tests.MarkitoWeb.framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserManager {
 
    public static WebDriver doBrowserSetup(String browserName){
 
         WebDriver driver = null;
         if (browserName.equalsIgnoreCase("chrome")){
             //steup chrome browser
             WebDriverManager.chromedriver().setup();
 
 
             //Add options for --headed or --headless browser launch
             ChromeOptions chromeOptions = new ChromeOptions();
             chromeOptions.addArguments("-headed");
             
             //initialize driver for chrome
             driver = new ChromeDriver(chromeOptions);
 
             //maximize window
             driver.manage().window().maximize();
 
             //add implicit timeout
            //driver.manage().timeouts().implicitlyWait(30, Duration.ofSeconds(0));
 
        }
        return driver;
    }
 }
