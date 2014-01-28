package jredis;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server class.
 * 
 * 
 * @author anoopelias
 * 
 */
public class Server {

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        System.out.println("Server starting..");
        try (ServerSocket serverSocket = new ServerSocket(15000)) {

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                        InputStream is = clientSocket.getInputStream();
                        OutputStream os = clientSocket.getOutputStream()) {

                    CommandReader reader = new CommandReader(
                            clientSocket.getInputStream());
                    
                    ResponseWriter writer = new ResponseWriter(os);

                    Command c;
                    while ((c = reader.next()) != null) {
                        writer.write("+" + c.execute() + "\r");
                    }
                }
            }

        } catch (Throwable e) {
            // TODO : Exception Handling
            System.out.println("Internal Server error");
            e.printStackTrace();
        }
    }

}
