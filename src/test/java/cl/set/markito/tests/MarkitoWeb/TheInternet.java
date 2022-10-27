/**
 * Experimental Thread safe web test.
 */

package cl.set.markito.tests.MarkitoWeb;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import cl.set.markito.MarkitoWeb;
import cl.set.markito.tests.MarkitoWeb.framework.BrowserManager;
import cl.set.markito.tests.MarkitoWeb.framework.TheInternet.pages.DragAndDrop;
import cl.set.markito.tests.MarkitoWeb.framework.TheInternet.pages.Home;

public class TheInternet {
    protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<WebDriver>();


    //@BeforeTest
    public void Setup(){
        WebDriver driver = BrowserManager.doBrowserSetup("chrome");
        //set driver
        threadLocalDriver.set(driver);

        System.out.println("Before Test Thread ID: "+Thread.currentThread().getId());

        //get URL
        getDriver().get("http://the-internet.herokuapp.com/");
    }

    //get thread-safe driver
    public static WebDriver getDriver(){
        return threadLocalDriver.get();
    }


    //@AfterTest
    public void tearDown(){
        getDriver().quit();

        System.out.println("After Test Thread ID: "+Thread.currentThread().getId());

        threadLocalDriver.remove();
    }
    @Test
    public void HelloWorldTest() throws Exception {
        Setup();

        // Arrange
        // Open Home Page
        Home homePage = new Home(getDriver());
        PageFactory.initElements(getDriver(), homePage);
        // Open link Drag And Drop
        homePage.clickDragAndDropLink();
        // 
        DragAndDrop dragAndDropPage = new DragAndDrop(getDriver());
        dragAndDropPage.verifyPageLoaded();

        tearDown();

    }
}
