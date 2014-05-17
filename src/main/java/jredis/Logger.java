package jredis;

/**
 * Logger.
 * 
 * @author anoopelias
 *
 */
public class Logger {

    private static boolean isDebug = Boolean.parseBoolean(Server
            .config("debug"));

    /**
     * Prints a debug message
     * 
     * @param message
     */
    public static void debug(String message) {
        if (isDebug)
            print(message);
    }

    /**
     * Prints a throwable.
     * 
     * @param t
     */
    public static void debug(Throwable t) {
        if (isDebug)
            print(t);
    }

    /**
     * Returns if the server is in debug mode.
     * 
     */
    public static boolean isDebug() {
        return isDebug;
    }

    
    /**
     * Prints a message as well as throwable.
     * 
     * @param message
     * @param t
     */
    public static void debug(String message, Throwable t) {
        debug(message);
        debug(t);
    }

    /**
     * Prints an info message.
     * 
     * @param message
     */
    public static void info(String message) {
        print(message);
    }

    /**
     * Prints an info throwable.
     * 
     * @param message
     */
    public static void info(Throwable t) {
        print(t);
    }

    /**
     * Print a string message.
     * 
     * @param message
     */
    private static void print(String message) {
        System.out.println(message);
    }

    /**
     * Print an exception stack trace.
     * 
     * @param t
     */
    private static void print(Throwable t) {
        t.printStackTrace();
    }



}
