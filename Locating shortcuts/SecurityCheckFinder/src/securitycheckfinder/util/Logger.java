package securitycheckfinder.util;

public class Logger {

    public static void comment(String msg) {
        if (Globals.VERBOSE) {
            System.out.println("# " + msg);
        }
    }

    public static void error(String msg) {
        System.err.println("ERROR: " + msg);
        System.exit(-1);
    }
}
