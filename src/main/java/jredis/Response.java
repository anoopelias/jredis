package jredis;

import java.io.IOException;

/**
 * Interface for response types.
 * 
 * @author anoopelias
 *
 * @param <T>
 */
public interface Response<T> {

    /**
     * Get the value in a response.
     * 
     * @return
     */
    public T value();

    /**
     * Write the response using writer.
     * 
     * @param writer
     */
    public void write(ResponseWriter writer) throws IOException;
    
}
