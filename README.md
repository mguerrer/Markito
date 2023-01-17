![Markito logo](/images/Markito-100.png)
# Markito v0.7 (Work in progress towards v1.0)
A Selenium/Appium Webdriver wrapper written in Java to make easiest:
1. Support web, native and hybrid app automation on different platforms.
   * Automator can run SAME script for WEB App on browsers (Chrome, Firefox, Edge, IE, Safari) running over Windows, Linux, Android and iOS.
   * Automator can run SAME script for NATIVE (or HYBRYD) App for Windows, Android and iOS.
2. Make coding simple, concise and readable.
   *  This wrapper has been designed to extend script style using [Selenese](https://ui.vision/rpa/docs/selenium-ide) alike commands. Similar ideas can be found on framework [Helium](https://github.com/mherrmann/selenium-python-helium)
   *  Simplified timeout management.
      * W3C webdriver standard provides timeout management at session level, page level, JS script level and element level (implicit and explicit). Markito uses a single implicit timeout parameter to set all levels.
3. Provide an extra layer of abstraction over webdriver and appium (or others in the future) to isolate your test code from changes on those frameworks.
4. Improved maintenance and usage through:
   * Automatic drivers update and download.
   * Flakyness reduction with multiple locators and retry disciplines.
   * Garbage collection and thread safety.

It is an implementation of the [Bot pattern](https://www.selenium.dev/documentation/test_practices/design_strategies/#bot-pattern) Inspired by Selenese concise grammar and written in Java 11, using Selenium Java client 3.141.59, and Appium client 6.0.0.  
Testing has been made in local Windows 10/Ubuntu and Chrome browser, also Android and iOS by using BrowserStack.

## Sponsors
This project is tested using [BrowserStack](https://www.browserstack.com/) on Web and Mobile automation.

![BrowserStack](/images/browserstacklogo.png)

## Why Markito?
Using a high level programming language to support scripting can be a good idea to provide easy support for complex interactions (e.g. access a DB or interact with OS), however this can produce obscure and hard to maintain test code because the difficulty to read and understand.  This also can cause the need for 'expert programmers only' to addapt or extend.  
In the case of Java (one the most used OOP languages) to automate using webdriver/appium frameworks (most used), the client implementation makes it hard to learn for testers that are used with tools with scripting/codeless style, that is how test code should be written, ideally with near zero complexity. Even in the case of Java trained programmers setup and start programming can be challenging because:
 * Selenium/Appium documentation is incomplete/outdated, and given the different versions of each framework, examples in Internet can be wrong for current version.
 * Java client has been written using OOP principles, making the programmer to write too much text to express the commands. Examples includes:
   *  driver.manage().wait( seconds );  // wait( seconds );
   *  driver.manage().window().maximize(); // maximize();
   *  element.findElement(By.cssSelector("...")).click(); // click( element );

## Features so far
1. Test use cases:
    * TUC01- Test web app in Chrome, Edge, Firefox, IE in local (Windows and Linux).

2. MarkitoDriver factory provides an ease to use API to build and manage sessions for the most common scenarios.  if you want some other you can build a driver object and inject to Markito* API objects.

5. Colorful text logging in console and highlighting for web elements helps on rapid debug.  Both can be disabled for runtime.
6. Easy support for headless execution.
## Next steps
1. Test use cases:
    * TUC02- Test web app in Chrome, Edge, Firefox, IE in remote (BrowserStack).
    * TUC03- Test web app in Chrome in Android and iOS devices.
    * TUC04- Test native mobile app in Android and iOS.
    * TUC05- Test native desktop app in Windows
1. Add automated testings.
2. Refactoring to comply with OOD patterns.
3. Thread safe for parallel execution.
4. Update to latest versions of selenium, appium and other dependencies.
5. Garbage collection for browsers and webdrivers improving clean execution.
6. Browserstack native support.
7. Automated browser drivers download to accomodate to current installed versions.  Build on top of [WebDriverManager](https://bonigarcia.dev/webdrivermanager/#webdriver-builder).

## Hello world
With Markito you'll be writing code in minutes, creating a super readable code, making you productive and improving code maintainability. This is the way your Hello World code will look like this with Markito:
### Web App
<pre><code>
public void HelloWorldWebTest() {
&nbsp;&nbsp;&nbsp;&nbsp;setBrowserstackProjectInformation("Markito", "MultiBrowserPlatformTests",
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Google Search-Chrome"+"-"+LOCAL_COMPUTER_DEVICE.getName());
&nbsp;&nbsp;&nbsp;&nbsp;setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
&nbsp;&nbsp;&nbsp;&nbsp;setDriver(openBrowserSessionInDevice(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE)); // Open web session on device
&nbsp;&nbsp;&nbsp;&nbsp;get("http://www.google.com");
&nbsp;&nbsp;&nbsp;&nbsp;sendKeys( By.name("q"), "Hello world!!");
&nbsp;&nbsp;&nbsp;&nbsp;click( By.name("btnK"));
&nbsp;&nbsp;&nbsp;&nbsp;closeWebSessionInDevice();
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
<pre><code>public class HelloWorld extends MarkitoMobile</code></pre> or
You are ready to start coding.

## References
1. [DriverManager library](https://bonigarcia.dev/webdrivermanager/): Provides automated support to manage webdriver's browser drivers.
2. [Writing your first test with Appium in Browserstack](https://github.com/browserstack/java-appium-app-browserstack/tree/master/java_8)

## Contributing
This is a Work In Progress, hence it could be nice that you can use and test, check compability on OSs other than Windows, add unit tests, etc.  Please feel free to add issues and PRs on contributions.   For further contact please write to [marcosguerrerow@hotmail.com](mailto:marcosguerrerow@hotmail.com).   Prior to add new issues consider the following criteria:

1. The idea is to maintain Markito as simple as possible in terms of available methods, for complex interactions user can use the exposed <code>WebDriver getDriver()</code> method and use webdriver/appium as usual.
2. Most of major web browsers are now based on Chromium, so multi-browser support will not be provided on COTS basis.