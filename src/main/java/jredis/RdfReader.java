package jredis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import jredis.exception.InvalidFileFormat;

/**
 * Actively read the file spec objects from stream.
 * 
 * TODO: API needs cleanup.
 * 
 * @author anoopelias
 * 
 */
public class RdfReader {

    private InputStream stream;

    public RdfReader(InputStream is) {
        this.stream = new BufferedInputStream(is);
    }

    /**
     * Read an ElementSet.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    public ElementSet readElementSet() throws IOException, InvalidFileFormat {

        ElementSet elementSet = new TreeElementSet();

        // length of the string
        readLength(read());

        // zlbytes
        read(4);

        // zltail
        read(4);

        // zllen starts at index 8.
        int len = readShort();

        // Number of entries will be even for zset.
        if(len % 2 != 0)
            throw new InvalidFileFormat("Element set should have even number of entries");

        for (int i = 0; i < len / 2; i++)
            elementSet.insert(readElement());
        
        if(read() != 0xff)
            throw new InvalidFileFormat("Couldn't find end of zset");

        return elementSet;
    }

    /**
     * Read and element from the stream.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private Element readElement() throws IOException, InvalidFileFormat {

        try {

            // Ignoring previous length.
            readLenPrevEntry();

            ByteString member = readString();

            // Ignoring previous length.
            readLenPrevEntry();

            int spFlag = read();
            double score;
            if ((spFlag & 0xc0) == 0xc0)
                score = readNumber(spFlag);
            else
                score = Protocol.parseDouble(readString(spFlag).toString());

            return new Element(member.toString(), score);

        } catch (NumberFormatException e) {
            throw new InvalidFileFormat("Couldn't parse score.", e);
        }
    }

    /**
     * Read a number in the format of previous length.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private int readLenPrevEntry() throws IOException, InvalidFileFormat {
        int len = read();
        if (len != 0xfe)
            return len;
        else
            return readInt();

    }

    /**
     * Read a number from the stream.
     * 
     * @param spFlag
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    public long readNumber(int spFlag) throws IOException, InvalidFileFormat {

        switch (spFlag & 0x30) {
        case 0x00: // 2 bytes
            return readShort();
        case 0x10: // 4 bytes
            return readInt();
        case 0x20: // 8 bytes
            return readLong();
        case 0x30:
            switch (spFlag & (byte) 0x0F) {
            case 0x00: // 3 bytes
                return read3ByteNumber();
            case 0x0e: // 1 byte
                return readByteNumber();
            default: // half byte
                return readHalfByteNumber(spFlag);
            }
        }

        // We shouldn't be here.
        throw new InvalidFileFormat("Couldn't read number");
    }

    /**
     * Convert the 4 bits of the byte in to a number.
     * 
     * @param spFlag
     * @return
     */
    private int readHalfByteNumber(int by) {
        return (by & (byte) 0x0F) - 1;
    }

    /**
     * Read a byte and convert it to signed integer.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private int readByteNumber() throws IOException, InvalidFileFormat {
        return (read() & 0xFF) << 24 >> 24;
    }

    /**
     * Read 3 bytes and convert it to signed integer.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private int read3ByteNumber() throws IOException, InvalidFileFormat {
        // http://stackoverflow.com/questions/13154715/convert-3-bytes-to-int-in-java
        return ((read() & 0xFF) | ((read() & 0xFF) << 8) | ((read()) << 16)) << 8 >> 8;
    }

    /**
     * Read a specified number of bytes.
     * 
     * @param len
     * @return
     * @throws IOException
     * @throws InvalidFileFormat 
     */
    public byte[] read(int len) throws IOException, InvalidFileFormat {
        byte[] ret = new byte[len];
        int readLen = stream.read(ret);
        
        if(len != readLen)
            throw new InvalidFileFormat("Premature end of stream.");
        
        return ret;
    }

    /**
     * Read a string value from buffer.
     * 
     * @throws IOException
     * @throws InvalidFileFormat
     */
    public ByteString readString() throws IOException, InvalidFileFormat {
        return readString(stream.read());
    }

    /**
     * Read a string value from buffer.
     * 
     * TODO: integer / LZF compression.
     * 
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private ByteString readString(int init) throws IOException,
            InvalidFileFormat {
        byte[] b = new byte[readLength(init)];
        stream.read(b);
        return new ByteString(b);
    }

    /**
     * Read the length encoding.
     * 
     * @param init
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private int readLength(int init) throws IOException, InvalidFileFormat {
        byte lengthType = (byte) (init & 0xc0); // First two bits of init
                                                // represents the length
                                                // encoding type.
        int length;
        switch (lengthType) {
        case 0x00: // 1 byte
            length = init;
            break;
        case 0x40: // 2 byte
            length = readSmallInt(init & 0x3f); // The last 6 bits give the msb
            break;
        case (byte) 0x80: // 5 byte
            length = readInt(ByteOrder.BIG_ENDIAN);
            break;
        default:
            throw new InvalidFileFormat("Unknown length format");
        }
        return length;
    }

    /**
     * Read a byte and join it with msb provided.
     * 
     * @param msb
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    private int readSmallInt(int msb) throws IOException, InvalidFileFormat {
        return (msb * 256) + read();
    }

    /**
     * Read a 4 byte integer.
     * 
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat 
     */
    private int readInt(ByteOrder order) throws IOException, InvalidFileFormat {
        return ByteBuffer.wrap(read(4)).order(order).getInt();
    }

    /**
     * Read a 4 byte to an unsigned long.
     * 
     * Ref : http://stackoverflow.com/questions/14108121/java-convert-long-to-4-
     * bytes
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat 
     */
    public long readUnsignedInt() throws IOException, InvalidFileFormat {
        return readInt() & 0xFFFFFFFFL;
    }

    /**
     * Read a 4 byte integer, Big Endian.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat 
     */
    private int readInt() throws IOException, InvalidFileFormat {
        return readInt(Protocol.ENDIAN);
    }

    /**
     * Read a 4 byte integer, Little Endian.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat 
     */
    private int readShort() throws IOException, InvalidFileFormat {
        return buffer(read(2)).getShort();
    }

    /**
     * Wrap with ByteBuffer.
     * 
     * @param by
     * @return
     */
    private ByteBuffer buffer(byte[] by) {
        return ByteBuffer.wrap(by).order(Protocol.ENDIAN);
    }

    /**
     * Read a 8 byte integer, Little Endian.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat 
     */
    public long readLong() throws IOException, InvalidFileFormat {
        return buffer(read(8)).getLong();
    }

    /**
     * Read a byte. Throws error on EOF.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    public int read() throws IOException, InvalidFileFormat {
        int ret = stream.read();
        if (ret == -1)
            throw new InvalidFileFormat("Premature end of file");

        return ret;
    }

    /**
     * Check if the end of file has reached.
     * 
     * @return
     * @throws IOException
     * @throws InvalidFileFormat
     */
    public boolean end() throws IOException, InvalidFileFormat {
        stream.mark(1);
        if (read() == 0xff)
            return true;

        stream.reset();
        return false;
    }

}
