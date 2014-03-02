package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

import org.junit.Test;

public class RedBlackBSTTest {
    
    
    @Test
    public void test_select_one_item() {
        RedBlackBST<Integer, String> bst = new RedBlackBST<>();
        bst.put(5, "A");
        
        Iterator<Integer> iterator = bst.select(0, 0).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());

        iterator = bst.select(0, 1).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());
        
        iterator = bst.select(-1, 0).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());
        
        iterator = bst.select(-1, 1).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());

        iterator = bst.select(1, 1).iterator();
        assertFalse(iterator.hasNext());

        iterator = bst.select(1, 5).iterator();
        assertFalse(iterator.hasNext());

        iterator = bst.select(-2, -1).iterator();
        assertFalse(iterator.hasNext());

        iterator = bst.select(2, -1).iterator();
        assertFalse(iterator.hasNext());

    }

    @Test
    public void test_select_three_item() {
        RedBlackBST<Integer, String> bst = new RedBlackBST<>();
        bst.put(6, "B");
        bst.put(5, "A");
        bst.put(7, "C");
        
        Iterator<Integer> iterator = bst.select(0, 0).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());

        iterator = bst.select(1, 1).iterator();
        assertEquals(new Integer(6), iterator.next());
        assertFalse(iterator.hasNext());

        iterator = bst.select(2, 2).iterator();
        assertEquals(new Integer(7), iterator.next());
        assertFalse(iterator.hasNext());

        iterator = bst.select(0, 1).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertEquals(new Integer(6), iterator.next());
        assertFalse(iterator.hasNext());
        
        iterator = bst.select(-1, 0).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());
        
        iterator = bst.select(-1, 1).iterator();
        assertEquals(new Integer(5), iterator.next());
        assertEquals(new Integer(6), iterator.next());
        assertFalse(iterator.hasNext());

        iterator = bst.select(1, 10).iterator();
        assertEquals(new Integer(6), iterator.next());
        assertEquals(new Integer(7), iterator.next());
        assertFalse(iterator.hasNext());

        iterator = bst.select(-2, -1).iterator();
        assertFalse(iterator.hasNext());

        iterator = bst.select(2, -1).iterator();
        assertFalse(iterator.hasNext());

    }

    @Test
    public void test_select() {
        RedBlackBST<Integer, String> bst = addAll();
        
        Iterator<Integer> iterator = bst.select(3, 5).iterator();
        assertEquals(new Integer(4), iterator.next());
        assertEquals(new Integer(5), iterator.next());
        assertEquals(new Integer(6), iterator.next());
        
        assertFalse(iterator.hasNext());
        
    }

    @Test
    public void test_select_same() {
        RedBlackBST<Integer, String> bst = addAll();
        
        Iterator<Integer> iterator = bst.select(3, 3).iterator();
        assertEquals(new Integer(4), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void test_select_none() {
        RedBlackBST<Integer, String> bst = addAll();
        
        Iterator<Integer> iterator = bst.select(5, 4).iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void test_select_negative() {
        RedBlackBST<Integer, String> bst = addAll();
        Iterator<Integer> iterator = bst.select(-1, 4).iterator();
        assertEquals(new Integer(1), iterator.next());
        assertEquals(new Integer(2), iterator.next());
        assertEquals(new Integer(3), iterator.next());
        assertEquals(new Integer(4), iterator.next());
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void test_select_debug() {
        RedBlackBST<Integer, String> bst = addAll();
        Iterator<Integer> iterator = bst.select(3, 5).iterator();
        assertEquals(new Integer(4), iterator.next());
        assertEquals(new Integer(5), iterator.next());
        assertEquals(new Integer(6), iterator.next());
        assertFalse(iterator.hasNext());
    }

    
    private RedBlackBST<Integer, String> addAll() {
        RedBlackBST<Integer, String> bst = new RedBlackBST<>();
        
        bst.put(1, "A");
        bst.put(2, "B");
        bst.put(3, "C");
        bst.put(4, "D");
        bst.put(5, "E");
        bst.put(6, "F");
        bst.put(7, "G");
        bst.put(8, "H");
        return bst;
    }

}
