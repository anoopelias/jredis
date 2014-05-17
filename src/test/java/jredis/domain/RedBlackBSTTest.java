package jredis.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

import org.junit.Test;

public class RedBlackBSTTest {
    
    
    @Test
    public void test_select_one_item() {
        RedBlackBST<Integer> bst = new RedBlackBST<>();
        bst.put(5);
        
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
        RedBlackBST<Integer> bst = new RedBlackBST<>();
        bst.put(6);
        bst.put(5);
        bst.put(7);
        
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
        RedBlackBST<Integer> bst = addAll();
        
        Iterator<Integer> iterator = bst.select(3, 5).iterator();
        assertEquals(new Integer(4), iterator.next());
        assertEquals(new Integer(5), iterator.next());
        assertEquals(new Integer(6), iterator.next());
        
        assertFalse(iterator.hasNext());
        
    }

    @Test
    public void test_select_same() {
        RedBlackBST<Integer> bst = addAll();
        
        Iterator<Integer> iterator = bst.select(3, 3).iterator();
        assertEquals(new Integer(4), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void test_select_none() {
        RedBlackBST<Integer> bst = addAll();
        
        Iterator<Integer> iterator = bst.select(5, 4).iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void test_select_negative() {
        RedBlackBST<Integer> bst = addAll();
        Iterator<Integer> iterator = bst.select(-1, 4).iterator();
        assertEquals(new Integer(1), iterator.next());
        assertEquals(new Integer(2), iterator.next());
        assertEquals(new Integer(3), iterator.next());
        assertEquals(new Integer(4), iterator.next());
        assertEquals(new Integer(5), iterator.next());
        assertFalse(iterator.hasNext());
    }

    private RedBlackBST<Integer> addAll() {
        RedBlackBST<Integer> bst = new RedBlackBST<>();
        
        bst.put(1);
        bst.put(2);
        bst.put(3);
        bst.put(4);
        bst.put(5);
        bst.put(6);
        bst.put(7);
        bst.put(8);
        return bst;
    }

}
