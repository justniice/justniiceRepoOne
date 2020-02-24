/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int[] helpArray = new int[s.length()];
        int first = 0;
        for (int i = 0; i < s.length(); i++) {
            helpArray[i] = csa.index(i);
            if (helpArray[i] == 0) {
                first = i;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt((helpArray[i] + s.length() - 1) % s.length()));
        }
        BinaryStdOut.flush();

    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] t = new char[s.length()];
        int[] howMany = new int[257];
        char[] front = new char[s.length()];
        int[] next = new int[s.length()];
        for (int i = 0; i < 257; i++) {
            howMany[i] = 0;
        }
        for (int i = 0; i < s.length(); i++) {
            char readingChar = s.charAt(i);
            t[i] = readingChar;
            howMany[(int) readingChar + 1]++;
        }
        for (int i = 0; i < 256; i++) {
            howMany[i + 1] += howMany[i];
        }
        for (int i = 0; i < s.length(); i++) {
            front[howMany[(int) t[i]]] = t[i];
            next[howMany[(int) t[i]]++] = i;
        }
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(front[first]);
            first = next[first];
        }
        BinaryStdOut.flush();

    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
