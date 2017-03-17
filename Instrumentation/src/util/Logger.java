/*
 * Default License
 * 
 */
package util;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class Logger {

    public static final boolean VERBOSE = true;

    public static void println(String msg) {
        if (VERBOSE) {
            System.out.println(msg);
        }
    }

    public static void println(String prefix, String msg) {
        Logger.println("[" + prefix + "] " + msg);
    }

    public static void printErr(String msg) {
        System.err.println(msg);
    }

}
