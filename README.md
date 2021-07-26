![Markito logo](/images/Markito-100.png)
# Markito v0.6
A Selenium/Appium Webdriver wrapper written in Java to make coding easier and readable.
## Features so far
1. Support for most common use cases.
2. Written in Java, supporting Selenium 3.141.59, Appium 6.0.0.  Tested in Windows 10, Chrome browser, Android and iOS.
3. Inspired by Selenese commands style.
4. Adds garbage collection for browsers and webdrivers improving clean execution.
## Sponsors
![BrowserStack](/images/browserstacklogo.png)
## Hello world
With Markito you'll be writing code in seconds, creating a super readable code, making you productive and improving code maintainability. This is the way your Hello World code will look like this with Markito:
### Web
documentation<pre><code>
public void HelloWorldTest() {
    OpenWebDriver();
    Get("http://www.google.com");
    SendKeys( By.name("q"), "Hello world!!");
    Click( By.name("btnK"));
    CloseWebDriver();
}
</code></pre>
### Mobile
<pre><code> 
    OpenAndroidDriver( new URL("http://hub.browserstack.com/wd/hub"), caps );
    Click( MobileBy.AccessibilityId("Search Wikipedia"));
    SendKeys( MobileBy.id("org.wikipedia.alpha:id/search_src_text"), "BrowserStack");
    List<WebElement> allProductsName = FindElements( MobileBy.className("android.widget.TextView"));
    assert(allProductsName.size() > 0);
    for (WebElement webElement : allProductsName) {
        printf("[%s]\n", webElement.getText());
    }
    CloseAndroidDriver();
</code></pre>
## Getting started
So far there is no packed distribution, however, you can clone this repository and build using Java 15, and Maven 3.6.3. to produce <code>markito-0.2.jar</code> file with <code>mvn package</code>.
<p>Create  <code>yourclass.java</code> file and add</p>
<pre><code>import org.openqa.selenium.*;</code></pre>
Now make your class to extend Markito as
<pre><code>public class HelloWorld extends MarkitoWeb</code></pre> or
<pre><code>public class HelloWorld extends MarkitoAndroid</code></pre> or
<pre><code>public class HelloWorld extends MarkitoiOS</code></pre> 
You are ready to start coding!!.

## Contributing
This is a Work In Progress, hence it could be nice that you can use and test, check compability on OSs other than Windows, add unit tests, etc.  Please feel free to add issues and PRs on contributions.   For further contact please write to [marcosguerrerow@hotmail.com](mailto:marcosguerrerow@hotmail.com).   Prior to add new issues consider the following criteria:

1. The idea is to maintain Markito as simple posible in terms of available methods, for complex interactions user can use the exposed <code>WebDriver driver</code> property.
2. Most of major web browsers are now based on Chromium, so multi-browser support will not be provided on COTS basis.
3. Easy support for headless will be part of Markito.