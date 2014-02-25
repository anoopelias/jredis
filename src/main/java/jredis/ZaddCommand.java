package jredis;

import jredis.exception.InvalidCommand;

public class ZaddCommand implements Command<Integer> {

    private String key;
    private double score;
    private String value;

    public ZaddCommand(String[] args) {
        key = args[0];
        score = Double.parseDouble(args[1]);
        value = args[2];
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        
        boolean insert;
        synchronized(DataMap.INSTANCE) {
            ValueSortedMap map = DataMap.INSTANCE.get(key, ValueSortedMap.class);
            if(map == null) {
                map = new ValueSortedMap();
                DataMap.INSTANCE.put(key, map);
            }
            
            insert = (map.put(value, score) == null);
        }
        
        return new ResponseNumber(insert? 1 : 0);
    }

}
