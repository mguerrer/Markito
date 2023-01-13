package cl.set.markito.framework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public interface MarkitoGenericWebElement extends WebElement{

    @Override
    default void clear() {
        // TODO Auto-generated method stub
        
    }

    @Override
    default void click() {
        // TODO Auto-generated method stub
        
    }

    @Override
    default WebElement findElement(By by) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default List<WebElement> findElements(By by) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default String getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default String getCssValue(String propertyName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default Point getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default Rectangle getRect() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default Dimension getSize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default String getTagName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default String getText() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    default boolean isDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default boolean isSelected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    default void sendKeys(CharSequence... keysToSend) {
        // TODO Auto-generated method stub
        
    }

    @Override
    default void submit() {
        // TODO Auto-generated method stub
        
    }

    @Override
    default <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
