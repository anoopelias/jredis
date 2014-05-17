package jredis.domain;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/**
 * Reference : http://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html
 * 
 * Modified a few things,
 * 1. Removed check() and related methods.
 * 2. Added select by range.
 * 3. Removed value as we don't need it as a map any more.
 * 4. Refactored 'Key' to 'Item'.
 * 
 * @author anoopelias
 * 
 * @param <Item>
 * @param <Value>
 */
public class RedBlackBST<Item extends Comparable<Item>> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root; // root of the BST

    // BST helper node data type
    private class Node {
        private Item item; // key
        private Node left, right; // links to left and right subtrees
        private boolean color; // color of parent link
        private int N; // subtree count

        public Node(Item key, boolean color, int N) {
            this.item = key;
            this.color = color;
            this.N = N;
        }
        
        public String toString() {
            return "{x:" + item + " left: " + left + " right: " + right
                    + "}";
        }
    }

    /*************************************************************************
     * Node helper methods
     *************************************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null)
            return false;
        return (x.color == RED);
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null)
            return 0;
        return x.N;
    }

    /*************************************************************************
     * Size methods
     *************************************************************************/

    // return number of key-value pairs in this symbol table
    public int size() {
        return size(root);
    }

    // is this symbol table empty?
    public boolean isEmpty() {
        return root == null;
    }

    /*************************************************************************
     * Standard BST search
     *************************************************************************/

    // is there a key-value pair with the given key?
    public boolean contains(Item key) {
        return contains(root, key);
    }

    // is there a key-value pair with the given key in the subtree rooted at x?
    private boolean contains(Node x, Item key) {
        while (x != null) {
            int cmp = key.compareTo(x.item);
            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else
                return true;
        }
        return false;
    }

    /*************************************************************************
     * Red-black insertion
     *************************************************************************/

    // insert the key-value pair; overwrite the old value with the new value
    // if the key is already present
    public void put(Item key) {
        root = put(root, key);
        root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, Item key) {
        if (h == null)
            return new Node(key, RED, 1);

        int cmp = key.compareTo(h.item);
        if (cmp < 0)
            h.left = put(h.left, key);
        else if (cmp > 0)
            h.right = put(h.right, key);

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))
            h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left))
            h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))
            flipColors(h);
        h.N = size(h.left) + size(h.right) + 1;

        return h;
    }

    /*************************************************************************
     * Red-black deletion
     *************************************************************************/

    // delete the key-value pair with the minimum key
    public void deleteMin() {
        if (isEmpty())
            throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty())
            root.color = BLACK;
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node h) {
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = deleteMin(h.left);
        return balance(h);
    }

    // delete the key-value pair with the maximum key
    public void deleteMax() {
        if (isEmpty())
            throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMax(root);
        if (!isEmpty())
            root.color = BLACK;
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node h) {
        if (isRed(h.left))
            h = rotateRight(h);

        if (h.right == null)
            return null;

        if (!isRed(h.right) && !isRed(h.right.left))
            h = moveRedRight(h);

        h.right = deleteMax(h.right);

        return balance(h);
    }

    // delete the key-value pair with the given key
    public void delete(Item key) {
        if (!contains(key)) {
            System.err.println("symbol table does not contain " + key);
            return;
        }

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty())
            root.color = BLACK;
    }

    // delete the key-value pair with the given key rooted at h
    private Node delete(Node h, Item key) {
        assert contains(h, key);

        if (key.compareTo(h.item) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.item) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.item) == 0) {
                Node x = min(h.right);
                h.item = x.item;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            } else
                h.right = delete(h.right, key);
        }
        return balance(h);
    }

    /*************************************************************************
     * red-black tree helper functions
     *************************************************************************/

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.N = h.N;
        h.N = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        assert (h != null) && (h.left != null) && (h.right != null);
        assert (!isRed(h) && isRed(h.left) && isRed(h.right))
                || (isRed(h) && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        assert (h != null);
        assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        assert (h != null);
        assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
        }
        return h;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        assert (h != null);

        if (isRed(h.right))
            h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left))
            h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))
            flipColors(h);

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }

    /*************************************************************************
     * Utility functions
     *************************************************************************/

    // height of tree (1-node tree has height 0)
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null)
            return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /*************************************************************************
     * Ordered symbol table methods.
     *************************************************************************/

    // the smallest key; null if no such key
    public Item min() {
        if (isEmpty())
            return null;
        return min(root).item;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) {
        assert x != null;
        if (x.left == null)
            return x;
        else
            return min(x.left);
    }

    // the largest key; null if no such key
    public Item max() {
        if (isEmpty())
            return null;
        return max(root).item;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) {
        assert x != null;
        if (x.right == null)
            return x;
        else
            return max(x.right);
    }

    // the largest key less than or equal to the given key
    public Item floor(Item key) {
        Node x = floor(root, key);
        if (x == null)
            return null;
        else
            return x.item;
    }

    // the largest key in the subtree rooted at x less than or equal to the
    // given key
    private Node floor(Node x, Item key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.item);
        if (cmp == 0)
            return x;
        if (cmp < 0)
            return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null)
            return t;
        else
            return x;
    }

    // the smallest key greater than or equal to the given key
    public Item ceiling(Item key) {
        Node x = ceiling(root, key);
        if (x == null)
            return null;
        else
            return x.item;
    }

    // the smallest key in the subtree rooted at x greater than or equal to the
    // given key
    private Node ceiling(Node x, Item key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.item);
        if (cmp == 0)
            return x;
        if (cmp > 0)
            return ceiling(x.right, key);
        Node t = ceiling(x.left, key);
        if (t != null)
            return t;
        else
            return x;
    }

    // the key of rank k
    public Item select(int k) {
        if (k < 0 || k >= size())
            return null;
        Node x = select(root, k);
        return x.item;
    }

    // Keys of rank from lo to hi
    public Set<Item> select(int lo, int hi) {
        Set<Item> queue = new LinkedHashSet<>();
        select(root, queue, lo, hi);
        return queue;
    }

    // the range of rank k in the subtree rooted at x
    private void select(Node x, Set<Item> queue, int lo, int hi) {
        if(x == null) return;
        
        int rank = size(x.left);
        if(lo < rank)
            select(x.left, queue, lo, hi);
        if(rank >= lo && rank <= hi)
            queue.add(x.item);
        if(rank < hi)
            select(x.right, queue, lo - rank - 1, hi - rank - 1);
    }

    
    // the key of rank k in the subtree rooted at x
    private Node select(Node x, int k) {
        assert x != null;
        assert k >= 0 && k < size(x);
        int t = size(x.left);
        if (t > k)
            return select(x.left, k);
        else if (t < k)
            return select(x.right, k - t - 1);
        else
            return x;
    }

    // number of keys less than key
    public int rank(Item key) {
        return rank(key, root);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(Item key, Node x) {
        if (x == null)
            return 0;
        int cmp = key.compareTo(x.item);
        if (cmp < 0)
            return rank(key, x.left);
        else if (cmp > 0)
            return 1 + size(x.left) + rank(key, x.right);
        else
            return size(x.left);
    }

    /***********************************************************************
     * Range count and range search.
     ***********************************************************************/

    // all of the keys, as an Iterable
    public Iterable<Item> keys() {
        return keys(min(), max());
    }

    // the keys between lo and hi, as an Iterable
    public Iterable<Item> keys(Item lo, Item hi) {
        Queue<Item> queue = new ArrayDeque<>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node x, Queue<Item> queue, Item lo, Item hi) {
        if (x == null)
            return;
        int cmplo = lo.compareTo(x.item);
        int cmphi = hi.compareTo(x.item);
        if (cmplo < 0)
            keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0)
            queue.add(x.item);
        if (cmphi > 0)
            keys(x.right, queue, lo, hi);
    }

    // number keys between lo and hi
    public int size(Item lo, Item hi) {
        if (lo.compareTo(hi) > 0)
            return 0;
        if (contains(hi))
            return rank(hi) - rank(lo) + 1;
        else
            return rank(hi) - rank(lo);
    }
    
    public String toString() {
        return root == null ? "null" : root.toString();
    }
}
