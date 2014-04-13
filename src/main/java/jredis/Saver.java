package jredis;

import java.io.IOException;
import java.io.OutputStream;

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
            for(String key : DB.INSTANCE) {
                Object obj = DB.INSTANCE.get(key, Object.class);
                if(obj instanceof TimedByteString) {
                    rdfWriter.write(RdfProtocol.ValueType.STRING);
                    rdfWriter.write(new ByteString(key)); // TODO : DB Key should ideally be ByteString
                    rdfWriter.write(((TimedByteString)obj).value());
                }
            }
            rdfWriter.writeEnd();
        } catch (IOException e) {
            throw new InternalServerError("Some problem with saving", e);
        }
    }

}
