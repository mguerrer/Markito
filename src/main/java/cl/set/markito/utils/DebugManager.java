package cl.set.markito.utils;

import java.io.PrintStream;

/**
 * This class implements a simple facility to add debug or trace comments with the ability to enable or stop verbose on runtime.
 */
public class DebugManager extends MarkitoBaseUtilsValues implements Debugger {
    private boolean debugMode = true; // Indicates whether or not debug mode is ON or OFF.
    private boolean coloredOutput = true; // Indicates to print using console control codes.   Otherwise removes them.
    PrintStream output = System.out; // Defaults to console.

    public DebugManager() {
        
    }
    public PrintStream getDebugManagerOutputStream() {
        return this.output;
    }    
    public void setDebugManagerOutputStream( PrintStream output ) {
        this.output = output;
    }

    public boolean getDebugMode() {
        return debugMode;
    }
    /**
     * Return the colored output mode.
     * @return true or false
     */
    public boolean getColoredOutput() {
        return coloredOutput;
    }
    /**
     * Set mode to colored output on console.
     * @param coloredOutput: true colored are printed, false removes colors.
     */
    public void setColoredOutput(boolean coloredOutput) {
        this.coloredOutput = coloredOutput;
    }

    /**
     * Clears console output.
     */
    public void clearConsole() {
        if ( coloredOutput ) {
            output.print("\033[H\033[2J"); // Clears console
            output.flush();
        }
    }

    /**
     * Enables println and printf to write to console.
     */
    public void setDebugModeON() {
        debugMode = true;
    }

    /**
     * Disable println and printf to write to console.
     */
    public void setDebugModeOFF() {
        debugMode = false;
    }

    /**
     * Prints an string to console when debug mode is ON.
     */
    public void print(String x) {
        if (debugMode) {
            if ( coloredOutput ) {
                output.print(x);
            } else {
                output.print( removeControlChars(x));
            }
        }
    }
    /**
     * Prints an string as line to console when debug mode is ON.
     */
    public void println(String x) {
        if (debugMode) {
            if ( coloredOutput ) {
                output.println(x);
            } else {
                output.println( removeControlChars(x));
            }
        }
    }

    /**
     * Prints an string using format string to console when debug mode is ON.
     */
    public void printf(String format, Object... args) {
        if (debugMode) {
            if (coloredOutput ) {
                output.printf(format, args);
            } else {
                output.printf(removeControlChars(format), args);
            }
        }
    }

    private String removeControlChars( String str ){

        try{
            str = str.replaceAll("\\u001B\\[[0-9]{1,2}m", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return str;
     }
}
