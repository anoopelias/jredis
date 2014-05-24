package jredis;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import jredis.domain.BinaryString;
import jredis.exception.InternalServerError;

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

    public static final byte[] CRLF = { CR, LF };
    public static final byte[] NULL_STRING = { DOLLAR, MINUS, '1' };

    public static final byte[] ERROR = toBytes("ERR ");

    public static final String CHARSET = "UTF-8";
    public static final ByteOrder ENDIAN = ByteOrder.LITTLE_ENDIAN;

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
            Logger.debug(e);
            throw new InternalServerError(e);
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

    /**
     * Convert bytes to string.
     * 
     * @param number
     * @return
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, 0, bytes.length);
    }

    /**
     * Convert bytes to string.
     * 
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public static String toString(byte[] bytes, int offset, int length) {
        try {
            return new String(bytes, offset, length, CHARSET);
        } catch (UnsupportedEncodingException e) {
            // This shouldn't happen, we have hardcoded charset.
            throw new InternalServerError(e);
        }
    }

    /**
     * Convert BinaryString array to String array.
     * 
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public static String[] toStringArray(BinaryString[] binaryStrings) {
        if (binaryStrings != null) {

            String[] strings = new String[binaryStrings.length];

            for (int i = 0; i < binaryStrings.length; i++)
                strings[i] = binaryStrings[i].toString();

            return strings;
        }
        
        return null;

    }

    /**
     * Convert String array to BinaryString array.
     * 
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public static BinaryString[] toBinaryStrings(String[] strings) {
        if (strings != null) {

            BinaryString[] binaryStrings = new BinaryString[strings.length];

            for (int i = 0; i < strings.length; i++)
                binaryStrings[i] = new BinaryString(strings[i]);

            return binaryStrings;
        }
        
        return null;

    }

}
