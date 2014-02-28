package jredis;

import jredis.exception.InvalidCommand;

public class ZcountCommand implements Command<Integer> {

    private String key;
    private Double from;
    private Double to;

    public ZcountCommand(String[] args) throws InvalidCommand {
        key = args[0];
        from = parse(args[1]);
        to = parse(args[2]);
    }

    private double parse(String arg) throws InvalidCommand {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            
            if (arg.endsWith("inf")) {
                
                if (arg.length() == 3)
                    return Double.POSITIVE_INFINITY;

                if (arg.length() == 4) {
                    if (arg.charAt(0) == '+')
                        return Double.POSITIVE_INFINITY;
                    if (arg.charAt(0) == '-')
                        return Double.NEGATIVE_INFINITY;
                }
            }

            throw new InvalidCommand("Unknown argument : " + arg);

        }
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        synchronized (DataMap.INSTANCE) {
            SortedElementSet set = DataMap.INSTANCE.get(key,
                    SortedElementSet.class);
            if (set == null)
                return new ResponseNumber(0);

            int count = set.subList(from, to).size();
            return new ResponseNumber(count);
        }

    }

}
