package jredis;

import jredis.exception.InvalidCommand;

public class ZcountCommand implements Command<Integer> {
    
    private String key;
    private Double from;
    private Double to;
    
    public ZcountCommand(String[] args) {
        key = args[0];
        from = Double.parseDouble(args[1]);
        to = Double.parseDouble(args[2]);
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        synchronized (DataMap.INSTANCE) {
            SortedElementSet set = DataMap.INSTANCE.get(key, SortedElementSet.class);
            if(set == null)
                return new ResponseNumber(0);
            
            int count = set.subList(from, to).size();
            return new ResponseNumber(count);
        }
        
    }

}
