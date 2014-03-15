package jredis;

import static jredis.Utils.CR;
import static jredis.Utils.DOLLAR;
import static jredis.Utils.LF;
import static jredis.Utils.STAR;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jredis.exception.InvalidCommand;

/**
 * Read a command according to the protocol and return the command object.
 * 
 * @author anoopelias
 * 
 */
public class CommandReader {

    private BufferedInputStream stream = null;

    public CommandReader(InputStream is) throws InvalidCommand {
        this.stream = new BufferedInputStream(is);
    }

    public Command<?> next() throws InvalidCommand {

        try {

            if (!hasNext())
                return null;

            String[] command = command(len());
            
            String type = command[0];
            String[] args = Arrays.copyOfRange(command, 1, command.length);

            return CommandFactory.INSTANCE.createCommand(type, args);

        } catch (IOException e) {
            throw new InvalidCommand("IO Exception during reading the command");
        } catch (NumberFormatException e) {
            throw new InvalidCommand("Number expected");
        }

    }

    private boolean hasNext() throws IOException {
        stream.mark(1);
        if (stream.read() == -1)
            return false;

        stream.reset();
        return true;
    }

    private String[] command(int len) throws IOException, InvalidCommand {
        String[] command = new String[len];
        for (int i = 0; i < len; i++)
            command[i] = arg();

        return command;
    }

    private String arg() throws IOException, InvalidCommand {
        ch(DOLLAR);
        byte[] b = new byte[num()];

        stream.read(b);
        crlf();

        return new String(b);
    }

    private void crlf() throws IOException, InvalidCommand {
        ch(CR);
        ch(LF);
    }

    private int len() throws InvalidCommand, IOException {
        ch(STAR);
        return num();
    }

    private int num() throws IOException, InvalidCommand {
        char c;
        StringBuilder len = new StringBuilder();
        while ((c = (char) stream.read()) != CR)
            len.append(c);

        ch(LF);
        return Integer.parseInt(len.toString());
    }

    private void ch(byte ch) throws IOException, InvalidCommand {
        if (stream.read() != ch)
            throw new InvalidCommand("Disagreement to Command Protocol");
    }

}
