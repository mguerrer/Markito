package cl.set.markito;

import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserStack extends DebugManager {
     /**
     * Get BrowserStack's username from environment variable BSUSERNAME
     * @return
     */
    public String getBsUsername(){
        String username = System.getenv("BSUSERNAME");
        if ( username == null){
            printf(ANSI_RED+"ERROR: BrowserStack user name not found.  Please add to environment variable BSUSERNAME.");
        }
        return username;
    }
     /**
     * Get  BrowserStack's password (KEY) from environment variable BSPASSWORD
     * @return
     */
    public String getBsPassword(){
        String username = System.getenv("BSPASSWORD");
        if ( username == null){
            printf(ANSI_RED+"ERROR: BrowserStack user KEY not found.  Please add to environment variable BSPASSWORD.");
        }
        return username;
    }
    /**
     * Logs in debug console the list of capabilities set with a pretty format.
     * @param caps
     */
    public void LogCapabilities(DesiredCapabilities caps) {
        Map<String,Object> jsoncaps = caps.toJson();
        println("\nCapabilities: ");
        for (String key: jsoncaps.keySet()) {
            if ( !key.equals("browserstack.key") && !key.equals("browserstack.user")) {
               printf("-%s:%s\n",ANSI_WHITE+key, ANSI_YELLOW+jsoncaps.get(key));
            }
        }
    }
}
