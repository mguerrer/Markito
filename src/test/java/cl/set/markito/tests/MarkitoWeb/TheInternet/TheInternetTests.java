/**
 * Experimental Thread safe web test.
 */

package cl.set.markito.tests.MarkitoWeb.TheInternet;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

import cl.set.markito.tests.MarkitoWeb.TheInternet.pages.DragAndDrop;
import cl.set.markito.tests.MarkitoWeb.TheInternet.pages.Home;
import cl.set.markito.tests.MarkitoWeb.framework.BrowserManager;

public class TheInternetTests {
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
    public void DragAndDropTest() throws Exception {

        // Arrange
        // Open Home Page
        Home homePage = new Home(getDriver());
        PageFactory.initElements(getDriver(), homePage);
        // Open link Drag And Drop
        homePage.clickDragAndDropLink();

        DragAndDrop dragAndDropPage = new DragAndDrop(getDriver());
        dragAndDropPage.verifyPageLoaded();
        
        // Act & Assert
        // Test drag and drop
        for (int i=0; i<10; i++) {
            dragAndDropPage.DragObjectAOverObjectB();
            Thread.sleep(100);
            String header = dragAndDropPage.squareA.getText();
            if ( (i % 2) == 0 ) { // Assert that header has changed after drag and drop.
                assertEquals( header, "B" );
            } else {
                assertEquals( header, "A" );
            }
        }


    }
}
