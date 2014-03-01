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
public class TreeElementSet implements ElementSet {

    private Map<String, Double> hashMap = new HashMap<>();
    private TreeSet<Element> sortedSet = new TreeSet<>();

    @Override
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

    @Override
    public int size() {
        return sortedSet.size();
    }

    @Override
    public Set<Element> subListByScore(Double from, Double to) {
        if(from > to)
            return Collections.emptySet();
        
        Element fromElement = new Element(null, from);
        Element toElement = new Element(null, to);

        return sortedSet.subSet(fromElement, true, toElement, true);
    }

    @Override
    public Set<Element> subListByRank(Double from, Double to) {
        // TODO Auto-generated method stub
        return null;
    }

}
