package jredis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jredis.exception.InvalidCommand;

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
    public SaveCommand(String[] args) throws InvalidCommand {
        if(args != null && args.length > 0)
            throw new InvalidCommand("Invalid args");
    }


    @Override
    public Response<String> execute() throws InvalidCommand {
        File rdfFile = new File(Server.config("data"));
        if (rdfFile.exists())
            rdfFile.delete();

        try {
            OutputStream os = new FileOutputStream(rdfFile);
            Saver saver = new Saver(os);
            saver.save();
            
            os.close();

        } catch (FileNotFoundException e) {
            throw new InvalidCommand("Couldn't create file", e);
        } catch (IOException e) {
            throw new InvalidCommand("Couldn't save to file", e);
        }

        return new ResponseOk();
    }

}
