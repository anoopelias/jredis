package jredis;

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
        this.key = args[0];
        this.start = Integer.parseInt(args[1]);
        this.stop = Integer.parseInt(args[2]);

        if (args.length == 4 && "WITHSCORES".equals(args[3]))
            withScores = true;

    }

    @Override
    public Response<ElementRange> execute() throws InvalidCommand {
        synchronized (DataMap.INSTANCE) {
            ElementSet set = DataMap.INSTANCE.get(key, ElementSet.class);
            if (set == null)
                return new ResponseElementRange();
            
            if(start < 0)
                start += set.size();
            
            if(stop < 0)
                stop += set.size();

            
            Iterable<Element> rangeElements = set.subsetByRank(
                    start, stop);
            ElementRange elementRange = new ElementRange(rangeElements, withScores);
            return new ResponseElementRange(elementRange);
        }
    }

}
