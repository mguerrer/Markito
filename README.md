![Markito logo](/images/Markito-100.png)
# Markito v0.7
A Selenium/Appium Webdriver wrapper written in Java to make coding easier and readable.  This wrapper has been designed for scripting style of programming for beginners, using the ideas of Selenese. 
It is an implementation of the [Bot pattern](https://www.selenium.dev/documentation/test_practices/design_strategies/#bot-pattern)
## Sponsors
This project is tested with [BrowserStack](https://www.browserstack.com/)

![BrowserStack](/images/browserstacklogo.png)
## Features so far
1. Support for most common use case, test web app in Chrome, if you want others you can build a driver object and inject to Markito objects.
2. Written in Java 11, supporting Selenium 3.141.59, Appium 6.0.0.  Tested in Windows 10, Chrome browser, Android and iOS.
3. Inspired by Selenese concise grammar.
4. Garbage collection for browsers and webdrivers improving clean execution.
5. Colorful logging in console and element highlighting helps on rapid debug.  Both can be disabled for runtime.
6. Easy support for headless execution.
## Next steps
1. Add automated testings.
2. Refactoring to comply with OOD patterns.
3. Thread safe for parallel execution.
4. Update to latest versions of selenium, appium and other dependencies.
5. Browserstack native support.
6. Automated browser drivers download to accomodate to current installed versions.  Build on top of [WebDriverManager](https://bonigarcia.dev/webdrivermanager/#webdriver-builder).

## Hello world
With Markito you'll be writing code in seconds, creating a super readable code, making you productive and improving code maintainability. This is the way your Hello World code will look like this with Markito:
### Web App
<pre><code>
public void HelloWorldWebTest() {
&nbsp;&nbsp;&nbsp;&nbsp;OpenWebDriver();
&nbsp;&nbsp;&nbsp;&nbsp;Get("http://www.google.com");
&nbsp;&nbsp;&nbsp;&nbsp;SendKeys( By.name("q"), "Hello world!!");
&nbsp;&nbsp;&nbsp;&nbsp;Click( By.name("btnK"));
&nbsp;&nbsp;&nbsp;&nbsp;CloseWebDriver();
}
</code></pre>
### Mobile App
<pre><code> 
public void HelloWorldMobileTest() {
&nbsp;&nbsp;&nbsp;&nbsp;OpenAndroidDriver( new URL("http://hub.browserstack.com/wd/hub"), caps );
&nbsp;&nbsp;&nbsp;&nbsp;Click( MobileBy.AccessibilityId("Search Wikipedia"));
&nbsp;&nbsp;&nbsp;&nbsp;SendKeys( MobileBy.id("org.wikipedia.alpha:id/search_src_text"), "BrowserStack");
&nbsp;&nbsp;&nbsp;&nbsp;List<WebElement> allProductsName = FindElements( MobileBy.className("android.widget.TextView"));
&nbsp;&nbsp;&nbsp;&nbsp;assert(allProductsName.size() > 0);
&nbsp;&nbsp;&nbsp;&nbsp;for (WebElement webElement : allProductsName) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;printf("[%s]\n", webElement.getText());
&nbsp;&nbsp;&nbsp;&nbsp;}
&nbsp;&nbsp;&nbsp;&nbsp;CloseAndroidDriver();
}

</code></pre>
## Getting started
So far there is no packed distribution, however, you can clone this repository and build using Java 11, and Maven 3.6.3. to produce <code>markito-0.7.jar</code> file with <code>mvn package</code>.
<p>Create  <code>yourclass.java</code> file and add</p>
<pre><code>import org.openqa.selenium.*;</code></pre>
Now make your class to extend Markito as
<pre><code>public class HelloWorld extends MarkitoWeb</code></pre> or
<pre><code>public class HelloWorld extends MarkitoAndroid</code></pre> or
<pre><code>public class HelloWorld extends MarkitoiOS</code></pre> 
You are ready to start coding.

## References
1. [DriverManager library](https://bonigarcia.dev/webdrivermanager/): Provides automated support to manage webdriver's browser drivers.
2. [Writing your first test with Appium in Browserstack](https://github.com/browserstack/java-appium-app-browserstack/tree/master/java_8)

## Contributing
This is a Work In Progress, hence it could be nice that you can use and test, check compability on OSs other than Windows, add unit tests, etc.  Please feel free to add issues and PRs on contributions.   For further contact please write to [marcosguerrerow@hotmail.com](mailto:marcosguerrerow@hotmail.com).   Prior to add new issues consider the following criteria:

1. The idea is to maintain Markito as simple as possible in terms of available methods, for complex interactions user can use the exposed <code>WebDriver driver</code> property.
2. Most of major web browsers are now based on Chromium, so multi-browser support will not be provided on COTS basis.
3. 
