![Markito logo](/images/Markito-100.png)
# Markito v0.7 (Work in progress towards v1.0)
A Selenium/Appium Webdriver wrapper written in Java to:
### 1. Support web, native and hybrid app automation on different platforms.
   * Automator can run SAME script for WEB App on browsers (Chrome, Firefox, Edge, IE, Safari) running over Windows, Linux, Android and iOS. _Implemented in v0.7_
   * Automator can run SAME script for NATIVE (or HYBRYD) App for Windows, Android and iOS. _v0.8_

### 2. Make coding simple, maintainable, concise and readable.  _Implemented in v0.7_
   * Automator can write test cases using script style in the form [command, element] 
   * Automator can use a simplified timeout management. 
   * Automator can use extensions to webdriver and appium driver. 

>>> Access shadow elements

### 3. Improved maintenance and usage through:
   * **Isolation of test code** from changes on Java, Selenium and Appium frameworks providing an extra layer of abstraction.
   * **Automatic drivers update** and download during local testing. _Implemented in v0.7_
   * **Easy debug**: Colorful text logging in console and highlighting for web elements helps on rapid debug.  Both can be disabled for runtime. _Implemented in v0.7_
   * **Flakyness reduction** with multiple locators and retry disciplines. _v1.0_
   * Garbage collection and thread safety. _v1.0_
   * **Native support for cloud browsers and devices providers**:
      * Browserstack support. _Implemented in v0.7_
      * Saucelabs support. (TBD)
      * Others

For more details please read [Markito wiki](https://github.com/mguerrer/Markito/wiki)

## Sponsors
This project is tested using [BrowserStack](https://www.browserstack.com/) on Web and Mobile automation.

![BrowserStack](/images/browserstacklogo.png)

## Documentation
For more details on adopting and using Markito on your own automation please refer to our wiki pages:
   * [Markito Home page](https://github.com/mguerrer/Markito/wiki/Markito-Home): Overall details on the project and roadmap.
   * [Getting started](https://github.com/mguerrer/Markito/wiki/Getting-started-with-Markito): Details on how to configure your environment to start test and get fun in minutes.
   * [Samples]():
 
      * [Testing a web app with Markito and BrowserStack](https://github.com/mguerrer/Markito/wiki/Testing-a-web-app-with-Markito-and-BrowserStack): See how to test a web app multi-browser on different desktop and Mobile platforms with Markito.

## Hello world
With Markito you'll be writing code in minutes, creating a super readable code, making you productive and improving code maintainability. This is the way your Hello World code will look like this with Markito:
### Web App testing
<pre><code>
public void HelloWorldWebTest() {
&nbsp;&nbsp;&nbsp;&nbsp;setBrowserstackProjectInformation("Markito", "MultiBrowserPlatformTests",
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "Google Search-Chrome"+"-"+LOCAL_COMPUTER_DEVICE.getName());
&nbsp;&nbsp;&nbsp;&nbsp;setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
&nbsp;&nbsp;&nbsp;&nbsp;setDriver(openBrowserSessionInDevice(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE)); // Open web session on device
&nbsp;&nbsp;&nbsp;&nbsp;get( "http://www.google.com" );
&nbsp;&nbsp;&nbsp;&nbsp;sendKeys( By.name("q"), "Hello world!!");
&nbsp;&nbsp;&nbsp;&nbsp;click( By.name("btnK"));
&nbsp;&nbsp;&nbsp;&nbsp;closeWebSessionInDevice();
}
</code></pre>

For more examples please refer to Documentation page on section Examples.

## Contributing
This is a Work In Progress, hence it could be nice that you can use and test, check compability on OSs other than Windows, add unit tests, etc.  Please feel free to add issues and PRs on contributions.   For further contact please write to [marcosguerrerow@hotmail.com](mailto:marcosguerrerow@hotmail.com).   Prior to add new issues consider the following criteria:

1. The idea is to maintain Markito as simple as possible in terms of available methods, for complex interactions user can use the exposed <code>WebDriver getDriver()</code> method and use webdriver/appium as usual.
2. Most of major web browsers are now based on Chromium, so multi-browser support will not be provided on COTS basis.