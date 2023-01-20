package cl.set.markito.tests.MarkitoBaseUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import cl.set.markito.utils.FileManager;

@TestMethodOrder(OrderAnnotation.class)
@DisabledOnOs(OS.LINUX)
public class FileManagerTests extends FileManager {
    String testFile = userDownloadsFolder + "internet.html";
    @Test

    @Order(1)  
    void TestFileDownload() {
       int result = this.downloadFile("http://the-internet.herokuapp.com/", testFile );
       Assertions.assertTrue(result == 0);
    }
    @Test
    @Order(2)  
    void TestFileRead() {
        String pomContent = readFileToString(testFile);
        Assertions.assertTrue(pomContent != null);
        assertTrue(pomContent.length() != 0);
    }
    @Test
    @Order(3)  
    void TestFileIsDownloaded() {
        Assertions.assertTrue( checkIfFileIsDownloaded( "internet.html"));
    }
    @Test
    @Order(4)  
    void TestFindFilesByNameRegex() {
        File[] files = findFilesByNameRegex( "internet.html", userDownloadsFolder);
        Assertions.assertTrue( files != null);
        Assertions.assertTrue( files.length == 1);
    }
    @Test
    @Order(5)  
    void TestDeleteDownloadedFileIfExists() {
        int retCode = deleteDownloadedFileIfExists( "internet.html");
        Assertions.assertTrue( retCode == 0);
    }
}
