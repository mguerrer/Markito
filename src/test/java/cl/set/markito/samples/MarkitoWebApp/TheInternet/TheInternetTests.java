/**
 * Tests different methods.
 */

package cl.set.markito.samples.MarkitoWebApp.TheInternet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.Point;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.PageFactory;

import cl.set.markito.framework.MarkitoWebApp;
import cl.set.markito.framework.devices.Device;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.AddRemoveElementsPage;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.DragAndDropPage;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.pages.HomePage;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Test The Internet web page")
public class TheInternetTests extends MarkitoWebApp {
    HomePage homePage = null;
    String hostUrl = "http://the-internet.herokuapp.com";
    Device device = WINDOWS11_COMPUTER_DEVICE;

    @BeforeAll
    public void beforeAll() throws Exception {

    }

    public void setup(String testName) throws Exception {
        // Arrange overall session
        setBrowserstackProjectInformation("Markito", "Markito " + getMarkitoVersion(),
                "The Internet-" + testName + CHROME_BROWSER.getName() + "-" +  device.getName());
        setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
        setDriver(openBrowserSessionInDevice(CHROME_BROWSER, device)); // Open web session on device
        // get URL
        // Open Home Page
        homePage = new HomePage(getDriver(), hostUrl);
        PageFactory.initElements(getDriver(), homePage);
        maximize();
        println("\n"+ANSI_GREEN+testName);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeWebSessionInDevice();
    }

    @Test
    @DisplayName("Test The Internet web page->Add/Remove Elements")
    public void AddRemoveElementsTest(TestInfo testInfo) throws Exception {
        setup(testInfo.getDisplayName());
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
    @DisplayName("Test The Internet web page->Drag and Drop")
    public void DragAndDropTest(TestInfo testInfo) throws Exception {
        setup(testInfo.getDisplayName());
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
                Assertions.assertEquals(header, "B");
            } else {
                Assertions.assertEquals(header, "A");
            }
        }
        Assertions.assertTrue(true);
    }
    @Test
    @DisplayName("Test Windows management interface")
    public void WindowsManagementTest(TestInfo testInfo) throws Exception {
        setup(testInfo.getDisplayName());
        setWindowSize( new Dimension(300,300));
        setPosition(new Point(100, 100));
        Point currentPosition = getPosition();
        maximize(); // Tests zooming
        setZoomLevelToPercentage(30);
        setZoomLevelToPercentage(100);
        setZoomLevelToPercentage(200);
        setZoomLevelToPercentage(30);
        
        setPosition( currentPosition);

        Assertions.assertEquals( currentPosition,  getPosition());
        fullscreen();
    }
}
