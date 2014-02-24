package jredis;

import jredis.exception.InvalidCommand;

public class SetCommand implements Command {

    private String key;
    private String value;

    private boolean isNx;
    private boolean isXx;

    public SetCommand(String[] args) throws InvalidCommand {
        validate(args);
    }

    private void validate(String[] args) throws InvalidCommand {
        if (args.length < 2)
            throw new InvalidCommand("Not enough args");

        key = args[0];
        value = args[1];
        
        for(int i=2; i<args.length; i++) {
            
            if ("NX".equals(args[i]))
                isNx = true;
            else if ("XX".equals(args[i]))
                isXx = true;
            else
                throw new InvalidCommand("Unknown argument :" + args[i]);
        }

    }

    @Override
    public String execute() {
        synchronized (DataMap.INSTANCE) {
            
            if (isNx && hasKey(key))
                return null;

            if (isXx && !hasKey(key))
                return null;

            DataMap.INSTANCE.put(key, value);
            return "OK";
        }
    }
    
    private boolean hasKey(String key) {
        return DataMap.INSTANCE.get(key) != null;
    }

}
