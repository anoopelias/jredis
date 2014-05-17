package jredis;

import jredis.exception.InternalServerError;
import jredis.exception.InvalidCommand;

public class Shutdown implements Runnable {

    @Override
    public void run() {
        Logger.debug("Initializing shutdown");
        
        try {
            CommandFactory.INSTANCE.createCommand("SAVE", null).execute();
            
        } catch (InvalidCommand e) {
            throw new InternalServerError(e);
        }

        Logger.debug("Shutdown complete");
    }
    
}
