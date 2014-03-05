package jredis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

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
     * @param reqId unique identifier for each new socket.
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
    
            Command<?> c;
            while ((c = reader.next()) != null) {
                c.execute().write(writer);
            }
            clientSocket.close();
            
        } catch (IOException e) {
            System.out.println("Some I/O problem with request :" + reqId + " Ending the request.");
            
            if(Server.isDebug())
                e.printStackTrace();
        }
        
        return null;
    }

}
