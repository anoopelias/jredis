package jredis;

import java.io.IOException;
import java.io.InputStream;

import jredis.exception.InvalidFileFormat;

/**
 * Loader to load rdf format.
 * 
 * Based on
 * https://github.com/sripathikrishnan/redis-rdb-tools/wiki/Redis-RDB-Dump
 * -File-Format
 * 
 * @author anoopelias
 * 
 */
public class Loader {

    /**
     * Initialization structure of the file.
     * 
     * Version 6, database 00 is supported at the moment.
     * 
     * 'REDIS0006' 0xFE 0x00
     * 
     */
    private static final byte[] INIT = { 0x52, 0x45, 0x44, 0x49, 0x53, 0x30,
            0x30, 0x30, 0x36, (byte) 0xfe, 0x00 };

    private StreamReader stream;

    public Loader(InputStream is) {
        stream = new StreamReader(is);
    }

    /**
     * Load the data from stream to server.
     * 
     * @throws InvalidFileFormat
     * 
     */
    public void load() throws InvalidFileFormat {
        try {

            // Stop everything else while loading.
            synchronized (DataMap.INSTANCE) {
                verifyInit();

                while (!stream.end())
                    loadValue();
            }

        } catch (IOException e) {
            throw new InvalidFileFormat("Error reading file", e);
        }

    }

    /**
     * Verify the the file starts in the correct format.
     * 
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private void verifyInit() throws IOException, InvalidFileFormat {
        byte[] init = stream.read(INIT.length);

        for (int i = 0; i < INIT.length; i++) {
            if (INIT[i] != init[i])
                throw new InvalidFileFormat("Init structure do not match");
        }
    }

    /**
     * Load next value based on type.
     * 
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private void loadValue() throws IOException, InvalidFileFormat {
        int valueType = stream.read();

        long time = -1;

        /*
         * We are not supporting 0xfd as we are using only mills to save
         * timestamp. (So does Redis, it seems)
         */
        if (valueType == 0xfc) {
            time = readTime();
            valueType = stream.read();
        }

        if (valueType == 0) // Currently reads only String type.
            loadStringValue(time);
        else if (valueType == 12)
            loadElementSetValue(time);
        else
            throw new InvalidFileFormat("Unsupported content, type : "
                    + valueType);
    }

    /**
     * Load a ByteString in to data store.
     * 
     * @param time
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private void loadStringValue(long time) throws IOException,
            InvalidFileFormat {
        String key = stream.readString().toString();
        TimedByteString value = readValue(time);

        if (value.isValid())
            DataMap.INSTANCE.put(key, value);
    }

    /**
     * Load ElementSet in to data store.
     * 
     * @param time
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private void loadElementSetValue(long time) throws IOException,
            InvalidFileFormat {
        String key = stream.readString().toString();
        ElementSet value = stream.readElementSet();
        DataMap.INSTANCE.put(key, value);
    }

    /**
     * Read a value and assign the given time to it.
     * 
     * @param time
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private TimedByteString readValue(long time) throws IOException,
            InvalidFileFormat {
        ByteString valString = stream.readString();

        TimedByteString value;
        if (time != -1)
            value = new TimedByteString(valString, time);
        else
            value = new TimedByteString(valString);
        return value;
    }

    /**
     * Read 8 byte unix time in milliseconds from stream.
     * 
     * @return
     * @throws IOException
     */
    private long readTime() throws IOException {
        return stream.readLong();
    }

}
