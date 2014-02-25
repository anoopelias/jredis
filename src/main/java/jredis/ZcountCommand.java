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
            ValueSortedMap map = DataMap.INSTANCE.get(key, ValueSortedMap.class);
            if(map == null)
                return new ResponseNumber(0);
            
        }
        
        return null;
    }

}
