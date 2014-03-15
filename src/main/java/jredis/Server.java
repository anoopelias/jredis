package jredis;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The server class.
 * 
 * Known mismatches in behavior of Redis with jRedis. 
 * 
 * 1. In Redis, SET store the string in bit format. Now a GETBIT will return the 
 * corresponding bits in original string. Contrary to this, a SET / GETBIT combination 
 * will throw an error in jRedis.
 * 
 * 2. In Redis, A SET with both PX and EX options at the same time will invalidate the SET
 * operation. While jRedis will use the last specified option.
 * 
 * 3. Error messages/types in jRedis is not consistent with that in Redis.
 * 
 * @author anoopelias
 * 
 */
public class Server {

    private ExecutorService service = Executors.newFixedThreadPool(5);

    private long reqId;

    /**
     * Psvm to start the server.
     * 
     * @param args
     */
    public static void main(String[] args) {
        new Server().start();
    }

    /**
     * Start the server.
     * 
     */
    public void start() {
        System.out.println("Server starting..");
        try (ServerSocket serverSocket = new ServerSocket(15000)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Processor processor = new Processor(clientSocket, reqId++);
                service.submit(processor);
            }

        } catch (Throwable e) {
            System.out.println("Fatal issue with server. Stopping server.");
            if (Server.isDebug())
                e.printStackTrace();
        }
    }

    /**
     * Check if the server is in debug mode.
     * 
     * @return
     */
    public static boolean isDebug() {

        // TODO: Get some jvm args to set this.
        return true;
    }

}
