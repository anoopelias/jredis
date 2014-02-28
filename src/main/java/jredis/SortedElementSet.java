package jredis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A data structure to keep the element data sorted by score.
 * 
 * @author anoopelias
 * 
 */
public class SortedElementSet implements Iterable<Element> {

    private Map<String, Double> hashMap = new HashMap<>();
    private TreeSet<Element> sortedSet = new TreeSet<>();

    public boolean insert(Element element) {

        boolean updated = false;
        Double currentScore = hashMap.get(element.getMember());

        if (currentScore != null && !currentScore.equals(element.getScore())) {

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

    public int size() {
        return sortedSet.size();
    }

    public Set<Element> subList(Double from, Double to) {
        return sortedSet.subSet(new Element(null, from), true, new Element(
                null, to), true);
    }

}
