package cl.set.markito;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import cl.set.markito.utils.DebugManager;
import cl.set.markito.utils.FileManager;
import cl.set.markito.utils.IDebugManager;
import cl.set.markito.utils.IFileManager;
import cl.set.markito.utils.IProcessManager;
import cl.set.markito.utils.IRandomUtils;
import cl.set.markito.utils.MarkitoBaseUtilsValues;
import cl.set.markito.utils.ProcessManager;
import cl.set.markito.utils.RandomUtils;

public class MarkitoBaseUtils extends MarkitoBaseUtilsValues {
    private IProcessManager processManager = null;
    private IFileManager fileManager = null;
    private IDebugManager debugManager = null;
    private IRandomUtils randomUtils = null;

    public MarkitoBaseUtils(IProcessManager processManager, IFileManager fileManager, DebugManager debugManager, IRandomUtils randomUtils) {
        this.processManager = processManager;
        this.fileManager = fileManager;
        this.debugManager = debugManager;
        this.randomUtils = randomUtils;
    }

    public MarkitoBaseUtils() {
        this.processManager = new ProcessManager();
        this.fileManager = new FileManager();
        this.debugManager = new DebugManager();
        this.randomUtils = new RandomUtils();
    }

    /* This group of simple methods to manage OS processes. */
    /**
     * Kill a process by name.
     * 
     * @param processName
     * @return
     *         0: Process found and killed.
     *         1: Operation not implemented for OS.
     *         -1: Exception whilst killing the process.
     */
    public int killProcess(String processName) {
        return this.processManager.killProcess(processName);
    }

    /**
     * Checks if there are instances of processes with name processName.
     * 
     * @param processName
     * @return boolean: true if found, false if operation not implemented on OS or
     *         not found.
     */
    public boolean isProcessRunning(String processName) {
        return this.processManager.isProcessRunning(processName);
    }

    /**
     * Get the host computer's name.
     * 
     * @return
     */
    public String getComputerName() {
        return processManager.getComputerName();
    }

    /**
     * Print the process results.
     * 
     * @param process
     * @throws IOException
     */
    public void printProcessResults(Process process) throws IOException {
        processManager.printProcessResults(process);
    }

    /* This group of methods to manage provide a simple debug tool. */
    /**
     * Get the current debug mode.
     * @return true: Debug mode is ON, false otherwise.
     */
    public boolean getDebugMode() {
        return debugManager.getDebugMode();
    }
    /**
     * Get current output stream.
     * 
     * @return
     */
    public PrintStream getDebugManagerOutputStream() {
        return debugManager.getDebugManagerOutputStream();
    }

    /**
     * Allows to redirect print commands to another output stream. Default to
     * System.out.
     */
    public void setDebugManagerOutputStream(PrintStream output) {
        debugManager.setDebugManagerOutputStream(output);
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

    /**
     * Downloads a file from URL to targetFilePathname.
     * 
     * @param URL
     * @param targetFilePathname
     * @return 0 on success, <0 on failure, >0 WARNING.
     */
    public int downloadFile(String URL, String targetFilePathname) {
        return fileManager.downloadFile(URL, targetFilePathname);
    }

    /**
     * Returns the content of a file given his pathname.
     * 
     * @param filePath
     * @return: File's content.
     * @throws IOException
     */
    public String readFileToString(String filePath) throws IOException {
        return fileManager.readFileToString(filePath);
    }

    /**
     * Checks whether or not a file with fileName exists in downloads folder.
     * 
     * @param fileName
     * @return true or false
     */
    public boolean checkIfFileIsDownloaded(String fileName) {
        return fileManager.checkIfFileIsDownloaded(fileName);
    }

    /**
     * Waits for a file with fileName exists in downloads folder up to
     * timeoutInSeconds.
     * 
     * @param fileName
     * @param timeoutInSeconds
     * @return true or false
     */
    public boolean waitForFileDownloaded(String fileName, long timeoutInSeconds) {
        return fileManager.waitForFileDownloaded(fileName, timeoutInSeconds);
    }

    /***
     * Deletes a file with fileName if exists in downloads folder.
     * 
     * @param fileName
     */
    public void deleteDownloadedFileIfExists(String fileName) {
        fileManager.deleteDownloadedFileIfExists(fileName);
    }

    /**
     * Find files in a folder whose name matches regular expression.
     * 
     * @param nameRegex
     * @param folder
     * @return
     */
    public File[] findFilesByNameRegex(String nameRegex, String folder) {
        return fileManager.findFilesByNameRegex(nameRegex, folder);
    }

    /**
     * Deletes a file.
     * 
     * @param filename
     * @return true on success, false otherwise.
     */
    public boolean deleteFile(String filename) {
        return fileManager.deleteFile(filename);
    }

     /**
     * Generates a random String of size length.
     * @param size
     * @return
     */
    public String RandomString(int size){
        return randomUtils.RandomString(size);
    }

    /**
     * Generates a random integer in range [min, max].
     * @param min
     * @param max
     * @return
     */
    public int RandomNumber(int min, int max){
        return randomUtils.RandomNumber(min, max);
    }
}