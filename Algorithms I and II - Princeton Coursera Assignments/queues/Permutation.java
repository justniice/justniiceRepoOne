/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int toPrint = Integer.parseInt(args[0]);
        RandomizedQueue<String> LETSGO = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            LETSGO.enqueue(StdIn.readString());
        }
        for (int i = 0; i < toPrint; i++) {
            StdOut.print(LETSGO.dequeue() + "\n");
        }

    }
}
