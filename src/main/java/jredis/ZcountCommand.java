package jredis;

import jredis.exception.InvalidCommand;

/**
 * Implementation of Zcount command.
 * 
 * @author anoopelias
 *
 */
public class ZcountCommand implements Command<Integer> {

    private String key;
    private Double from;
    private Double to;

    /**
     * Construct command from arguments.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public ZcountCommand(String[] args) throws InvalidCommand {
        key = args[0];
        from = parse(args[1]);
        to = parse(args[2]);
    }

    private double parse(String arg) throws InvalidCommand {
        try {
            return Utils.parseDouble(arg);
            
        } catch (NumberFormatException e) {
            throw new InvalidCommand("Unknown argument : " + arg);

        }
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        synchronized (DataMap.INSTANCE) {
            ElementSet set = DataMap.INSTANCE.get(key,
                    ElementSet.class);
            if (set == null)
                return new ResponseNumber(0);

            int count = set.subsetSizeByScore(from, to);
            return new ResponseNumber(count);
        }

    }

}