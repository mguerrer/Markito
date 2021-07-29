/**
 * Markito general util test tools.
 * Marcos Guerrero
 * 30-06-2021
 */
package cl.set.markito;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class MarkitoBaseUtils {
    private final static String TASKLIST = "tasklist";
    private static String KILL = "taskkill /F /IM ";
    public final static String IE_EXE = "iexplore.exe";
    public final static String IE_DRIVER_EXE = "IEDriverServer.exe";
    public final static String CHROME_EXE = "chrome.exe";
    public final static String CHROMEDRIVER_EXE = "chromedriver.exe";
    public final static String EDGE_EXE = "MicrosoftEdge.exe";
    public final static String EDGEDRIVER_EXE = "MsEdgeDriver.exe";
    public final static String FIREFOX_EXE = "firefox.exe";
    public final static String FIREFOXDRIVER_EXE = "geckodriver.exe";
    private static String OS = System.getProperty("os.name").toLowerCase();
    public static boolean debug = true;
    // ANSI colors tobe used in println and printf.
    public final static String ANSI_RESET = "\u001B[0m";
    public final static String ANSI_BLACK = "\u001B[30m";
    public final static String ANSI_RED = "\u001B[31m";
    public final static String ANSI_GREEN = "\u001B[32m";
    public final static String ANSI_YELLOW = "\u001B[33m";
    public final static String ANSI_BLUE = "\u001B[34m";
    public final static String ANSI_PURPLE = "\u001B[35m";
    public final static String ANSI_CYAN = "\u001B[36m";
    public final static String ANSI_WHITE = "\u001B[37m";
    public final static String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public final static String ANSI_RED_BACKGROUND = "\u001B[41m";
    public final static String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public final static String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public final static String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public final static String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public final static String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public final static String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public MarkitoBaseUtils() {
        switch (OS) {
            case "win":
                KILL = "taskkill /F /IM ";
        }
    }

    /**
     * Clears console output.
     */
    public static void ClearConsole() {
        System.out.print("\033[H\033[2J"); // Borra consola
        System.out.flush();
    }

    /**
     * Prints an string to console when debug mode is ON.
     */
    public static void println(String x) {
        if (debug)
            System.out.println(x);
    }

    /**
     * Prints an string using format string to console when debug mode is ON.
     */
    public static void printf(String format, Object... args) {
        if (debug)
            System.out.printf(format, args);
    }

    /**
     * Checks if there are instances of processes with name processName.
     * 
     * @param processName
     * @return boolean
     */
    public static boolean isProcessRunning(String processName) {
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
    public static void killProcessIfRunning(String processName) {
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
    public static boolean CheckIfFileIsDownloaded(String fileName) {
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
    public static boolean WaitForFileDownloaded(String fileName, long timeoutInSeconds) {
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
    public static void PrintResults(Process process) throws IOException {
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
    public static void DeleteDownloadedFileIfExists(String fileName) {
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
    public static String RandomString(int size) {
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
    public static int RandomNumber(int min, int max) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public static String GetComputerName() {
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
    public static boolean DeleteFile( String filename) { 
        printf(ANSI_YELLOW+"DeleteFile %s\n", filename);
        try {
            File myObj = new File(filename); 
            return myObj.delete();
        }
        catch (Exception e){
            printf(ANSI_RED+"failed!!! %s\n", e.getMessage());
            return false;
        }

    } 
    /**
     * Find files in a folder whose name matches regular expression.
     * @param nameRegex
     * @param folder
     * @return
     */
    public static File[] FindFilesByNameRegex(String nameRegex, String folder) {
        File[] files;
        try {
            File f = new File(folder);

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.matches(nameRegex);
                }
            };
            files = f.listFiles(filter);
            return files;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    /**
     * Recibe un archivo que lee y devuelve su contenido.
     * @param strFileInput: Pathname del archivo.
     * @return strData: Contenido del archivo.
     */
    public static String ReadFileToString(String strFileInput) {
        printf(ANSI_YELLOW+"ReadFileToString %s\n", strFileInput);
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(strFileInput), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
}
