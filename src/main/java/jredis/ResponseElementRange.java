package jredis;

import java.util.HashSet;


public class ResponseElementRange implements Response<ElementRange> {
    
    private ElementRange elementRange;
    
    public ResponseElementRange() {
    }

    public ResponseElementRange(ElementRange elementRange) {
        this.elementRange = elementRange;
    }

    @Override
    public ElementRange value() {
        if(elementRange == null)
            return new ElementRange(new HashSet<Element>(), false);
        
        return elementRange;
    }

    @Override
    public String toProtocolString() {
        // TODO Auto-generated method stub
        return null;
    }

}
