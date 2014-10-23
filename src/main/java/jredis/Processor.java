package jredis;

import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jredis.command.Command;
import jredis.command.QuitCommand;
import jredis.data.Response;
import jredis.exception.InvalidCommand;

/**
 * A thread which processes one socket client.
 * 
 * @author anoopelias
 * 
 */
public class Processor implements Callable<Object> {

    private Socket socket = null;
    
    private static final ExecutorService service = Executors.newSingleThreadExecutor();

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
                    final Command<?> c = reader.next();
                    
                    Response<?> response = exec(c);
                    response.write(writer);
                    
                    Logger.debug("Execution complete : " + response);
                    
                    if(c instanceof QuitCommand)
                        break;

                } catch (InvalidCommand e) {
                    
                    Logger.debug("Invalid Command : " + e.getMessage());
                    /*
                     * In case of invalid command, we write the error back to
                     * the client and wait for next command.
                     */

                    writer.write(e);
                }
            }
            socket.close();

        } catch (Throwable e) {
            // We don't let any exceptions escape.
            Logger.debug("Some unknown problem :" + reqId
                    + " Ending the connection.", e);
        }

        return null;
    }

    private Response<?> exec(final Command<?> c) throws InvalidCommand {
        try {
            return service.submit(new Callable<Response<?>>() {

                @Override
                public Response<?> call() throws Exception {
                    return c.execute();
                }
            }).get();
        } catch (InterruptedException e) {
            throw new InvalidCommand("Thread interrupted", e);
        } catch (ExecutionException e) {
            if(e.getCause() != null && e.getCause() instanceof InvalidCommand)
                throw (InvalidCommand)e.getCause();
            throw new InvalidCommand("Execution exception", e);
        }
    }

}
