/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private int head;
    private int tail;
    private Item[] theQueue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.size = 0;
        this.head = 0;
        this.tail = 0;
        this.theQueue = (Item[]) new Object[1];

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (this.size == theQueue.length) {
            expand();
        }
        this.size++;
        theQueue[head] = item;
        int toSwitch = (StdRandom.uniform(tail, tail + size)) % theQueue.length;
        Item temp = theQueue[toSwitch];
        theQueue[head] = temp;
        theQueue[toSwitch] = item;
        head++;
        if (head == theQueue.length) {
            head = 0;
        }
    }

    private void expand() {
        Item[] newQueue = (Item[]) new Object[2 * theQueue.length];
        for (int i = 0; i < size; i++) {
            newQueue[i] = theQueue[(i + tail) % theQueue.length];
        }
        tail = 0;
        head = size;
        theQueue = newQueue;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            java.util.NoSuchElementException e = new java.util.NoSuchElementException();
            throw e;
        }
        Item toReturn = theQueue[tail];
        tail++;
        if (tail == theQueue.length) {
            tail = 0;
        }
        this.size--;
        if (size < theQueue.length / 4) {
            contract();
        }
        return toReturn;
    }

    private void contract() {
        Item[] newQueue = (Item[]) new Object[theQueue.length / 2];
        for (int i = 0; i < size; i++) {
            newQueue[i] = theQueue[(i + tail) % theQueue.length];
        }
        tail = 0;
        head = size;
        theQueue = newQueue;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            java.util.NoSuchElementException e = new java.util.NoSuchElementException();
            throw e;
        }
        int index = (StdRandom.uniform(tail, tail + size)) % theQueue.length;
        return theQueue[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int number = size;
        private final int[] indices;

        public RandomizedQueueIterator() {
            indices = new int[size];
            for (int i = 0; i < size; i++) {
                indices[i] = (tail + i) % theQueue.length;
            }
            StdRandom.shuffle(indices);
        }

        public boolean hasNext() {
            return (number > 0);
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
            return theQueue[indices[number]];
        }
    }

    public String toString() {
        StdOut.print(head);
        StdOut.print(tail);
        StdOut.print(size);
        return Arrays.toString(theQueue);
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<Integer>();
        test.enqueue(1);
        test.enqueue(2);
        test.enqueue(3);
        test.enqueue(4);
        test.enqueue(5);
        test.enqueue(6);
        test.enqueue(7);
        test.dequeue();
        test.enqueue(8);
        test.enqueue(9);
        test.dequeue();
        test.enqueue(10);
        StdOut.print(test);
        for (int i : test) {
            StdOut.print(i);
        }
        StdOut.print(test.dequeue());
        StdOut.print(test.dequeue());
        StdOut.print(test.dequeue());
        StdOut.print(test);

    }

}
