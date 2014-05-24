package jredis.rdf;

import java.io.IOException;
import java.io.OutputStream;

import jredis.DB;
import jredis.domain.BinaryString;
import jredis.domain.ElementSet;
import jredis.domain.TimedBinaryString;
import jredis.exception.InternalServerError;

/**
 * Save the current data in DB to OutputStream.
 * 
 * @author anoopelias
 *
 */
public class Saver {
    
    private RdfWriter rdfWriter;
    
    /**
     * Construct a saver operation.
     * 
     * @param os
     */
    public Saver(OutputStream os) {
        this.rdfWriter = new RdfWriter(os);
    }
    
    /**
     * Save the current database to output stream.
     * 
     */
    public void save() {
        try {
            rdfWriter.writeInit();
            writeAllKeys();
            
            rdfWriter.writeEnd();
        } catch (IOException e) {
            throw new InternalServerError("Some problem with saving", e);
        }
    }

    /**
     * Write all keys to stream.
     * 
     * @throws IOException
     */
    private void writeAllKeys() throws IOException {
        for(BinaryString key : DB.INSTANCE) {
            Object obj = DB.INSTANCE.get(key, Object.class);
            if(obj instanceof TimedBinaryString) {
                writeTbs(key, (TimedBinaryString)obj);
            } else if (obj instanceof ElementSet) {
                writeElementSet(key, (ElementSet)obj);
            } else {
                throw new InternalServerError("Unknown data type to save : " + obj);
            }
        }
    }

    /**
     * Write an element set key and value.
     * 
     * @param key
     * @param obj
     * @throws IOException
     */
    private void writeElementSet(BinaryString key, ElementSet obj) throws IOException {
        rdfWriter.write(RdfProtocol.ValueType.SORTED_ZIPLIST);
        rdfWriter.write(key);
        rdfWriter.write(obj);
    }

    /**
     * Write a TimedByteString to output stream.
     * 
     * @param key
     * @param tbs
     * @throws IOException
     */
    private void writeTbs(BinaryString key, TimedBinaryString tbs)
            throws IOException {
        
        if(tbs.expiryTime() != null)
            rdfWriter.write(tbs.expiryTime());
            
        rdfWriter.write(RdfProtocol.ValueType.STRING);
        rdfWriter.write(key);
        rdfWriter.write(tbs.value());
    }

}
