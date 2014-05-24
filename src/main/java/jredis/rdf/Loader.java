package jredis.rdf;

import java.io.IOException;
import java.io.InputStream;

import jredis.DB;
import jredis.domain.BinaryString;
import jredis.domain.ElementSet;
import jredis.domain.TimedBinaryString;
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

    private RdfReader reader;

    public Loader(InputStream is) {
        reader = new RdfReader(is);
    }

    /**
     * Load the data from stream to server.
     * 
     * @throws InvalidFileFormat
     * 
     */
    public void load() throws InvalidFileFormat {
        try {

            reader.verifyInit();
            while (!reader.end())
                loadValue();

        } catch (IOException e) {
            throw new InvalidFileFormat("Error reading file", e);
        }

    }

    /**
     * Load next value based on type.
     * 
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private void loadValue() throws IOException, InvalidFileFormat {
        int valueType = reader.read();

        long time = -1;

        /*
         * We are not supporting 0xfd as we are using only mills to save
         * timestamp. (So does Redis, it seems)
         */
        if (valueType == 0xfc) {
            time = readTime();
            valueType = reader.read();
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
        BinaryString key = reader.readString();
        TimedBinaryString value = readValue(time);

        if (value.isValid())
            DB.INSTANCE.put(key, value);
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
        BinaryString key = reader.readString();
        ElementSet value = reader.readElementSet();
        DB.INSTANCE.put(key, value);
    }

    /**
     * Read a value and assign the given time to it.
     * 
     * @param time
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private TimedBinaryString readValue(long time) throws IOException,
            InvalidFileFormat {
        BinaryString valString = reader.readString();

        TimedBinaryString value;
        if (time != -1)
            value = new TimedBinaryString(valString, time);
        else
            value = new TimedBinaryString(valString);
        return value;
    }

    /**
     * Read 8 byte unix time in milliseconds from stream.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private long readTime() throws IOException, InvalidFileFormat {
        return reader.readLong();
    }

}
