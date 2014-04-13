package jredis.exception;

/**
 * To denote invalid format of a loading file.
 * 
 * @author anoopelias
 *
 */
public class InvalidFileFormat extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5129785358312708366L;

    public InvalidFileFormat(String message) {
        super(message);
    }

    public InvalidFileFormat(String message, Throwable e) {
        super(message, e);
    }

}
