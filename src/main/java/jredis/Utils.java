package jredis;


/**
 * Utility methods
 * 
 * @author anoopelias
 *
 */
public class Utils {
    
    // So that the class won't get instantiated.
    private Utils() {
        
    }
    
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

}
