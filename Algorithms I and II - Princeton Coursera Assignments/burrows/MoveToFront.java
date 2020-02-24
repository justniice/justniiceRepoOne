/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    public static void encode() {
        char[] sequence = new char[256];
        for (int i = 0; i <= 255; i++) {
            sequence[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (int i = 0; i <= 255; i++) {
                if (c == sequence[i]) {
                    BinaryStdOut.write((char) i);
                    for (int j = i; j >= 1; j--) {
                        sequence[j] = sequence[j - 1];
                    }
                    sequence[0] = c;
                    break;
                }
            }
        }
        BinaryStdOut.flush();
    }

    public static void decode() {
        char[] sequence = new char[256];
        for (int i = 0; i <= 255; i++) {
            sequence[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int input = BinaryStdIn.readInt(8);
            char c = sequence[input];
            BinaryStdOut.write(c);
            for (int i = input; i >= 1; i--) {
                sequence[i] = sequence[i - 1];
            }
            sequence[0] = c;
        }
        BinaryStdOut.flush();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
    }
}
