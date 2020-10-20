package cl.set.markito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class OsUtils
{
    private final String TASKLIST = "tasklist";
    private final String KILL = "taskkill /F /IM ";
    public final String  IE_EXE = "iexplore.exe";
    public final String  CHROME_EXE = "chrome.exe";
    public final String  CHROMEDRIVER_EXE = "chromedriver.exe";
    public final String  EDGE_EXE = "MicrosoftEdge.exe";
    public final String  EDGEDRIVER_EXE = "MsEdgeDriver.exe";
    public final String  FIREFOX_EXE = "firefox.exe";
    public final String  FIREFOXDRIVER_EXE = "geckodriver.exe";

    public boolean isProcessRunning(String processName)
    {
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

    public void killProcessIfRunning(String processName)
    {
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
