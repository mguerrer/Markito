![Markito logo](/images/Markito-100.png)
# Markito v0.7
A Selenium/Appium Webdriver wrapper written in Java to make coding easier and readable.  This wrapper has been designed for scripting style of programming for beginners, using the ideas of Selenese.
## Sponsors
![BrowserStack](/images/browserstacklogo.png)
## Features so far
1. Support for most common use cases.
2. Written in Java 11, supporting Selenium 3.141.59, Appium 6.0.0.  Tested in Windows 10, Chrome browser, Android and iOS.
3. Inspired by Selenese.
4. Adds garbage collection for browsers and webdrivers improving clean execution.
## Next steps
1. Add automated testings.
2. Refactoring to comply with OOD patterns like SOLID.
3. Adds thread safe for parallel execution.
4. Update to latest versions of selenium, appium and other dependencies.

## Hello world
With Markito you'll be writing code in seconds, creating a super readable code, making you productive and improving code maintainability. This is the way your Hello World code will look like this with Markito:
### Web
documentation<pre><code>
public void HelloWorldTest() {
&nbsp;&nbsp;&nbsp;&nbsp;OpenWebDriver();
&nbsp;&nbsp;&nbsp;&nbsp;Get("http://www.google.com");
&nbsp;&nbsp;&nbsp;&nbsp;SendKeys( By.name("q"), "Hello world!!");
&nbsp;&nbsp;&nbsp;&nbsp;Click( By.name("btnK"));
&nbsp;&nbsp;&nbsp;&nbsp;CloseWebDriver();
}
</code></pre>
### Mobile
<pre><code> 
&nbsp;&nbsp;&nbsp;&nbsp;OpenAndroidDriver( new URL("http://hub.browserstack.com/wd/hub"), caps );
&nbsp;&nbsp;&nbsp;&nbsp;Click( MobileBy.AccessibilityId("Search Wikipedia"));
&nbsp;&nbsp;&nbsp;&nbsp;SendKeys( MobileBy.id("org.wikipedia.alpha:id/search_src_text"), "BrowserStack");
&nbsp;&nbsp;&nbsp;&nbsp;List<WebElement> allProductsName = FindElements( MobileBy.className("android.widget.TextView"));
&nbsp;&nbsp;&nbsp;&nbsp;assert(allProductsName.size() > 0);
&nbsp;&nbsp;&nbsp;&nbsp;for (WebElement webElement : allProductsName) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;printf("[%s]\n", webElement.getText());
&nbsp;&nbsp;&nbsp;&nbsp;}
&nbsp;&nbsp;&nbsp;&nbsp;CloseAndroidDriver();
</code></pre>
## Getting started
So far there is no packed distribution, however, you can clone this repository and build using Java 15, and Maven 3.6.3. to produce <code>markito-0.2.jar</code> file with <code>mvn package</code>.
<p>Create  <code>yourclass.java</code> file and add</p>
<pre><code>import org.openqa.selenium.*;</code></pre>
Now make your class to extend Markito as
<pre><code>public class HelloWorld extends MarkitoWeb</code></pre> or
<pre><code>public class HelloWorld extends MarkitoAndroid</code></pre> or
<pre><code>public class HelloWorld extends MarkitoiOS</code></pre> 
You are ready to start coding.

## References
1. [DriverManager library](https://bonigarcia.dev/webdrivermanager/): Provides automated support to manage webdriver's browser drivers.

## Contributing
This is a Work In Progress, hence it could be nice that you can use and test, check compability on OSs other than Windows, add unit tests, etc.  Please feel free to add issues and PRs on contributions.   For further contact please write to [marcosguerrerow@hotmail.com](mailto:marcosguerrerow@hotmail.com).   Prior to add new issues consider the following criteria:

1. The idea is to maintain Markito as simple posible in terms of available methods, for complex interactions user can use the exposed <code>WebDriver driver</code> property.
2. Most of major web browsers are now based on Chromium, so multi-browser support will not be provided on COTS basis.
3. Easy support for headless will be part of Markito.
