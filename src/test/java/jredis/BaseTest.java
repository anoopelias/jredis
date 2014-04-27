package jredis;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import jredis.nft.Timer;

public class BaseTest {

    private static final int SERVER_START_TIMEOUT = 2000; // mSecs
    protected static String HOST = "localhost";
    protected static int PORT = 15000;

    /**
     * Start the server.
     * 
     */
    protected void startServer() {

        /*
         * Since there is no mechanism to stop the server right now, we will
         * start the server only if it is not already started. This will enable
         * us to run multiple server test cases.
         * 
         */

        if (!isStarted()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Server.INSTANCE.start();
                }
            }).start();
            waitForServerStartup();
        }

    }

    private void waitForServerStartup() {
        Timer timer = new Timer();
        while (!isStarted() && timer.milliTime() < SERVER_START_TIMEOUT) {

        }

        if (!isStarted())
            throw new AssertionError("Failed to start server in time");
    }

    private boolean isStarted() throws AssertionError {
        try (Socket kkSocket = new Socket(HOST, PORT)) {
            return true;
        } catch (UnknownHostException e) {
            throw new AssertionError("Unknown Host", e);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Stop server.
     */
    protected void stopServer() {
        // Do nothing.

    }

}
