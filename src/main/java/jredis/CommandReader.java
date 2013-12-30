package jredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jredis.exception.InvalidCommand;

public class CommandReader {

    private BufferedReader reader = null;

    public CommandReader(InputStream is) throws InvalidCommand {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public Command next() throws InvalidCommand {
        
        Command c = null;
        
        try {
            String str = reader.readLine();

            if (str == null)
                return null;

            int argLen = readArgLen(str);
            c = readCommand(c);
            
            for(int i=0; i<argLen - 1; i++) {
                reader.readLine();
                reader.readLine();
            }

        } catch (IOException e) {
            throw new InvalidCommand();
        }
        
        return c;
    }

    private Command readCommand(Command c) throws IOException, InvalidCommand {
        String str;
        // Byte size of Command
        str = reader.readLine();
        if(str == null)
            throw new InvalidCommand();

        // Command
        str = reader.readLine();
        if(str == null)
            throw new InvalidCommand();
        
        return CommandFactory.INSTANCE.createCommand(str, null);
    }

    private int readArgLen(String str) throws InvalidCommand {
        if (str == null)
            throw new InvalidCommand();

        if (str.charAt(0) != '*')
            throw new InvalidCommand();
        
        if (!Character.isDigit(str.charAt(1)))
            throw new InvalidCommand();

        return Character.getNumericValue(str.charAt(1));

    }

}
