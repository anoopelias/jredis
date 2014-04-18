package jredis;

import jredis.exception.InternalServerError;
import jredis.exception.InvalidCommand;

public class Shutdown implements Runnable {

    @Override
    public void run() {
        Server.debug("Initializing shutdown");
        
        try {
            CommandFactory.INSTANCE.createCommand("SAVE", null).execute();
            
        } catch (InvalidCommand e) {
            throw new InternalServerError(e);
        }

        Server.debug("Shutdown complete");
    }
    
}
