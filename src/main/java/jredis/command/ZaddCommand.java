package jredis.command;

import jredis.DB;
import jredis.Protocol;
import jredis.data.Response;
import jredis.data.ResponseNumber;
import jredis.domain.BinaryString;
import jredis.domain.Element;
import jredis.domain.ElementSet;
import jredis.domain.TreeElementSet;
import jredis.exception.InvalidCommand;

public class ZaddCommand implements Command<Integer> {

    private BinaryString key;
    private double score;
    private BinaryString value;

    public ZaddCommand(BinaryString[] args) throws InvalidCommand {
        if (args.length != 3)
            throw new InvalidCommand("Invalid arg length");
        
        try {
            key = args[0];
            score = Protocol.parseDouble(args[1].toString());
            value = args[2];
        } catch (NumberFormatException e) {
            throw new InvalidCommand("Unparsable score");
        }
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {

        boolean inserted;

        synchronized (DB.INSTANCE) {
            ElementSet map = ZsetHelper.get(key);
            if (map == null) {
                map = new TreeElementSet();
                DB.INSTANCE.put(key, map);
            }

            inserted = map.insert(new Element(value.toString(), score));
        }

        return new ResponseNumber(inserted ? 1 : 0);
    }

}
