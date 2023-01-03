package cl.set.markito.tests.MarkitoBaseUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import cl.set.markito.utils.DebugManager;

public class DebugManagerTests extends DebugManager {
    @Test
    void TestDebugModeWithPrintln() throws IOException{
        // Arrange
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        this.setDebugManagerOutputStream(new PrintStream(bo));// Redirects System.out
        // Act
        setDebugModeON();
        println(ANSI_GREEN+"This is a test msg to be visible");
        setDebugModeOFF();
        println(ANSI_GREEN+"This is a test msg not to be visible");
        bo.flush();
        // Assert
        String allWrittenLines = new String(bo.toByteArray()); 
        assertTrue(allWrittenLines.contains("This is a test msg to be visible"));
        assertFalse(allWrittenLines.contains("This is a test msg not to be visible"));
    }

    @Test
    void TestDebugModeWithPrintf() throws IOException{
        // Arrange
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        this.setDebugManagerOutputStream(new PrintStream(bo));// Redirects System.out
        // Act
        setDebugModeON();
        printf("%s", ANSI_GREEN+"This is a test msg to be visible");
        setDebugModeOFF();
        printf("%s", ANSI_GREEN+"This is a test msg not to be visible");
        bo.flush();
        // Assert
        String allWrittenLines = new String(bo.toByteArray()); 
        assertTrue(allWrittenLines.contains("This is a test msg to be visible"));
        assertFalse(allWrittenLines.contains("This is a test msg not to be visible"));
    }

}
