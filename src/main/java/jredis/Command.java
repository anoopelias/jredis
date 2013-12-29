package jredis;

import jredis.exceptions.InvalidCommand;

public interface Command {
    
    public String execute() throws InvalidCommand;
    
}
