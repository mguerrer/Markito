package cl.set.markito.tests.MarkitoBaseUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import cl.set.markito.FileManager;

@TestMethodOrder(OrderAnnotation.class)
public class FileManagerTests extends FileManager {
    String testFile = userDownloadsFolder + "internet.html";
    @Test
    @Order(1)  
    void TestFileDownload() {
       this.downloadFile("http://the-internet.herokuapp.com/", testFile );
    }
    @Test
    @Order(2)  
    void TestFileRead() {
        String pomContent = readFileToString(testFile);
        assertTrue(pomContent.length() != 0);
    }
    @Test
    @Order(3)  
    void TestFileIsDownloaded() {
        assertTrue( checkIfFileIsDownloaded( "internet.html"));
    }
    @Test
    @Order(4)  
    void TestFindFilesByNameRegex() {
        File[] files = findFilesByNameRegex( "internet.html", userDownloadsFolder);
        assertTrue( files.length == 1);
    }
    @Test
    @Order(5)  
    void TestDeleteDownloadedFileIfExists() {
        deleteDownloadedFileIfExists( "internet.html");
    }
}
