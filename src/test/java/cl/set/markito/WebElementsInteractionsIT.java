/* Ejemplo de uso de webdriver para interacciones finas con elementos de la página.
 * Autor: Marcos Guerrero
 * Fecha: 25-08-2020
 */
package cl.set.markito;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;


@TestMethodOrder(OrderAnnotation.class)
public class WebElementsInteractionsIT {
     private static WebDriver driver;
     JavascriptExecutor js;

     @DisplayName("Selección múltiple.")
     @Test
     void seleccionMultiple() throws InterruptedException {
          driver.get("http://omayo.blogspot.com/");

          WebElement listaSeleccionMultiple = miFindElement(By.id("multiselect1")); // Selecciona div de búsqueda
          Select selector = new Select(listaSeleccionMultiple);
          selector.deselectAll(); // Deselecciona todo por si acaso.
          selector.selectByIndex(0);
          selector.selectByValue("Hyundaix");
          selector.selectByVisibleText("Swift");
     }
     @DisplayName("Radio buttons y Checkboxes.")
     @Test
     void radioButton() throws InterruptedException {
          driver.get("http://demo.guru99.com/test/radio.html");					
          WebElement radio1 = driver.findElement(By.id("vfb-7-1"));							
          WebElement radio2 = driver.findElement(By.id("vfb-7-2"));							
          
          radio1.click();//Radio Button1 seleccionado			
          radio2.click();//Radio Button1 desmarcado y marca Radio Button2
          
          WebElement option1 = driver.findElement(By.id("vfb-6-0"));// Seleccionamos CheckBox
          
          option1.click();// Seleccionamos el checkbox 		
          System.out.printf("radio1=%b radio2=%b Checkbox=%b", 
                             radio1.isSelected(), radio2.isSelected(), option1.isSelected());					
     }
     @DisplayName("Mouse:doble click.")
     @Test
     void mouse() {
          driver.get("http://demo.guru99.com/test/simple_context_menu.html");
          driver.manage().window().maximize();
          //Doble click genera una alerta
          Actions action = new Actions(driver);
          WebElement link =driver.findElement(By.xpath("//button[text()='Double-Click Me To See Alert']"));
          action.doubleClick(link).perform();
          //Atiende la alerta
          Alert alert = driver.switchTo().alert();
          System.out.println("Texto de la alerta:" +alert.getText());
          alert.accept();
     }
     @DisplayName("Mouse y teclado:Menú de contexto.")
     @Test
     void menuContexto() {
          driver.get("http://demo.guru99.com/test/simple_context_menu.html");
          Actions miAccion = new Actions(driver); 
          WebElement menuContexto = driver.findElement(By.className("context-menu-one"));
          miAccion = miAccion // Construye secuencia
                    .contextClick(menuContexto)
                    .sendKeys(Keys.ARROW_DOWN)
                    .sendKeys(Keys.ARROW_DOWN)
                    .sendKeys(Keys.ARROW_DOWN)
                    .sendKeys(Keys.ARROW_DOWN)
                    .tick()
                    .sendKeys(Keys.ENTER);
          for (int i=0; i<10; i++){
               miAccion.build().perform(); //Selecciona opción 4
               if ( isAlertPresent()){ //Atiende la alerta
                    Alert alert = driver.switchTo().alert();
                    System.out.println("Texto de la alerta:" +alert.getText());
                    alert.accept();
               }
          }
     }
     @BeforeAll
     public static void creaDriver() throws IOException {
          driver = new ChromeDriver();
          driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES); // Timeouts de waitFor*
          driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MINUTES); // TImeout de espera de página
          driver.manage().timeouts().setScriptTimeout(1, TimeUnit.MINUTES); // Timeout de ejecución javascript
          System.out.print("\033[H\033[2J");  // Borra consola
          System.out.flush();  
     }
     @AfterAll
     public static void detieneDriver() {
          driver.quit();
     }
     WebElement miFindElement(By by){
          try{
               WebElement element = driver.findElement(by);
               highLightElement(driver, element);
               imprimeAtributos(element);
               return element; 
          } catch (Exception e){
               return null;
          }
     }
     public void highLightElement(WebDriver driver, WebElement element){
          JavascriptExecutor js = (JavascriptExecutor) driver;
          js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');", element);
     }
     int i=1;
     void imprimeAtributos(WebElement element)
     {
          System.out.print((i++)+"-[");
          String id = element.getAttribute("id");
          if (id!=null && ! id.equals(""))
               System.out.print("id="+id);
          String title = element.getAttribute("title");
          if (title!=null && ! title.equals(""))
               System.out.print(" title="+title);
          String name = element.getAttribute("name");
          if (name!=null && ! name.equals(""))
               System.out.print(" name="+name);
          String type = element.getAttribute("type");
          if (type!=null && ! type.equals(""))
               System.out.print(" type="+type);
          String value = element.getAttribute("value");
          if (value!=null && ! value.equals(""))
               System.out.print(" value="+value);
          System.out.print("]-[");
          if (element.isDisplayed())
               System.out.print("Displayed-");
          if (element.isSelected())
               System.out.print("Selected-");
          if (element.isEnabled())
               System.out.print("Enabled");
          System.out.println("]");
     }
     public boolean isAlertPresent() 
     { 
          try 
          { 
               driver.switchTo().alert(); 
               return true; 
          }   // try 
          catch (NoAlertPresentException Ex) 
          { 
               return false; 
          }   // catch 
     } 
}