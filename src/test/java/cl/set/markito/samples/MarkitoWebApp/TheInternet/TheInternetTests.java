/**
 * Tests different methods.
 */

package cl.set.markito.samples.MarkitoWebApp.TheInternet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.support.PageFactory;

import cl.set.markito.framework.MarkitoWebApp;
import cl.set.markito.framework.devices.Device;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.AddRemoveElementsPage;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.DragAndDropPage;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.HomePage;

@TestInstance(Lifecycle.PER_CLASS)
public class TheInternetTests extends MarkitoWebApp {
    HomePage homePage = null;
    String hostUrl = "http://the-internet.herokuapp.com";
    Device device = LOCAL_COMPUTER_DEVICE;

    @BeforeAll
    public void beforeAll() throws Exception {

    }

    public void setup(String testName) throws Exception {
        // Arrange overall session
        setBrowserstackProjectInformation("Markito", "Markito " + getMarkitoVersion(),
                "The Internet" + testName + CHROME_BROWSER.getName() + "-" +  device.getName());
        setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
        setDriver(openBrowserSessionInDevice(CHROME_BROWSER, device)); // Open web session on device
        // get URL
        // Open Home Page
        homePage = new HomePage(getDriver(), hostUrl);
        PageFactory.initElements(getDriver(), homePage);
    }

    @AfterAll
    public void tearDown() throws Exception {
        closeWebSessionInDevice();
    }

    @Test
    public void AddRemoveElementsTest() throws Exception {
        setup("AddRemoveElementsTest");
        // Open AddRemoveElements Page
        homePage.clickAddremoveElementsLink();

        AddRemoveElementsPage addRemovePage = new AddRemoveElementsPage(getDriver(), hostUrl);
        PageFactory.initElements(getDriver(), addRemovePage);
        addRemovePage.verifyPageUrl();

        addRemovePage.clickAddElementButton();
        waitForElementVisible(addRemovePage.deleteElement);
        addRemovePage.clickDeleteButton();
        // Assert element is invisible
        waitForElementInvisibility(addRemovePage.deleteElement);
        Assertions.assertTrue(true);
    }

    @Test
    public void DragAndDropTest() throws Exception {
        setup("DragAndDropTest");
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
