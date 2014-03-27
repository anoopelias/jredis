package jredis;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

import jredis.exception.InvalidCommand;

/**
 * A thread which processes one socket client.
 * 
 * @author anoopelias
 * 
 */
public class Processor implements Callable<Object> {

    private Socket socket = null;

    private long reqId;

    /**
     * Construct the processor.
     * 
     * @param clientSocket
     * @param reqId
     *            unique identifier for each new socket.
     */
    public Processor(Socket clientSocket, long reqId) {
        this.socket = clientSocket;
        this.reqId = reqId;
    }

    @Override
    public Object call() throws Exception {

        try {
            CommandReader reader = new CommandReader(socket.getInputStream());
            ResponseWriter writer = new ResponseWriter(socket.getOutputStream());

            while (reader.hasNext()) {
                try {
                    Command<?> c = reader.next();
                    c.execute().write(writer);

                } catch (InvalidCommand e) {
                    /*
                     * In case of invalid command, we write the error back to
                     * the client and wait for next command.
                     */

                    writer.write(e);
                }
            }
            socket.close();

        } catch (IOException e) {

            if (Server.isDebug()) {
                System.out.println("Some I/O problem with connection :" + reqId
                        + " Ending the connection.");
                e.printStackTrace();
            }
        }

        return null;
    }

}
