package jredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import jredis.exception.InvalidCommand;

public class CommandReader {

    private BufferedReader reader = null;

    public CommandReader(InputStream is) throws InvalidCommand {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public Command next() throws InvalidCommand {

        try {
            int argLen;
            
            if ((argLen = readArgLen()) == -1)
                return null;
            
            String[] args = readArgs(argLen);

            return CommandFactory.INSTANCE.createCommand(args[0], Arrays.copyOfRange(args, 1, args.length));

        } catch (IOException e) {
            throw new InvalidCommand();
        }

    }

    private String[] readArgs(int argLen) throws IOException {
        String[] args = new String[argLen];
        for (int i = 0; i < argLen; i++) {
            // Byte size of the arg
            reader.readLine();

            // Actual value of the arg
            args[i] = reader.readLine();
        }
        return args;
    }

    private int readArgLen() throws InvalidCommand, IOException {

        String str = reader.readLine();

        if (str == null)
            return -1;

        if (str.charAt(0) != '*')
            throw new InvalidCommand();

        if (!Character.isDigit(str.charAt(1)))
            throw new InvalidCommand();

        return Character.getNumericValue(str.charAt(1));

    }

}
