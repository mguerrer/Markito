/* Ejemplos de uso de webdriver para crear sesiones usando capacidades.
 * Autor: Marcos Guerrero
 * Fecha: 25-08-2020
 */
package cl.set.markito;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.*;
@TestMethodOrder(OrderAnnotation.class)

public class WebSessionsIT {
     @DisplayName("Usando capabilities Chrome")
     @Test
     void usandoCapacidadesChrome(){
          ChromeOptions options = new ChromeOptions();
          //https://www.selenium.dev/documentation/en/webdriver/page_loading_strategy/
          options.setPageLoadStrategy(PageLoadStrategy.EAGER); 
          options.setHeadless(true);  // Indica modo headless.
          options.setAcceptInsecureCerts(true); // Acepta sitio sin certificados seguros.
          options.setCapability("platform", "windows");
          
          System.out.println(options.toString());
          WebDriver driver = new ChromeDriver(options);
          driver.get("https://www.Google.cl");
          System.out.println("Título:"+driver.getTitle());
          driver.quit();
     }     
     @DisplayName("Usando capabilities Firefox")
     @Test
     void usandoCapacidadesFirefox(){
          FirefoxOptions options = new FirefoxOptions();

          System.out.println("Firefox en modo headless. version >= 56");
          options.addArguments("-headless");
          options.setHeadless(true); // Hace lo mismo que anterior.

          options.setCapability("platform", "windows");
          options.addArguments("--window-size=1280,1024");
          options.setLogLevel(FirefoxDriverLogLevel.TRACE);
          System.out.println("Capacidades:"+options.toString());
          WebDriver driver = new FirefoxDriver(options);
          driver.get("https://www.Google.cl");
          System.out.println("Título:"+driver.getTitle());
          driver.quit();
     }     
     @DisplayName("Buscando capacidad no encontrada")
     @Test
     void fallandoCapacidadesFirefox(){
          FirefoxOptions options = new FirefoxOptions();
          options.setCapability("platform", "linux");
          
          try {
               WebDriver driver = new FirefoxDriver(options);
               driver.quit();
          } 
          catch (Exception e){
               System.out.println("Excepción:"+e.getMessage());
          }
     }
}