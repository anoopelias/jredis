package jredis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(
                                clientSocket.getOutputStream(), true)) {
                    
                    String command;
                    while ((command = in.readLine()) != null) {
                        System.out.println("Command " + command);
                        out.println("Responding to " + command);
                    }
                }
            }

        } catch (Throwable e) {
            // TODO : Exception Handling
            System.out.println("Internal Server error");
        }
    }

}
