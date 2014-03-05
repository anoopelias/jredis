package jredis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A data structure to keep the element data sorted by score (zset).
 * 
 * @author anoopelias
 * 
 */
public class TreeElementSet implements ElementSet {

    private Map<String, Double> hashMap = new HashMap<>();
    private RedBlackBST<Element> sortedSet = new RedBlackBST<>();

    @Override
    public boolean insert(Element element) {

        boolean updated = false;
        Double currentScore = hashMap.get(element.getMember());

        if (currentScore != null) {

            Element existingElement = new Element(element.getMember(),
                    currentScore);
            sortedSet.delete(existingElement);
            updated = true;
        }

        /*
         * At this point, it is guaranteed that the element do not exist in the
         * sorted set.
         */
        hashMap.put(element.getMember(), element.getScore());
        sortedSet.put(element);

        return !updated;
    }

    @Override
    public Iterator<Element> iterator() {
        return sortedSet.keys().iterator();
    }

    @Override
    public int size() {
        return sortedSet.size();
    }

    @Override
    public int subsetSizeByScore(Double from, Double to) {
        if(from > to)
            return 0;
        
        return sortedSet.size(new Element(null, from), new Element(null, to));
    }

    @Override
    public Iterable<Element> subsetByScore(Double from, Double to) {
        if(from > to)
            return Collections.emptySet();
        
        return sortedSet.keys(new Element(null, from), new Element(null, to));
    }

    @Override
    public Set<Element> subsetByRank(int from, int to) {
        return sortedSet.select(from, to);
    }

}
