/**
 * Pruebas Panel 
 */
package cl.gasco.gasconnect.panel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cl.set.markito.MarkitoWeb;

@TestInstance(Lifecycle.PER_CLASS)
public class PedidosDIConsultasTest extends MarkitoWeb {

  @BeforeAll
  public void setUp() {
    OpenWebDriver();
    Login();
  }

  @AfterAll
  public void tearDown() {
    Logout();
    CloseWebDriver();
  }

  public void Login() {
    Get("https://clientes:TXpYcCdS@panelgasconnect.fusionadns.cl/");
    SendKeys(By.id("exampleInputEmail1"), "testadmin@yopmail.com");
    SendKeys(By.id("exampleInputPassword1"), "gasco2020");
    Click(By.cssSelector(".btn"));
    assertTrue(GetText(By.cssSelector(".dropdown > a > .ng-binding")).equals("Administrador"));
  }

  public void Logout() {
    Click(By.xpath("//span[contains(.,\'Administrador\')]"));
    Click(By.xpath("//p[contains(.,\'Cerrar sesión\')]"));
    Click(By.xpath("//button[contains(.,\'Sí\')]"));
  }
  private void WaitUntilConditionIsMet(BooleanSupplier awaitedCondition, int timeoutInSec) {
    boolean done;
    long startTime = System.currentTimeMillis();
    do {
        done = awaitedCondition.getAsBoolean();
    } while (!done && System.currentTimeMillis() - startTime < timeoutInSec * 1000);
}
  public void SelectOptionByVisibleText( By by, String VisibleText){

    Select dropdown = new Select(driver.findElement( by ));
    // Waits for options loaded.
    WaitUntilConditionIsMet(() -> (dropdown.getOptions().size() > 0), (int) timeOutInSeconds );
    HighLightElement(driver.findElement(by));
    dropdown.selectByVisibleText(VisibleText);
    WaitUntilConditionIsMet(() -> dropdown.getFirstSelectedOption().getText().equals( VisibleText), (int) timeOutInSeconds);
    printf("SelectOptionByVisibleText from %s option %s\n", by, VisibleText);
  }
  public void BusquedaPorEstado(String EstadoDeseado, String RespuestaEsperada) {
    // Get("/#/login");

    Click(By.xpath("//span[contains(.,\'Pedidos DI\')]"));

    this.SelectOptionByVisibleText(By.id("status"), "Ninguna seleccionada");
    this.SelectOptionByVisibleText(By.id("status"), EstadoDeseado);

    Click(By.xpath("//button[contains(.,\'Filtrar\')]"));
    // Espera respuesta
    WaitForElementVisible(By.xpath("//table[@id=\'datatable2\']/tbody/tr/td[7]"));
    String TextoLeido = GetText(By.xpath("//table[@id=\'datatable2\']/tbody/tr/td[7]"));
    printf("Estado=%s Recibido=%s\n", EstadoDeseado, TextoLeido);

    assertEquals( RespuestaEsperada, TextoLeido);
  }
  @Tag("PROD")
  @DisplayName("PedidosDI->Consulta x Estado=PENDIENTE")
  @Test
  public void ConsultaPorEstadosPendiente() {
    BusquedaPorEstado( "PENDIENTE", "PENDIENTE - no hay controladores");
  }
  @Tag("PROD")
  @DisplayName("PedidosDI->Consulta x Estado=ACEPTADO")
  @Test
  public void ConsultaPorEstadosAceptado() {
    BusquedaPorEstado( "ACEPTADO", "ACEPTADO");
  }
  @Tag("PROD")
  @DisplayName("PedidosDI->Consulta x Estado=ALCANZADO")
  @Test
  public void ConsultaPorEstadosAlcanzado() {
    BusquedaPorEstado( "ALCANZADO", "ALCANZADO");
  }
  @Tag("PROD")
  @DisplayName("PedidosDI->Consulta x Estado=CANCELADO POR EL CLIENTE")
  @Test
  public void ConsultaPorEstadosCanceladoCli() {
    BusquedaPorEstado( "CANCELADO POR EL CLIENTE", "CANCELADO POR EL CLIENTE");
  }
  @Tag("PROD")
  @DisplayName("PedidosDI->Consulta x Estado=CANCELADO POR ADMIN")
  @Test
  public void ConsultaPorEstadosCanceladoAdm() {
    BusquedaPorEstado( "CANCELADO POR ADMIN", "CANCELADO POR ADMIN");
  }
  @Tag("PROD")
  @DisplayName("PedidosDI->Consulta x Estado=CANCELADO POR EL CONDUCTOR")
  @Test
  public void ConsultaPorEstadosCanceladoCOnd() {
    BusquedaPorEstado( "CANCELADO POR EL CONDUCTOR", "CANCELADO POR EL CONDUCTOR");
  }


}

