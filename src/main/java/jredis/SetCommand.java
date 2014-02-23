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

        if (args.length == 3) {
            if ("NX".equals(args[2]))
                isNx = true;
            else if ("XX".equals(args[2]))
                isXx = true;
            else
                throw new InvalidCommand("Unknown argument :" + args[2]);
        }

    }

    @Override
    public String execute() {
        if (isNx) {
            set(true);
        } else if (isXx) {
            set(false);
        } else {
            set();
        }
        
        return "OK";
    }

    private void set(boolean condition) {
        synchronized (DataMap.INSTANCE) {
            if ((DataMap.INSTANCE.get(key) == null) == condition) {
                set();
            }
        }
    }

    private void set() {
        DataMap.INSTANCE.put(key, value);
    }
}
