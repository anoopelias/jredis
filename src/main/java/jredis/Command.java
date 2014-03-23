package jredis;

import jredis.exception.InvalidCommand;

/**
 * A Command can handle all incoming commands.
 * 
 * @author anoopelias
 *
 * @param <T>
 */
public interface Command<T> {
    
    /**
     * Execute the command.
     * 
     * @return Response to a command.
     * @throws InvalidCommand
     */
    public Response<T> execute() throws InvalidCommand;
    
}
