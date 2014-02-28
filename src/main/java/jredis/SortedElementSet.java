package jredis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A data structure to keep the element data sorted by score (zset).
 * 
 * @author anoopelias
 * 
 */
public class SortedElementSet implements Iterable<Element> {

    private Map<String, Double> hashMap = new HashMap<>();
    private TreeSet<Element> sortedSet = new TreeSet<>();

    /**
     * Insert an element in to the set. Guaranteed O(log(N)) time.
     * 
     * @param element
     * @return true if inserted, false if updated
     */
    public boolean insert(Element element) {

        boolean updated = false;
        Double currentScore = hashMap.get(element.getMember());

        if (currentScore != null) {

            Element existingElement = new Element(element.getMember(),
                    currentScore);
            sortedSet.remove(existingElement);
            updated = true;
        }

        /*
         * At this point, it is guaranteed that the element do not exist in the
         * sorted set.
         */
        hashMap.put(element.getMember(), element.getScore());
        sortedSet.add(element);

        return !updated;
    }

    @Override
    public Iterator<Element> iterator() {
        return sortedSet.iterator();
    }

    /**
     * Size of the collection.
     * 
     * @return
     */
    public int size() {
        return sortedSet.size();
    }

    /**
     * Sublist of elements at the given range of scores, both inclusive.
     * 
     * @param from
     * @param to
     * @return
     */
    public Set<Element> subList(Double from, Double to) {
        
        if(from > to)
            return Collections.emptySet();
        
        Element fromElement = new Element(null, from);
        Element toElement = new Element(null, to);

        return sortedSet.subSet(fromElement, true, toElement, true);
    }

}
