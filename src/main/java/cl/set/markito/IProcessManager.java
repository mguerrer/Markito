package cl.set.markito;

/**
 * Interface to isolate some common used OS dependant operations over processes, to help ensure clean execution.
 */
public interface IProcessManager {

    public int killProcess(String processName);
    public boolean isProcessRunning(String processName);
}