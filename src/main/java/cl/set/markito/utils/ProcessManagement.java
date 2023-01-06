package cl.set.markito.utils;

import java.io.IOException;

/**
 * Interface to isolate some common used OS dependant operations over processes, to help ensure clean execution.
 */
public interface ProcessManagement {

    public int killProcess(String processName);
    public boolean isProcessRunning(String processName);
    public String getComputerName();
    public void printProcessResults(Process process) throws IOException;
}