package jredis;

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
     * Convert the response to its protocol type.
     * 
     * @return
     */
    public String encode(); 
    
}
