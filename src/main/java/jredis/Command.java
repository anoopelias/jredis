package jredis;

import jredis.exception.InvalidCommand;

public interface Command {
    
    public String execute() throws InvalidCommand;
    
}
