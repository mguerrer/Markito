package cl.set.markito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MarkitoBaseUtils
{
    private final String TASKLIST = "tasklist";
    private String KILL = "taskkill /F /IM ";
    public final String  IE_EXE = "iexplore.exe";
    public final String  CHROME_EXE = "chrome.exe";
    public final String  CHROMEDRIVER_EXE = "chromedriver.exe";
    public final String  EDGE_EXE = "MicrosoftEdge.exe";
    public final String  EDGEDRIVER_EXE = "MsEdgeDriver.exe";
    public final String  FIREFOX_EXE = "firefox.exe";
    public final String  FIREFOXDRIVER_EXE = "geckodriver.exe";
    private static String OS = System.getProperty("os.name").toLowerCase();
    public boolean debug=true;

    
    MarkitoBaseUtils(){
        switch (OS) 
        {
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
    void println(String x){
        if ( debug ) System.out.println(x);
    }    /**
    * Prints an string using format string to console when debug mode is ON.
    */
    void printf(String format, Object ... args){
        if ( debug ) System.out.printf(format, args);
    }
    /** 
     * @param processName
     * @return boolean
     */
    public boolean isProcessRunning(String processName)
    {
        switch (OS) 
        {
            case "win":
                Process process;
                try
                {
                    process = Runtime.getRuntime().exec(TASKLIST);
                }
                catch (IOException ex)
                {
                    System.out.println("Error on get runtime" + ex.getMessage());
                    return false;
                }

                String line;
                try ( BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); )
                {
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(processName)) {
                            System.out.println("Process found");
                            return true;
                        }
                    }
                }
                catch (IOException ex)
                {
                    System.out.println("Error on check for process " + processName + ": " + ex.getMessage());
                }
                return false;
            }
        return false;
    }
    /** 
     * @param processName
     */
    public void killProcessIfRunning(String processName)
    {
        switch (OS) 
        {
            case "win":
            System.out.println("Trying to kill process: " + processName);
            try
            {
                if (isProcessRunning(processName))
                {
                    Runtime.getRuntime().exec(KILL + processName);
                }
            }
            catch (IOException ex)
            {
                System.out.println("Error on kill process " + processName+ ": " +  ex.getMessage());
            }
        }
    }
}
