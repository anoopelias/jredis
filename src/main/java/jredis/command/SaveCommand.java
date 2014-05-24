package jredis.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jredis.DB;
import jredis.Server;
import jredis.data.Response;
import jredis.data.ResponseOk;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;
import jredis.rdf.Saver;

/**
 * To save the contents of memory to file system.
 * 
 * @author anoopelias
 * 
 */
public class SaveCommand implements Command<String> {
    
    /**
     * Constructor with no args expected.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public SaveCommand(BinaryString[] args) throws InvalidCommand {
        if(args != null && args.length > 0)
            throw new InvalidCommand("Invalid args");
    }


    @Override
    public Response<String> execute() throws InvalidCommand {

        try {
            
            // Complete blocking call
            synchronized (DB.INSTANCE) {
                
                File rdfFile = new File(Server.config(Server.DATA_DUMP));
                if (rdfFile.exists())
                    rdfFile.delete();

                OutputStream os = new FileOutputStream(rdfFile);
                Saver saver = new Saver(os);
                saver.save();
                
                os.close();
                
            }
            
        } catch (FileNotFoundException e) {
            throw new InvalidCommand("Couldn't create file", e);
        } catch (IOException e) {
            throw new InvalidCommand("Couldn't save to file", e);
        }

        return new ResponseOk();
    }

}
