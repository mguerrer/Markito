package cl.set.markito.framework;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.openqa.selenium.By;
import org.openqa.selenium.DeviceRotation;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.Response;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.appmanagement.ApplicationState;

/**
 * Generic interface to drive browsers in desktop and mobile devices.
 * Marcos Guerrero: 12-01-2023
 */
public interface MarkitoGenericWebDriver extends MobileDriver<WebElement>, MarkitoGenericWebElement {

    @Override
    default WebElement findElement(By by) {
        return null;
    }

    @Override
    default WebElement findElementByClassName(String className) {
        return null;
    }

    @Override
    default WebElement findElementByCssSelector(String cssSelector) {
        return null;
    }

    @Override
    default WebElement findElementById(String id) {
        return null;
    }

    @Override
    default WebElement findElementByLinkText(String linkText) {
        return null;
    }

    @Override
    default WebElement findElementByName(String name) {
        return null;
    }

    @Override
    default WebElement findElementByPartialLinkText(String partialLinkText) {
        return null;
    }

    @Override
    default WebElement findElementByTagName(String tagName) {
        return null;
    }

    @Override
    default WebElement findElementByXPath(String xPath) {
        return null;
    }

    @Override
    default List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    default List<WebElement> findElementsByClassName(String className) {
        return null;
    }

    @Override
    default List<WebElement> findElementsByCssSelector(String cssSelector) {
        return null;
    }

    @Override
    default List<WebElement> findElementsById(String id) {
        return null;
    }

    @Override
    default List<WebElement> findElementsByLinkText(String linkText) {
        return null;
    }

    @Override
    default List<WebElement> findElementsByName(String name) {
        return null;
    }

    @Override
    default List<WebElement> findElementsByPartialLinkText(String partialLinkText) {
        return null;
    }

    @Override
    default List<WebElement> findElementsByTagName(String tagName) {
        return null;
    }

    @Override
    default List<WebElement> findElementsByXPath(String xPath) {
        return null;
    }

    @Override
    default void close() {
        
    }

    @Override
    default void get(String url) {
        
    }

    @Override
    default String getCurrentUrl() {
        return null;
    }

    @Override
    default String getPageSource() {
        return null;
    }

    @Override
    default String getTitle() {
        return null;
    }

    @Override
    default String getWindowHandle() {
        return null;
    }

    @Override
    default Set<String> getWindowHandles() {
        return null;
    }

    @Override
    default Options manage() {
        return null;
    }

    @Override
    default Navigation navigate() {
        return null;
    }

    @Override
    default void quit() {
        
    }

    @Override
    default TargetLocator switchTo() {
        return null;
    }

    @Override
    default void performMultiTouchAction(MultiTouchAction multiAction) {
        MobileDriver.super.performMultiTouchAction(multiAction);
    }

    @Override
    default Response execute(String driverCommand) {
        return null;
    }

    @Override
    default Response execute(String driverCommand, Map<String, ?> parameters) {
        return null;
    }

    @Override
    default WebDriver context(String name) {
        return null;
    }

    @Override
    default String getContext() {
        return null;
    }

    @Override
    default Set<String> getContextHandles() {
        return null;
    }

    @Override
    default ScreenOrientation getOrientation() {
        return null;
    }

    @Override
    default void rotate(ScreenOrientation orientation) {
        
    }

    @Override
    default void rotate(DeviceRotation rotation) {
        
    }

    @Override
    default DeviceRotation rotation() {
        return null;
    }

    @Override
    default WebElement findElementByAccessibilityId(String using) {
        return MobileDriver.super.findElementByAccessibilityId(using);
    }

    @Override
    default List<WebElement> findElementsByAccessibilityId(String using) {
        return MobileDriver.super.findElementsByAccessibilityId(using);
    }

    @Override
    default WebElement findElement(String by, String using) {
        return null;
    }

    @Override
    default List<WebElement> findElements(String by, String using) {
        return null;
    }

    @Override
    default Location location() {
        return null;
    }

    @Override
    default void setLocation(Location location) {
        
    }

    @Override
    default void hideKeyboard() {
        MobileDriver.super.hideKeyboard();
    }

    @Override
    default String getDeviceTime() {
        return MobileDriver.super.getDeviceTime();
    }

    @Override
    default byte[] pullFile(String remotePath) {
        return MobileDriver.super.pullFile(remotePath);
    }

    @Override
    default byte[] pullFolder(String remotePath) {
        return MobileDriver.super.pullFolder(remotePath);
    }

    @Override
    default void activateApp(String bundleId) {
        MobileDriver.super.activateApp(bundleId);
    }

    @Override
    default void closeApp() {
        MobileDriver.super.closeApp();
    }

    @Override
    default void installApp(String appPath) {
        MobileDriver.super.installApp(appPath);
    }

    @Override
    default boolean isAppInstalled(String bundleId) {
        return MobileDriver.super.isAppInstalled(bundleId);
    }

    @Override
    default void launchApp() {
        MobileDriver.super.launchApp();
    }

    @Override
    default ApplicationState queryAppState(String bundleId) {
        return MobileDriver.super.queryAppState(bundleId);
    }

    @Override
    default boolean removeApp(String bundleId) {
        return MobileDriver.super.removeApp(bundleId);
    }

    @Override
    default void resetApp() {
        MobileDriver.super.resetApp();
    }

    @Override
    default void runAppInBackground(Duration duration) {
        MobileDriver.super.runAppInBackground(duration);
    }

    @Override
    default boolean terminateApp(String bundleId) {
        return MobileDriver.super.terminateApp(bundleId);
    }

    @Override
    default Map<String, String> getAppStringMap() {
        return MobileDriver.super.getAppStringMap();
    }

    @Override
    default Map<String, String> getAppStringMap(String language) {
        return MobileDriver.super.getAppStringMap(language);
    }

    @Override
    default Map<String, String> getAppStringMap(String language, String stringFile) {
        return MobileDriver.super.getAppStringMap(language, stringFile);
    }

    @Override
    default @Nullable String getAutomationName() {
        return MobileDriver.super.getAutomationName();
    }

    @Override
    default @Nullable String getPlatformName() {
        return MobileDriver.super.getPlatformName();
    }

    @Override
    default @Nullable Object getSessionDetail(String detail) {
        return MobileDriver.super.getSessionDetail(detail);
    }

    @Override
    default Map<String, Object> getSessionDetails() {
        return MobileDriver.super.getSessionDetails();
    }

    @Override
    default boolean isBrowser() {
        return MobileDriver.super.isBrowser();
    }

    Object clone() throws CloneNotSupportedException;

    @Override
    boolean equals(Object obj);

    void finalize() throws Throwable;

    @Override
    int hashCode();

    @Override
    String toString();

    @Override
    default void clear() {
        // TODO Auto-generated method stub
        MarkitoGenericWebElement.super.clear();
    }

    @Override
    default void click() {
        // TODO Auto-generated method stub
        MarkitoGenericWebElement.super.click();
    }

    @Override
    default String getAttribute(String name) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getAttribute(name);
    }

    @Override
    default String getCssValue(String propertyName) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getCssValue(propertyName);
    }

    @Override
    default Point getLocation() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getLocation();
    }

    @Override
    default Rectangle getRect() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getRect();
    }

    @Override
    default <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getScreenshotAs(target);
    }

    @Override
    default Dimension getSize() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getSize();
    }

    @Override
    default String getTagName() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getTagName();
    }

    @Override
    default String getText() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.getText();
    }

    @Override
    default boolean isDisplayed() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.isDisplayed();
    }

    @Override
    default boolean isEnabled() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.isEnabled();
    }

    @Override
    default boolean isSelected() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebElement.super.isSelected();
    }

    @Override
    default void sendKeys(CharSequence... keysToSend) {
        MarkitoGenericWebElement.super.sendKeys(keysToSend);
    }

    @Override
    default void submit() {
        // TODO Auto-generated method stub
        MarkitoGenericWebElement.super.submit();
    }

  }
