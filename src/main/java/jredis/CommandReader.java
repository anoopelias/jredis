package jredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jredis.exceptions.InvalidCommand;

public class CommandReader {

    private BufferedReader reader = null;

    public CommandReader(InputStream is) throws InvalidCommand {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public Command next() throws InvalidCommand {
        try {
            String str = reader.readLine();

            if (str == null)
                return null;

            int argLen = readArgLen(str);

        } catch (IOException e) {
            throw new InvalidCommand();
        }

        return null;
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
