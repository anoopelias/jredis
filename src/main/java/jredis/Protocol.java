package jredis;

import java.io.UnsupportedEncodingException;


/**
 * Utility methods
 * 
 * @author anoopelias
 *
 */
public class Protocol {
    
    // So that the class won't get instantiated.
    public Protocol() {
        
    }
    
    public static final byte DOLLAR = '$';
    public static final byte PLUS = '+';
    public static final byte COLON = ':';
    public static final byte STAR = '*';
    public static final byte MINUS = '-';
    public static final byte CR = '\r';
    public static final byte LF = '\n';

    public static final byte[] CRLF = { CR , LF };
    public static final byte[] NULL_STRING = { DOLLAR, MINUS, '1' };

    public static final byte[] ERROR = toBytes("ERR ");

    public static final String CHARSET = "UTF-8";
    
    /**
     * Parse a float value to String.
     * 
     * @param d
     * @return
     */
    public static Double parseDouble(String d) {
        try {
            return Double.parseDouble(d);
        } catch (NumberFormatException e) {
            
            if (d.endsWith("inf")) {
                
                if (d.length() == 3)
                    return Double.POSITIVE_INFINITY;

                if (d.length() == 4) {
                    if (d.charAt(0) == '+')
                        return Double.POSITIVE_INFINITY;
                    if (d.charAt(0) == '-')
                        return Double.NEGATIVE_INFINITY;
                }
            }

            throw e;

        }
    }
    
    /**
     * Convert string to bytes.
     * 
     * @param s
     * @return
     */
    public static byte[] toBytes(String s) {
        try {
            return s.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            if (Server.isDebug())
                e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert integer to bytes.
     * 
     * @param number
     * @return
     */
    public static byte[] toBytes(int number) {
        return toBytes(String.valueOf(number));
    }

    /**
     * Convert long to bytes.
     * 
     * @param number
     * @return
     */
    public static byte[] toBytes(long number) {
        return toBytes(String.valueOf(number));
    }

}
