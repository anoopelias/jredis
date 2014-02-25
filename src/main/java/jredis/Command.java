package jredis;

import jredis.exception.InvalidCommand;

public interface Command<T> {
    
    public Response<T> execute() throws InvalidCommand;
    
}
