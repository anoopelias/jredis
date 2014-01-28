package jredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {

    private static final int SERVER_START_TIMEOUT = 1000; // mSecs
    
    private static String HOST = "localhost";
    private static int PORT = 15000;

    @Before
    public void setup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new Server();
                server.start();
            }
        }).start();

        waitForServerStartup();
    }

    private void waitForServerStartup() {
        Socket tempSocket = null;
        Timer timer = new Timer();
        while (tempSocket == null && timer.time() < SERVER_START_TIMEOUT) {
            
            try (Socket kkSocket = new Socket(HOST, PORT)) {
                tempSocket = kkSocket;
            } catch (UnknownHostException e) {
                throw new AssertionError("Unknown Host", e);
            } catch (IOException e) {
                // Eat the exception.
            }
        }

        if (tempSocket == null)
            throw new AssertionError("Failed to start server in time");
    }

    @Test
    public void test_server_request() throws UnknownHostException, IOException {

        try (Socket kkSocket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(),
                        true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        kkSocket.getInputStream()));) {

            out.println("SET A 1");
            System.out.println(in.readLine());
            out.println("WRITE D");
            System.out.println(in.readLine());

        }
    }

}
