package cl.set.markito.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class implements methods to manage OS processes.  Currently implemented only for Windows 10.
 */
public class ProcessManager extends DebugManager implements ProcessManagement {
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM ";
    /**
     * Current Operating System.
     */
    private final String OS = System.getProperty("os.name").toLowerCase();

    public String getCurrentOS() {
        return this.OS;
    }

    public int killProcess(String processName) {
        println("Killing "+OS+" process: " + processName);
        try {
            switch (OS) {
                case "windows 10":
                    Runtime.getRuntime().exec(KILL + processName);
                    return 0;
                default:
                    println("WARNING: Kill process not implemented fo OS=" + OS);
                    return 1;
            }
        } catch (Exception ex) {
            println("Error on kill process " + processName + ": " + ex.getMessage());
            return -1;
        }
    }

    public boolean isProcessRunning(String processName) {
        switch (OS) {
            case "windows 10":
                Process process;
                try {
                    process = Runtime.getRuntime().exec(TASKLIST);
                } catch (IOException ex) {
                    println("Error on get runtime" + ex.getMessage());
                    return false;
                }

                String line;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(processName)) {
                            println("Process found");
                            return true;
                        }
                    }
                } catch (IOException ex) {
                    println("Error on check for process " + processName + ": " + ex.getMessage());
                }
                return false;
            default:
                println("WARNING: Kill process not implemented fo OS=" + OS);
        }
        return false;
    }

    public String getComputerName() {
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            println("Hostname can not be resolved");
            return null;
        }
    }

    public void printProcessResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}