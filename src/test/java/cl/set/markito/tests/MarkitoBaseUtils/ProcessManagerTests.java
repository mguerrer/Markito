package cl.set.markito.tests.MarkitoBaseUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import cl.set.markito.MarkitoBaseUtils;

public class ProcessManagerTests extends MarkitoBaseUtils{

    @Test
    void TestKillProcess(){
        String os = System.getProperty("os.name");
        int retCode;
        assertEquals( "Windows 10", os);      
        retCode = this.killProcess("NON_EXISTING");
        switch (os) {
            case "Windows 10":
                assertEquals(0, retCode);
                break;
            default:
                assertEquals(1, retCode);
        }
    }
    @Test
    void TestKillProcessIfExistingProcessRunning(){
        String os = System.getProperty("os.name");
        boolean retCode;
        assertEquals( "Windows 10", os);      
        retCode = this.isProcessRunning("dwm.exe");
        switch (os) {
            case "Windows 10":
                assertEquals(true, retCode);
                break;
            default:
                assertEquals(false, retCode);
        }
    }
    @Test
    void TestKillProcessIfNotExistingProcessRunning(){
        String os = System.getProperty("os.name");
        boolean retCode;
        assertEquals( "Windows 10", os);      
        retCode = this.isProcessRunning("NON_EXISTING");
        assertEquals(false, retCode);
    }
    @Test
    void TestGetComputerName() {
        assertTrue( getComputerName() != null);
    }
}
