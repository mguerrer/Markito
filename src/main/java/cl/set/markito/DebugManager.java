package cl.set.markito;

import java.io.PrintStream;

/**
 * This class implements a simple facility to add debug or trace comments with the ability to enable or stop verbose on runtime.
 */
public class DebugManager extends MarkitoBaseUtilsValues implements IDebugManager {
    public boolean debug = true; // Indicates whether or not debug mode is ON or OFF.
    PrintStream output = System.out; // Defaults to console.

    public DebugManager() {
    }
    public void setDebugManagerOutputStream( PrintStream output ) {
        this.output = output;
    }

    /**
     * Clears console output.
     */
    public void clearConsole() {
        output.print("\033[H\033[2J"); // Clears console
        output.flush();
    }

    /**
     * Enables println and printf to write to console.
     */
    public void setDebugModeON() {
        debug = true;
    }

    /**
     * Disable println and printf to write to console.
     */
    public void setDebugModeOFF() {
        debug = false;
    }

    /**
     * Prints an string to console when debug mode is ON.
     */
    public void println(String x) {
        if (debug)
            output.println(x);
    }

    /**
     * Prints an string using format string to console when debug mode is ON.
     */
    public void printf(String format, Object... args) {
        if (debug)
            output.printf(format, args);
    }
}
