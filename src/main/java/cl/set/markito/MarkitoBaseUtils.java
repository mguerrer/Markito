// Markito general util test tools.
// Marcos Guerrero
// 20-01-2021
package cl.set.markito;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.util.concurrent.ThreadLocalRandom;

public class MarkitoBaseUtils {
    private final String TASKLIST = "tasklist";
    private String KILL = "taskkill /F /IM ";
    public final String IE_EXE = "iexplore.exe";
    public final String CHROME_EXE = "chrome.exe";
    public final String CHROMEDRIVER_EXE = "chromedriver.exe";
    public final String EDGE_EXE = "MicrosoftEdge.exe";
    public final String EDGEDRIVER_EXE = "MsEdgeDriver.exe";
    public final String FIREFOX_EXE = "firefox.exe";
    public final String FIREFOXDRIVER_EXE = "geckodriver.exe";
    private String OS = System.getProperty("os.name").toLowerCase();
    public boolean debug = true;
    // ANSI colors tobe used in println and printf.
    public final String ANSI_RESET = "\u001B[0m";
    public final String ANSI_BLACK = "\u001B[30m";
    public final String ANSI_RED = "\u001B[31m";
    public final String ANSI_GREEN = "\u001B[32m";
    public final String ANSI_YELLOW = "\u001B[33m";
    public final String ANSI_BLUE = "\u001B[34m";
    public final String ANSI_PURPLE = "\u001B[35m";
    public final String ANSI_CYAN = "\u001B[36m";
    public final String ANSI_WHITE = "\u001B[37m";
    public final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public MarkitoBaseUtils() {
        switch (OS) {
            case "win":
                KILL = "taskkill /F /IM ";
        }
    }

    /**
     * Clears console output.
     */
    public void ClearConsole() {
        System.out.print("\033[H\033[2J"); // Borra consola
        System.out.flush();
    }

    /**
     * Prints an string to console when debug mode is ON.
     */
    public void println(String x) {
        if (debug)
            System.out.println(x);
    }

    /**
     * Prints an string using format string to console when debug mode is ON.
     */
    public void printf(String format, Object... args) {
        if (debug)
            System.out.printf(format, args);
    }

    /**
     * Checks if there are instances of processes with name processName.
     * 
     * @param processName
     * @return boolean
     */
    public boolean isProcessRunning(String processName) {
        switch (OS) {
            case "win":
                Process process;
                try {
                    process = Runtime.getRuntime().exec(TASKLIST);
                } catch (IOException ex) {
                    System.out.println("Error on get runtime" + ex.getMessage());
                    return false;
                }

                String line;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(processName)) {
                            System.out.println("Process found");
                            return true;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Error on check for process " + processName + ": " + ex.getMessage());
                }
                return false;
        }
        return false;
    }

    /**
     * @param processName
     */
    public void killProcessIfRunning(String processName) {
        switch (OS) {
            case "win":
                System.out.println("Trying to kill process: " + processName);
                try {
                    if (isProcessRunning(processName)) {
                        Runtime.getRuntime().exec(KILL + processName);
                    }
                } catch (IOException ex) {
                    System.out.println("Error on kill process " + processName + ": " + ex.getMessage());
                }
        }
    }

    /**
     * Checks whether or not a file with fileName exists in downloads folder.
     * 
     * @param fileName
     * @return true or false
     */
    public boolean CheckIfFileIsDownloaded(String fileName) {
        String filePathname = System.getProperty("user.home") + "/Downloads/" + fileName;
        File file = new File(filePathname);
        if (file.exists() && !file.isDirectory()) {
            return true;
        } else
            return false;
    }

    /**
     * Checks whether or not a file with fileName exists in downloads folder.
     * 
     * @param fileName
     * @return true or false
     */
    public boolean WaitForFileDownloaded(String fileName, long timeoutInSeconds) {
        int i = 0;
        String filePathname = FileSystems.getDefault().getPath(System.getProperty("user.home"), "downloads", fileName).toString();

        printf(ANSI_YELLOW + "WaitForFileDownloaded %s..", filePathname);
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(CheckIfFileIsDownloaded(fileName)) { 
                printf(ANSI_YELLOW+"found.\n");
                return true;
            }
            printf(ANSI_YELLOW+".");
        } while( i++<timeoutInSeconds);
        printf(ANSI_RED+"ERROR: File %s not found in %d\n",filePathname, timeoutInSeconds);
        return false;
    }
    
    /**
     * Print the process results.
     * @param process
     * @throws IOException
     */
    public void PrintResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
    /***
     * Deletes a file with fileName if exists in downloads folder.
     * 
     * @param fileName
     */
    public void DeleteDownloadedFileIfExists(String fileName) {
        if ( CheckIfFileIsDownloaded( fileName)){
            String filePathname = System.getProperty("user.home") +"/Downloads/" + fileName;
            File file = new File(filePathname);
            file.delete();
        } 
    }
    /**
     * Generates a random String of size length.
     * @param size
     * @return
     */
    public String RandomString(int size) {
        // El banco de caracteres
        String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String randomString = "";
        for (int x = 0; x < size; x++) {
            int randomIndex = RandomNumber(0, pool.length() - 1);
            char randomCharacter = pool.charAt(randomIndex);
            randomString += randomCharacter;
        }
        return randomString;
    }
    public int RandomNumber(int min, int max) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public String GetComputerName() {
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
            return null;
        }
    }
    public boolean DeleteFile( String filename) { 
        File myObj = new File(filename); 
        return myObj.delete();
    } 
}
