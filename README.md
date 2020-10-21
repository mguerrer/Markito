# Markito
A Webdriver wrapper written in Java to make coding easier and readable.
## Features
1. Support for most common use cases.
2. Written in Java, supporting Windows and Chrome browser.
3. Inspired by Selenese.
4. Adds garbage collection for browsers and webdrivers improving clean execution.
## Hello world
With Markito you'll be writing code in seconds, creating a super readable code, making you productive and improving code maintainability. This is the way your Hello World code will look like with Markito:
<pre><code>    public void HelloWorldTest() {
        OpenDriver();
        Get("http://www.google.com");
        SendKeys( By.name("q"), "Hello world!!");
        Click( By.name("btnK"));
        CloseDriver();
    }
</code></pre>
## Getting started
So far there is no packed distribution, however, you can clone this repository and build using Java 15, and Maven 3.6.3. to produce markito-0.1.jar file with <code>mvn package</code>.
<p>Create  yourclass.java file and add</p>
<pre><code>import org.openqa.selenium.*;</code></pre>
Now make your class to extend Markito as
<pre><code>public class HelloWorld extends Markito</code></pre>
You are ready to start coding.
