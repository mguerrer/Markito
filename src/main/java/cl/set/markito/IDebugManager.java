package cl.set.markito;

import java.io.PrintStream;

/**
 * This interface implements a simple facility to add debug or trace comments with the ability to enable or stop verbose on runtime.
 */

public interface IDebugManager {
    public boolean getDebugMode();
    public PrintStream getDebugManagerOutputStream();
    public void setDebugManagerOutputStream( PrintStream output );
    public void clearConsole();
    public void setDebugModeON();
    public void setDebugModeOFF();
    public void println(String x);
    public void printf(String format, Object... args);
}