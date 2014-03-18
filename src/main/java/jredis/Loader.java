package jredis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

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

    private BufferedInputStream stream;

    public Loader(InputStream is) {
        stream = new BufferedInputStream(is);
    }

    /**
     * Load the data from stream to server.
     * 
     * @throws InvalidFileFormat
     * 
     */
    public void load() throws InvalidFileFormat {
        try {
            verifyInit();

            while (hasNext())
                loadValue();

        } catch (IOException e) {
            throw new InvalidFileFormat("Error reading file", e);
        }

    }

    /**
     * Check if the end of file has reached.
     * 
     * @return
     * @throws IOException
     */
    private boolean hasNext() throws IOException {
        stream.mark(1);
        if (stream.read() == 0xff)
            return false;

        stream.reset();
        return true;
    }

    /**
     * Verify the the file starts in the correct format.
     * 
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private void verifyInit() throws IOException, InvalidFileFormat {
        byte[] init = new byte[INIT.length];
        stream.read(init);
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
        int type = stream.read();

        long time = -1;

        /*
         * We are not supporting 0xfd as we are using only mills to save
         * timestamp. (So does Redis, it seems)
         */
        if (type == 0xfc) {
            time = readTime();
            type = stream.read();
        }

        if (type != 0)
            throw new InvalidFileFormat("Unsupported content");

        String key = readString();
        TimedString value = readValue(time);

        if (value.isValid())
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
    private TimedString readValue(long time) throws IOException,
            InvalidFileFormat {
        String valString = readString();

        TimedString value;
        if (time != -1)
            value = new TimedString(valString, time);
        else
            value = new TimedString(valString);
        return value;
    }

    /**
     * Read 8 byte unix time in milliseconds from stream.
     * 
     * @return
     * @throws IOException
     */
    private long readTime() throws IOException {
        byte[] bTime = new byte[8];
        stream.read(bTime);
        return ByteBuffer.wrap(bTime).order(Protocol.ENDIAN).getLong();
    }

    /**
     * Read a string value from stream.
     * 
     * TODO: integer / LZF compression.
     * 
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private String readString() throws IOException, InvalidFileFormat {
        byte[] b = new byte[stream.read()];
        stream.read(b);
        return new String(b, Protocol.CHARSET);
    }

}
