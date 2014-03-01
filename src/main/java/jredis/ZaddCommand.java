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
        
        boolean inserted;
        
        synchronized(DataMap.INSTANCE) {
            ElementSet map = DataMap.INSTANCE.get(key, ElementSet.class);
            if(map == null) {
                map = new TreeElementSet();
                DataMap.INSTANCE.put(key, map);
            }
            
            inserted = map.insert(new Element(value, score));
        }
        
        return new ResponseNumber(inserted? 1 : 0);
    }

}
