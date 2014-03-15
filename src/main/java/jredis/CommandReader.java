package jredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import jredis.exception.InvalidCommand;

/**
 * Read a command according to the protocol and return the command object.
 * 
 * @author anoopelias
 * 
 */
public class CommandReader {

    private BufferedReader reader = null;

    public CommandReader(InputStream is) throws InvalidCommand {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public Command<?> next() throws InvalidCommand {

        try {
            int len;

            if ((len = len()) == -1)
                return null;

            String[] args = args(len);

            return CommandFactory.INSTANCE.createCommand(args[0],
                    Arrays.copyOfRange(args, 1, args.length));

        } catch (IOException e) {
            throw new InvalidCommand("IO Exception during reading the command");
        }

    }

    private String[] args(int len) throws IOException {
        String[] args = new String[len];
        for (int i = 0; i < len; i++) {
            // Byte size of the arg
            reader.readLine();

            // Actual value of the arg
            args[i] = reader.readLine();
        }
        return args;
    }

    private int len() throws InvalidCommand, IOException {

        String str = reader.readLine();

        if (str == null)
            return -1;

        if (str.charAt(0) != '*')
            throw new InvalidCommand("Disagreement to Command Protocol");

        if (!Character.isDigit(str.charAt(1)))
            throw new InvalidCommand("Disagreement to Command Protocol");

        return Character.getNumericValue(str.charAt(1));

    }

}
