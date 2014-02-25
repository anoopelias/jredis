package jredis;

import jredis.exception.InvalidCommand;

public class ZcardCommand implements Command<Integer> {
    
    private String key;
    
    public ZcardCommand(String[] args) {
        key = args[0];
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        synchronized(DataMap.INSTANCE) {
            ValueSortedMap map = DataMap.INSTANCE.get(key, ValueSortedMap.class);
            return new ResponseNumber((map == null) ? 0 : map.size());
        }
    }

}
