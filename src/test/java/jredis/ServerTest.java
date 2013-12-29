package jredis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class ServerTest {

    private static String HOST = "localhost";
    private static int PORT = 15000;

    // @Before
    public void setup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new Server();
                server.start();
            }
        }).start();
    }

    @Test
    public void test_server_start() throws UnknownHostException, IOException {

        try (Socket kkSocket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(),
                        true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        kkSocket.getInputStream()));) {

            out.println("READ A");
            System.out.println(in.readLine());
            out.println("WRITE D");
            System.out.println(in.readLine());

        }
    }

}
