package cl.set.markito;

import java.io.IOException;

public class MarkitoBaseUtils2 {
    private IProcessManager processManager=null;
    private IFileManager fileManager=null;
    private IDebugManager debugManager=null;

    public MarkitoBaseUtils2(IProcessManager processManager, IFileManager fileManager, DebugManager debugManager) {
        this.processManager = processManager;
        this.fileManager = fileManager;
        this.debugManager = debugManager;
    }

    public MarkitoBaseUtils2() {
        this.processManager = new ProcessManager();
        this.fileManager = new FileManager();
        this.debugManager = new DebugManager();
    }
    /**
     * Kill a process by name.
     * @param processName
     * @return
     * 0: Process found and killed.
     * 1: Operation not implemented for OS.
     * -1: Exception whilst killing the process.
     */
    public int killProcess(String processName) {
        return this.processManager.killProcess(processName);
    }
    /**
     * Checks if there are instances of processes with name processName.
     * 
     * @param processName
     * @return boolean: true if found, false if operation not implemented on OS or not found.
     */
    public boolean isProcessRunning(String processName) {
        return this.processManager.isProcessRunning(processName);
    }

    public String ReadFileToString(String filePath) throws IOException {
        return fileManager.ReadFileToString(filePath);
    }

    /**
     * Clears console output.
     */
    public void ClearConsole() {
        debugManager.clearConsole();
    }

    /**
     * Enables println and printf to write to console.
     */
    public void SetDebugModeON() {
        debugManager.setDebugModeON();
    }

    /**
     * Disable println and printf to write to console.
     */
    public void SetDebugModeOFF() {
        debugManager.setDebugModeOFF();
    }

    /**
     * Prints an string to console when debug mode is ON.
     */
    public void println(String x) {
        debugManager.println(x);
    }

    /**
     * Prints an string using format string to console when debug mode is ON.
     */
    public void printf(String format, Object... args) {
        debugManager.printf(format, args);
    }
}
