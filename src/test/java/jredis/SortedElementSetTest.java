package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

import org.junit.Test;

public class SortedElementSetTest {
    
    @Test
    public void test_insert_sort() {
        SortedElementSet elementSet = new SortedElementSet();
        elementSet.insert(new Element("HIJ", 1.4));
        elementSet.insert(new Element("DFG", 1.2));
        elementSet.insert(new Element("OPQ", 1.8));
        elementSet.insert(new Element("KLM", 1.6));
        
        Iterator<Element> iterElem = elementSet.iterator();
        
        Element element = iterElem.next();
        assertEquals("DFG", element.getMember());
        assertEquals(new Double(1.2), element.getScore());

        element = iterElem.next();
        assertEquals("HIJ", element.getMember());
        assertEquals(new Double(1.4), element.getScore());
        
        element = iterElem.next();
        assertEquals("KLM", element.getMember());
        assertEquals(new Double(1.6), element.getScore());

        element = iterElem.next();
        assertEquals("OPQ", element.getMember());
        assertEquals(new Double(1.8), element.getScore());

        assertFalse(iterElem.hasNext());

    }

    @Test
    public void test_insert_same_key() {
        SortedElementSet elementSet = new SortedElementSet();
        elementSet.insert(new Element("QWE", 2.0));
        elementSet.insert(new Element("ABC", 1.4));
        elementSet.insert(new Element("ABC", 1.6));
        elementSet.insert(new Element("ABC", 1.8));
        
        Iterator<Element> iterElem = elementSet.iterator();
        Element element = iterElem.next();
        assertEquals("ABC", element.getMember());
        assertEquals(new Double(1.8), element.getScore());
        
        element = iterElem.next();
        assertEquals("QWE", element.getMember());
        assertEquals(new Double(2.0), element.getScore());
        
        assertFalse(iterElem.hasNext());
    }

    @Test
    public void test_insert_same_score() {
        SortedElementSet elementSet = new SortedElementSet();
        elementSet.insert(new Element("HIJ", 1.4));
        elementSet.insert(new Element("DFG", 1.4));
        elementSet.insert(new Element("OPQ", 1.4));
        elementSet.insert(new Element("KLM", 1.4));
        
        Iterator<Element> iterElem = elementSet.iterator();
        Element element = iterElem.next();
        assertEquals("DFG", element.getMember());
        assertEquals(new Double(1.4), element.getScore());
        
        element = iterElem.next();
        assertEquals("HIJ", element.getMember());
        assertEquals(new Double(1.4), element.getScore());

        element = iterElem.next();
        assertEquals("KLM", element.getMember());
        assertEquals(new Double(1.4), element.getScore());

        element = iterElem.next();
        assertEquals("OPQ", element.getMember());
        assertEquals(new Double(1.4), element.getScore());

        assertFalse(iterElem.hasNext());
    }
    
    @Test
    public void test_sublist() {
        SortedElementSet elementSet = addAll();
        
        Iterator<Element> iterElem = elementSet.subList(1.5, 2.1).iterator();
        
        Element element = iterElem.next();
        assertEquals("KLM", element.getMember());
        assertEquals(new Double(1.6), element.getScore());

        element = iterElem.next();
        assertEquals("OPQ", element.getMember());
        assertEquals(new Double(1.8), element.getScore());

        element = iterElem.next();
        assertEquals("QPM", element.getMember());
        assertEquals(new Double(2.0), element.getScore());
        
        assertFalse(iterElem.hasNext());

    }

    @Test
    public void test_sublist_from_inclusive() {
        SortedElementSet elementSet = addAll();
        
        Iterator<Element> iterElem = elementSet.subList(1.6, 1.7).iterator();
        
        Element element = iterElem.next();
        assertEquals("KLM", element.getMember());
        assertEquals(new Double(1.6), element.getScore());

        assertFalse(iterElem.hasNext());

    }

    @Test
    public void test_sublist_to_inclusive() {
        SortedElementSet elementSet = addAll();
        
        Iterator<Element> iterElem = elementSet.subList(2.1, 2.2).iterator();
        
        Element element = iterElem.next();
        assertEquals("VFR", element.getMember());
        assertEquals(new Double(2.2), element.getScore());

        assertFalse(iterElem.hasNext());

    }

    @Test
    public void test_sublist_from_infinity() {
        SortedElementSet elementSet = addAll();
        
        Iterator<Element> iterElem = elementSet.subList(Double.NEGATIVE_INFINITY, 1.4).iterator();
        
        Element element = iterElem.next();
        assertEquals("DFG", element.getMember());
        assertEquals(new Double(1.2), element.getScore());

        element = iterElem.next();
        assertEquals("HIJ", element.getMember());
        assertEquals(new Double(1.4), element.getScore());

        assertFalse(iterElem.hasNext());

    }

    @Test
    public void test_sublist_to_infinity() {
        SortedElementSet elementSet = addAll();
        
        Iterator<Element> iterElem = elementSet.subList(2.0, Double.POSITIVE_INFINITY).iterator();
        
        Element element = iterElem.next();
        assertEquals("QPM", element.getMember());
        assertEquals(new Double(2.0), element.getScore());

        element = iterElem.next();
        assertEquals("VFR", element.getMember());
        assertEquals(new Double(2.2), element.getScore());

        assertFalse(iterElem.hasNext());

    }

    private SortedElementSet addAll() {
        SortedElementSet elementSet = new SortedElementSet();
        elementSet.insert(new Element("HIJ", 1.4));
        elementSet.insert(new Element("QPM", 2.0));
        elementSet.insert(new Element("DFG", 1.2));
        elementSet.insert(new Element("VFR", 2.2));
        elementSet.insert(new Element("OPQ", 1.8));
        elementSet.insert(new Element("KLM", 1.6));
        return elementSet;
    }

}
