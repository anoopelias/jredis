package jredis;

import java.util.Set;

import jredis.exception.InvalidCommand;

/**
 * Handle the logic of ZRANGE.
 * 
 * @author anoopelias
 * 
 */
public class ZrangeCommand implements Command<ElementRange> {

    private String key;
    private int start;
    private int stop;
    private boolean withScores;

    /**
     * Construct command from arguments.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public ZrangeCommand(String[] args) throws InvalidCommand {

        if (!(args.length == 3 || args.length == 4))
            throw new InvalidCommand("Invalid args");

        try {
            this.key = args[0];
            this.start = Integer.parseInt(args[1]);
            this.stop = Integer.parseInt(args[2]);

        } catch (NumberFormatException e) {
            throw new InvalidCommand("Unparsable integer");
        }

        if (args.length == 4)
            if ("WITHSCORES".equalsIgnoreCase(args[3]))
                withScores = true;
            else
                throw new InvalidCommand("Invalid fourth argument");

    }

    public ZrangeCommand(BinaryString[] args) throws InvalidCommand {
        this(Protocol.toStringArray(args));
    }

    @Override
    public Response<ElementRange> execute() throws InvalidCommand {
        synchronized (DB.INSTANCE) {
            ElementSet set = ZsetHelper.get(key);
            if (set == null)
                return new ResponseElementRange();

            if (start < 0)
                start += set.size();

            if (stop < 0)
                stop += set.size();

            Set<Element> rangeElements = set.subsetByRank(start, stop);
            ElementRange elementRange = new ElementRange(rangeElements,
                    withScores);
            return new ResponseElementRange(elementRange);
        }
    }

}
