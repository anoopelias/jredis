package jredis.exception;

import java.io.IOException;

import jredis.ResponseWriter;

public class InvalidCommand extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5247037045013971682L;
    
    public InvalidCommand(String message) {
        super(message);
    }
    
    public void write(ResponseWriter writer) throws IOException {
        writer.writeError(this.getMessage());
    }

}
