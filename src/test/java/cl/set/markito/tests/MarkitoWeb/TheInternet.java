/**
 * Experimental Thread safe web test.
 */

package cl.set.markito.tests.MarkitoWeb;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import cl.set.markito.MarkitoWeb;
import cl.set.markito.tests.MarkitoWeb.framework.BrowserManager;
import cl.set.markito.tests.MarkitoWeb.framework.TheInternet.pages.DragAndDrop;
import cl.set.markito.tests.MarkitoWeb.framework.TheInternet.pages.Home;

public class TheInternet {
    protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<WebDriver>();


    @BeforeEach
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


    @AfterEach
    public void tearDown(){
        getDriver().quit();

        System.out.println("After Test Thread ID: "+Thread.currentThread().getId());

        threadLocalDriver.remove();
    }
    @Test
    public void HelloWorldTest() throws Exception {
        //Setup();

        // Arrange
        // Open Home Page
        Home homePage = new Home(getDriver());
        PageFactory.initElements(getDriver(), homePage);
        // Open link Drag And Drop
        homePage.clickDragAndDropLink();

        DragAndDrop dragAndDropPage = new DragAndDrop(getDriver());
        dragAndDropPage.verifyPageLoaded();
        
        // Test drag and drop
        for (int i=0; i<10; i++) {
            dragAndDropPage.DragObjectAOverObjectB();
            Thread.sleep(1000);
        }

        //tearDown();

    }
}
