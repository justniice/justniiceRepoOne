/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final String theString;
    private CircularSuffix[] suffixArray;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        this.theString = s;
        this.suffixArray = new CircularSuffix[s.length()];
        for (int i = 0; i < s.length(); i++) {
            suffixArray[i] = new CircularSuffix(i);
        }
        Arrays.sort(suffixArray);
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int start;

        public CircularSuffix(int start) {
            this.start = start;
        }

        public int originalIndex() {
            return start;
        }

        public char getCharAt(int i) {
            return theString.charAt((start + i) % theString.length());
        }

        public int compareTo(CircularSuffix cs) {
            for (int i = 0; i < theString.length(); i++) {
                if (this.getCharAt(i) > cs.getCharAt(i)) {
                    return 1;
                }
                else if (this.getCharAt(i) < cs.getCharAt(i)) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public int length() {
        return theString.length();
    }

    public int index(int i) {
        if (i < 0 || i > theString.length() - 1) {
            throw new IllegalArgumentException();
        }
        return suffixArray[i].originalIndex();
    }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < 12; i++) {
            StdOut.print("\n");
            StdOut.print(csa.index(i));
        }
    }
}
