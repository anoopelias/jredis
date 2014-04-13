package jredis.exception;

public class InternalServerError extends RuntimeException {

    public InternalServerError() {
        super();
    }

    public InternalServerError(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerError(String message) {
        super(message);
    }

    public InternalServerError(Throwable cause) {
        super(cause);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1780894364369354793L;

}
