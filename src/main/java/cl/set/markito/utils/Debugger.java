package cl.set.markito.utils;

import java.io.PrintStream;

/**
 * This interface implements a simple facility to add debug or trace comments with the ability to enable or stop verbose on runtime.
 */

public interface Debugger {
    public boolean getDebugMode();
    public PrintStream getDebugManagerOutputStream();
    public void setDebugManagerOutputStream( PrintStream output );
    public boolean getColoredOutput();
    public void setColoredOutput(boolean coloredOutput);
    public void clearConsole();
    public void setDebugModeON();
    public void setDebugModeOFF();
    public void print(String x);
    public void println(String x);
    public void printf(String format, Object... args);
}