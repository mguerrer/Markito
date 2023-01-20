package cl.set.markito.tests.MarkitoBaseUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import cl.set.markito.MarkitoBaseUtils;

public class ProcessManagerTests extends MarkitoBaseUtils{

    @Test
    @DisabledOnOs(OS.LINUX)
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
    @DisabledOnOs(OS.LINUX)
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
    @DisabledOnOs(OS.LINUX)
    void TestKillProcessIfNotExistingProcessRunning(){
        String os = System.getProperty("os.name");
        boolean retCode;
        assertEquals( "Windows 10", os);      
        retCode = this.isProcessRunning("NON_EXISTING");
        assertEquals(false, retCode);
    }
    @Test
    @DisabledOnOs(OS.LINUX)
    void TestGetComputerName() {
        assertTrue( getComputerName() != null);
    }
}
