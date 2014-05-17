package jredis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jredis.exception.InternalServerError;
import jredis.exception.InvalidFileFormat;
import jredis.rdf.Loader;

/**
 * The server class.
 * 
 * Known mismatches in behavior of Redis with jRedis.
 * 
 * 1. In Redis, A SET with both PX and EX options at the same time will
 * invalidate the SET operation. While jRedis will use the last specified
 * option.
 * 
 * 2. Error messages/types in jRedis is not consistent with that in Redis.
 * 
 * 3. Loading/Saving data saved using LZF compression.
 * 
 * @author anoopelias
 * 
 */
public class Server {

    public static final String DATA_DUMP = "data";

    public static final String JREDIS_CONFIG = "jredis.config";

    private ExecutorService service;

    private long reqId;

    private static int port;
    private static Properties props;

    public static Server INSTANCE = new Server();

    /**
     * One server instance per JVM.
     * 
     */
    private Server() {
        init();
    }

    /**
     * Initialize server.
     * 
     */
    private void init() {
        try {
            loadConfig();

            loadData();
            loadPort();
            loadPool();

            addShutdownHook();

        } catch (Throwable e) {
            Logger.info("Fatal : Exception during startup");
            Logger.info("Fatal : Aborting");
            throw new InternalServerError(e);
        }
    }

    /**
     * Psvm to start the server.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        INSTANCE.start();
    }

    /**
     * Load data from file to server.
     * 
     */
    private void loadData() {
        String rdfFileName = config(DATA_DUMP);
        if (rdfFileName != null) {
            File rdfFile = new File(rdfFileName);
            if (rdfFile.exists()) {
                try {

                    InputStream is = new FileInputStream(rdfFile);
                    new Loader(is).load();

                    is.close();

                } catch (FileNotFoundException e) {
                    // Ignore. This cannot happen.
                } catch (InvalidFileFormat | IOException e) {
                    Logger.info("Couldn't load data : " + e.getMessage());
                    Logger.info("Ignoring the error");
                }
            }
        }

    }

    /**
     * Load server configuration.
     * 
     * @throws IOException
     */
    private void loadConfig() throws IOException {
        InputStream config = null;
        String configFile = System.getProperty(JREDIS_CONFIG);
        if (configFile != null) {
            config = loadConfig(configFile);
        }

        if (config == null) {
            config = loadDefaultConfig();
        }

        props = new Properties();
        props.load(config);
    }

    /**
     * Load default config.
     * 
     * @return
     */
    private InputStream loadDefaultConfig() {
        InputStream config;
        System.out.println("Using default config");
        config = this.getClass().getClassLoader()
                .getResourceAsStream("jredis/config_default.properties");
        return config;
    }

    /**
     * Load the given config file. Returns null if file doens't exist.
     * 
     * @param configFile
     * @return
     */
    private InputStream loadConfig(String configFile) {
        InputStream config = null;
        System.out.println("Using config file : " + configFile);
        try {
            config = new FileInputStream(configFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found : " + configFile);
        }
        return config;
    }

    /**
     * Start the server.
     * 
     */
    public void start() {
        Logger.info("Server starting on port " + port + " ...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Processor processor = new Processor(clientSocket, reqId++);
                service.submit(processor);
            }

        } catch (Throwable e) {
            Logger.info("Fatal issue with server. Stopping server.");
            Logger.info(e);
        }
    }

    /**
     * Get a specific config value.
     * 
     * @param key
     * @return
     */
    public static String config(String key) {
        return props.getProperty(key);
    }

    /**
     * Load a thread pool.
     * 
     */
    private void loadPool() {
        service = Executors
                .newFixedThreadPool(Integer.parseInt(config("pool")));
    }

    /**
     * Load the port to which the server listens to.
     * 
     */
    private void loadPort() {
        port = Integer.parseInt(config("port"));
    }

    /**
     * Add a shutdown hook.
     * 
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));

    }

}
