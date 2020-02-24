/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
    private final TST<Integer> vWords;

    public BoggleSolver(String[] dictionary) {
        TST<Integer> validWords = new TST<Integer>();
        for (String s : dictionary) {
            if (s.length() >= 3) {
                if (s.length() <= 4) {
                    validWords.put(s, 1);
                }
                else if (s.length() == 5) {
                    validWords.put(s, 2);
                }
                else if (s.length() == 6) {
                    validWords.put(s, 3);
                }
                else if (s.length() == 7) {
                    validWords.put(s, 5);
                }
                else if (s.length() >= 8) {
                    validWords.put(s, 11);
                }
            }
        }
        this.vWords = validWords;
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int rows = board.rows();
        int columns = board.cols();
        int nSquares = rows * columns;
        boolean[] marked = new boolean[nSquares + 1];
        TST<Integer> boardWords = new TST<Integer>();
        for (int i = 1; i <= nSquares; i++) {
            marked[i] = false;
        }
        for (int i = 1; i <= nSquares; i++) {
            StringBuilder sb = new StringBuilder();
            wordSearch(i, sb, board, boardWords, marked);
        }
        return boardWords.keys();
    }

    private void wordSearch(int i, StringBuilder sb, BoggleBoard board, TST<Integer> boardWords,
                            boolean[] marked) {
        marked[i] = true;
        StringBuilder anotherSB = new StringBuilder();
        for (int k = 0; k < sb.length(); k++) {
            anotherSB.append(sb.charAt(k));
        }
        char c = intToChar(i, board);
        if (c == 'Q') {
            anotherSB.append('Q');
            anotherSB.append('U');
        }
        else {
            anotherSB.append(c);
        }
        String currentString = anotherSB.toString();
        if (vWords.contains(currentString)) {
            boardWords.put(currentString, vWords.get(currentString));
        }
        Iterable<String> matches = vWords.keysWithPrefix(currentString);
        if (matches.iterator().hasNext()) {
            for (int j : neighbours(i, board.rows(), board.cols())) {
                if (!marked[j]) {
                    wordSearch(j, anotherSB, board, boardWords, marked);
                }
            }
        }
        marked[i] = false;
    }

    private char intToChar(int i, BoggleBoard board) {
        return board.getLetter((i - 1) / board.cols(), (i - 1) % board.cols());
    }

    public int scoreOf(String word) {
        if (vWords.contains(word)) {
            return vWords.get(word);
        }
        else return 0;
    }

    private Iterable<Integer> neighbours(int i, int rows, int columns) {
        Bag<Integer> b = new Bag<Integer>();
        if (columns == 1) {
            if (i == 1 && rows >= 2) {
                b.add(2);
            }
            else if (i < rows) {
                b.add(i - 1);
                b.add(i + 1);
            }
            else if (i == rows && rows > 1) {
                b.add(i - 1);
            }
        }
        else {
            if (i % columns != 1) { // not leftmost
                b.add(i - 1);
            }
            if (i % columns != 0) { // not rightmost
                b.add(i + 1);
            }
            if ((i - 1) / columns != 0) { // not topmost
                b.add(i - columns);
            }
            if ((i - 1) / columns != rows - 1) { // not bottommost
                b.add(i + columns);
            }
            if (i % columns != 1 && (i - 1) / columns != 0) {
                b.add(i - 1 - columns);
            }
            if (i % columns != 1 && (i - 1) / columns != rows - 1) {
                b.add(i - 1 + columns);
            }
            if (i % columns != 0 && (i - 1) / columns != 0) {
                b.add(i + 1 - columns);
            }
            if (i % columns != 0 && (i - 1) / columns != rows - 1) {
                b.add(i + 1 + columns);
            }
        }
        return b;
    }

    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        BoggleSolver bs = new BoggleSolver(in.readAllStrings());
        BoggleBoard bb = new BoggleBoard(args[0]);
        int count = 0;
        for (String s : bs.getAllValidWords(bb)) {
            count += bs.scoreOf(s);
        }
        StdOut.print(count);
    }
}
