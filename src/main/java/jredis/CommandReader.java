package jredis;

import static jredis.Protocol.CR;
import static jredis.Protocol.DOLLAR;
import static jredis.Protocol.LF;
import static jredis.Protocol.STAR;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jredis.exception.InternalServerError;
import jredis.exception.InvalidCommand;

/**
 * Read a command according to the protocol and return the command object.
 * 
 * @author anoopelias
 * 
 */
public class CommandReader {

    private BufferedInputStream stream = null;

    /**
     * Construct a command reader from input stream.
     * 
     * @param is
     * @throws InvalidCommand
     */
    public CommandReader(InputStream is) throws InvalidCommand {
        this.stream = new BufferedInputStream(is);
    }

    /**
     * Return the next command. 'null' if its over.
     * 
     * @return
     * @throws InvalidCommand
     */
    public Command<?> next() throws InvalidCommand {

        try {

            if (!hasNext())
                return null;

            String[] command = command(len());

            String type = command[0];
            String[] args = Arrays.copyOfRange(command, 1, command.length);

            return CommandFactory.INSTANCE.createCommand(type, args);

        } catch (IOException e) {
            throw new InternalServerError("IO Exception during reading the command");
        } catch (NumberFormatException e) {
            throw new InvalidCommand("Number expected");
        }

    }

    /**
     * Check if the stream is already closed.
     * 
     * @return
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        stream.mark(1);
        if (stream.read() == -1)
            return false;

        stream.reset();
        return true;
    }

    /**
     * Read the length of the args from the command stream.
     * 
     * @return
     * @throws InvalidCommand
     * @throws IOException
     */
    private int len() throws InvalidCommand, IOException {
        ch(STAR);
        return num();
    }

    /**
     * Get all the command params.
     * 
     * TODO : There is an obvious advantage in returning a ByteString[] rather
     * than a String[] here. It will avoid double conversion.
     * 
     * @param len
     * @return
     * @throws IOException
     * @throws InvalidCommand
     */
    private String[] command(int len) throws IOException, InvalidCommand {
        String[] command = new String[len];
        for (int i = 0; i < len; i++)
            command[i] = arg();

        return command;
    }

    /**
     * Read an argument.
     * 
     * @return
     * @throws IOException
     * @throws InvalidCommand
     */
    private String arg() throws IOException, InvalidCommand {
        ch(DOLLAR);
        byte[] b = new byte[num()];

        stream.read(b);
        crlf();

        return Protocol.toString(b);
    }

    /**
     * Check if the next two chars belong to CRLF.
     * 
     * @throws IOException
     * @throws InvalidCommand
     */
    private void crlf() throws IOException, InvalidCommand {
        ch(CR);
        ch(LF);
    }

    /**
     * Read a number from stream ending with CRLF.
     * 
     * @return
     * @throws IOException
     * @throws InvalidCommand
     */
    private int num() throws IOException, InvalidCommand {
        char c;
        StringBuilder len = new StringBuilder();
        while ((c = (char) stream.read()) != CR)
            len.append(c);

        ch(LF);
        return Integer.parseInt(len.toString());
    }

    /**
     * Check if the next character in the stream is the one provided.
     * 
     * @param ch
     * @throws IOException
     * @throws InvalidCommand
     */
    private void ch(byte ch) throws IOException, InvalidCommand {
        if (stream.read() != ch)
            throw new InvalidCommand("Disagreement to Command Protocol");
    }

}
