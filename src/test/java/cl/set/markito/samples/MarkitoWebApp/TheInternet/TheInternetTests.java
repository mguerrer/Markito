/**
 * Tests different methods.
 */

package cl.set.markito.samples.MarkitoWebApp.TheInternet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;

import cl.set.markito.framework.MarkitoWebApp;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.DragAndDropPage;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.HomePage;

public class TheInternetTests extends MarkitoWebApp{
    HomePage homePage = null;
    @BeforeEach
    public void Setup() throws Exception {
        // Arrange
        setBrowserstackProjectInformation("Markito", "MultiBrowserPlatformTests", 
                                            "Google Search-Chrome"+"-"+LOCAL_COMPUTER_DEVICE.getName());
        setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
        setDriver(openBrowserSessionInDevice(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE)); // Open web session on device

        // get URL
        getDriver().get("http://the-internet.herokuapp.com/");
        // Open Home Page
        homePage = new HomePage(getDriver());
        PageFactory.initElements(getDriver(), homePage);
    }

    @AfterEach
    public void tearDown() throws Exception {
       closeWebSessionInDevice();
    }

    @Test
    public void DragAndDropTest() throws Exception {
        // Open link Drag And Drop
        homePage.clickDragAndDropLink();

        DragAndDropPage dragAndDropPage = new DragAndDropPage(getDriver());
        dragAndDropPage.verifyPageLoaded();

        // Act & Assert
        // Test drag and drop
        for (int i = 0; i < 3; i++) {
            dragAndDropPage.DragObjectAOverObjectB();
            Thread.sleep(100);
            String header = dragAndDropPage.squareA.getText();
            if ((i % 2) == 0) { // Assert that header has changed after drag and drop.
                assertEquals(header, "B");
            } else {
                assertEquals(header, "A");
            }
        }

    }
}
