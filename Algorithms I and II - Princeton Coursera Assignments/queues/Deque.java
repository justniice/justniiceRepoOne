/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Item[] theDeque;
    private int head;
    private int tail;
    private int size;

    // construct an empty deque
    public Deque() {
        this.theDeque = (Item[]) new Object[1];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (this.size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (this.size() == theDeque.length) {
            this.expand();
        }
        if (this.head == theDeque.length) {
            this.head = 1;
            theDeque[0] = item;
        }
        else {
            theDeque[this.head] = item;
            head++;
        }
        this.size++;
    }

    private void expand() {
        Item[] newDeque = (Item[]) new Object[2 * (theDeque.length)];
        for (int i = tail; i < theDeque.length; i++) {
            if (theDeque[i] != null) {
                newDeque[i - tail] = theDeque[i];
            }
        }
        if (head <= tail) {
            for (int j = 0; j < head; j++) {
                newDeque[theDeque.length - tail + j] = theDeque[j];
            }
        }
        this.head = this.size();
        this.tail = 0;
        this.theDeque = newDeque;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (this.size == 0) {
            addFirst(item);
        }
        else {
            if (item == null) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
            if (this.size() == theDeque.length) {
                this.expand();
            }
            if (this.tail == 0) {
                theDeque[theDeque.length - 1] = item;
                this.tail = theDeque.length - 1;
            }
            else {
                this.tail--;
                theDeque[this.tail] = item;
            }
            this.size++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.size() == 0) {
            java.util.NoSuchElementException e = new java.util.NoSuchElementException();
            throw e;
        }
        this.size--;
        Item toReturn;
        if (this.head == 1) {
            toReturn = theDeque[this.head - 1];
            theDeque[this.head - 1] = null;
            this.head = theDeque.length;
        }
        else {
            this.head--;
            toReturn = theDeque[this.head];
            theDeque[this.head] = null;
        }
        if (this.size() < theDeque.length / 4) {
            this.downsize();
        }

        return toReturn;
    }

    private void downsize() {
        Item[] newDeque = (Item[]) new Object[theDeque.length / 2];
        if (this.head == this.tail) {
            this.theDeque = newDeque;
            this.head = 0;
            this.tail = 0;
        }
        for (int i = tail; i < theDeque.length; i++) {
            if (theDeque[i] != null) {
                newDeque[i - tail] = theDeque[i];
            }
        }
        if (head < tail) {
            for (int j = 0; j < head; j++) {
                newDeque[theDeque.length - tail + 1 + j] = theDeque[j];
            }
        }
        this.head = this.size;
        this.tail = 0;
        this.theDeque = newDeque;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.size() == 1) {
            return this.removeFirst();
        }
        else {
            if (this.size == 0) {
                java.util.NoSuchElementException e = new java.util.NoSuchElementException();
                throw e;
            }
            this.size--;
            Item toReturn;
            if (this.tail == theDeque.length - 1) {
                toReturn = theDeque[this.tail];
                theDeque[this.tail] = null;
                this.tail = 0;
            }
            else {
                toReturn = theDeque[this.tail];
                theDeque[this.tail] = null;
                this.tail++;
            }
            if (this.size < theDeque.length / 4) {
                this.downsize();
            }
            return toReturn;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int number = size();

        public boolean hasNext() {
            return number > 0;
        }

        public void remove() {
            UnsupportedOperationException e = new UnsupportedOperationException();
            throw e;
        }

        public Item next() {
            if (number == 0) {
                java.util.NoSuchElementException e = new java.util.NoSuchElementException();
                throw e;
            }
            number--;
            return theDeque[(tail + (number)) % theDeque.length];
        }
    }

    public String toString() {
        StdOut.print("Head: " + this.head);
        StdOut.print("Tail: " + this.tail);
        StdOut.print("Size: " + this.size);
        return Arrays.toString(theDeque);
    }


    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> test = new Deque<String>();
        test.addLast("a");
        test.addFirst("b");
        test.addLast("c");
        test.addFirst("d");
        test.addLast("e");
        StdOut.print(test);
        test.addLast("f");
        test.addLast("g");
        test.addFirst("h");
        test.addFirst("i");
        StdOut.print(test);
        for (String s : test) {
            System.out.println(s);
        }
        StdOut.print(test.removeFirst());
        test.removeFirst();
        System.out.println(test);
        test.removeFirst();
        test.removeFirst();
        StdOut.print(test.removeLast());
        StdOut.print(test);
    }

}
