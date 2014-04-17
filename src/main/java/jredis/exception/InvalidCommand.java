package jredis.exception;


/**
 * An invalid command error.
 * 
 * @author anoopelias
 *
 */
public class InvalidCommand extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5247037045013971682L;
    
    public InvalidCommand(String message) {
        super(message);
    }

    public InvalidCommand(String message, Throwable e) {
        super(message, e);
    }

}
