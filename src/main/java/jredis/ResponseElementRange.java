package jredis;

import java.io.IOException;
import java.util.HashSet;

/**
 * Response of type ElementRange
 * 
 * @author anoopelias
 *
 */
public class ResponseElementRange implements Response<ElementRange> {
    
    private ElementRange elementRange;
    
    /**
     * Null constructor.
     * 
     */
    public ResponseElementRange() {
        this.elementRange = new ElementRange(new HashSet<Element>());
    }

    /**
     * Construct based on an ElementRange.
     * 
     * @param elementRange
     */
    public ResponseElementRange(ElementRange elementRange) {
        this.elementRange = elementRange;
    }

    @Override
    public ElementRange value() {
        return elementRange;
    }

    @Override
    public void write(ResponseWriter writer) throws IOException {
        writer.write(elementRange);
    }

}
