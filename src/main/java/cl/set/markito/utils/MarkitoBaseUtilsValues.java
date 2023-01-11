package cl.set.markito.utils;

import cl.set.markito.browsers.*;
import cl.set.markito.devices.Device;
import cl.set.markito.devices.GooglePixel3;
import cl.set.markito.devices.LocalComputer;
import cl.set.markito.devices.MACVentura;
import cl.set.markito.devices.Windows10;
import cl.set.markito.devices.Windows11;
import cl.set.markito.devices.iPhone11Pro;

public class MarkitoBaseUtilsValues {
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
        public final static Browser CHROME_BROWSER  = new Chrome();
        public final static Browser EDGE_BROWSER    = new Edge();
        public final static Browser IE_BROWSER      = new IE();
        public final static Browser FIREFOX_BROWSER = new Firefox();
        public final static Browser SAFARI_BROWSER  = new Safari();
        public final static Device  GOOGLEPIXEL3_DEVICE = new GooglePixel3();
        public final static Device  IPHONE11PRO_DEVICE  = new iPhone11Pro();
        public final static Device  LOCAL_COMPUTER_DEVICE = new LocalComputer();
        public final static Device  WINDOWS10_COMPUTER_DEVICE = new Windows10();
        public final static Device  WINDOWS11_COMPUTER_DEVICE = new Windows11();
        public final static Device  MAC_VENTURA_COMPUTER_DEVICE = new MACVentura();
}
