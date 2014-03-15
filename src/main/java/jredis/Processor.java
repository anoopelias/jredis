package jredis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private Socket clientSocket = null;

    private long reqId;

    /**
     * Construct the processor.
     * 
     * @param clientSocket
     * @param reqId
     *            unique identifier for each new socket.
     */
    public Processor(Socket clientSocket, long reqId) {
        this.clientSocket = clientSocket;
        this.reqId = reqId;
    }

    @Override
    public Object call() throws Exception {

        try {

            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();

            CommandReader reader = new CommandReader(is);
            ResponseWriter writer = new ResponseWriter(os);

            while (true) {
                try {
                    Command<?> c = reader.next();
                    if (c == null)
                        break;

                    c.execute().write(writer);

                } catch (InvalidCommand e) {
                    /*
                     * In case of invalid command, we write the error back to
                     * the client and wait for next command.
                     */

                    writer.write(e);
                }
            }
            clientSocket.close();

        } catch (IOException e) {

            if (Server.isDebug()) {
                System.out.println("Some I/O problem with request :" + reqId
                        + " Ending the request.");
                e.printStackTrace();
            }
        }

        return null;
    }

}
