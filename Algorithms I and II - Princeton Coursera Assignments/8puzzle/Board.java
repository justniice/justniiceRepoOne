import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] currentState;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        currentState = new int[tiles[0].length][tiles[0].length];
        for (int i = 0; i < tiles[0].length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                currentState[i][j] = tiles[i][j];
            }
        }
        this.dimension = this.currentState[0].length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension);
        s.append("\n");
        for (int i = 0; i < this.dimension(); i++) {
            for (int entry : this.currentState[i]) {
                s.append("  ");
                if (entry < 10) {
                    s.append(" ");
                }
                s.append(entry);
            }
            s.append("\n");
        }
        String toReturn = s.toString();
        return toReturn;
    }

    // board dimension n
    public int dimension() {
        return this.dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int toReturn = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tileNumber = currentState[i][j];
                if (tileNumber != 0) {
                    if (i != (tileNumber - 1) / dimension
                            || j != (tileNumber - 1) % dimension) {
                        toReturn += 1;
                    }
                }
            }
        }
        return toReturn;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int toReturn = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tileNumber = currentState[i][j];
                if (tileNumber != 0) {
                    if (i > (tileNumber - 1) / dimension) {
                        toReturn += (i - (tileNumber - 1) / dimension);
                    }
                    else {
                        toReturn += ((tileNumber - 1) / dimension - i);
                    }
                    if (j > (tileNumber - 1) % dimension) {
                        toReturn += (j - (tileNumber - 1) % dimension);
                    }
                    else {
                        toReturn += ((tileNumber - 1) % dimension - j);
                    }
                }
            }
        }
        return toReturn;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (currentState[i][j] != i * dimension + j + 1) {
                    if (i != dimension - 1 || j != dimension - 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        try {
            if (y == null) {
                return false;
            }
            Board toCheck = (Board) y;
            if (this.dimension != toCheck.dimension) {
                return false;
            }
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (currentState[i][j] != toCheck.currentState[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        catch (ClassCastException e) {
            // StdOut.print(e.getStackTrace());
            return false;
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        Board b = this;
        int rowOfZero = 0;
        int columnOfZero = 0;
        for (int i = 0; i < b.dimension; i++) {
            for (int j = 0; j < b.dimension; j++) {
                if (b.currentState[i][j] == 0) {
                    rowOfZero = i;
                    columnOfZero = j;
                }
            }
        }
        if (rowOfZero != 0) {
            int[][] toAdd = new int[b.dimension][b.dimension];
            for (int i = 0; i < b.dimension; i++) {
                for (int j = 0; j < b.dimension; j++) {
                    toAdd[i][j] = b.currentState[i][j];
                }
            }
            int temp = toAdd[rowOfZero - 1][columnOfZero];
            toAdd[rowOfZero - 1][columnOfZero] = 0;
            toAdd[rowOfZero][columnOfZero] = temp;
            q.enqueue(new Board(toAdd));
        }
        if (rowOfZero != b.dimension - 1) {
            int[][] toAdd = new int[b.dimension][b.dimension];
            for (int i = 0; i < b.dimension; i++) {
                for (int j = 0; j < b.dimension; j++) {
                    toAdd[i][j] = b.currentState[i][j];
                }
            }
            int temp = toAdd[rowOfZero + 1][columnOfZero];
            toAdd[rowOfZero + 1][columnOfZero] = 0;
            toAdd[rowOfZero][columnOfZero] = temp;
            q.enqueue(new Board(toAdd));
        }
        if (columnOfZero != 0) {
            int[][] toAdd = new int[b.dimension][b.dimension];
            for (int i = 0; i < b.dimension; i++) {
                for (int j = 0; j < b.dimension; j++) {
                    toAdd[i][j] = b.currentState[i][j];
                }
            }
            int temp = toAdd[rowOfZero][columnOfZero - 1];
            toAdd[rowOfZero][columnOfZero - 1] = 0;
            toAdd[rowOfZero][columnOfZero] = temp;
            q.enqueue(new Board(toAdd));
        }
        if (columnOfZero != b.dimension - 1) {
            int[][] toAdd = new int[b.dimension][b.dimension];
            for (int i = 0; i < b.dimension; i++) {
                for (int j = 0; j < b.dimension; j++) {
                    toAdd[i][j] = b.currentState[i][j];
                }
            }
            int temp = toAdd[rowOfZero][columnOfZero + 1];
            toAdd[rowOfZero][columnOfZero + 1] = 0;
            toAdd[rowOfZero][columnOfZero] = temp;
            q.enqueue(new Board(toAdd));
        }
        return q;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] toReturn = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                toReturn[i][j] = currentState[i][j];
            }
        }
        if (this.currentState[0][0] == 0) {
            toReturn[1][0] = this.currentState[0][1];
            toReturn[0][1] = this.currentState[1][0];
            return new Board(toReturn);
        }
        else if (this.currentState[0][1] != 0) {
            toReturn[0][1] = this.currentState[0][0];
            toReturn[0][0] = this.currentState[0][1];
            return new Board(toReturn);
        }
        else {
            toReturn[1][0] = this.currentState[0][0];
            toReturn[0][0] = this.currentState[1][0];
            return new Board(toReturn);
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] testArray = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] testArray2 = { { 0, 2, 3 }, { 5, 1, 4 }, { 6, 8, 7 } };
        Board testBoard = new Board(testArray);
        StdOut.print(testBoard.hamming());
        StdOut.print(testBoard.manhattan());
        Board testBoard2 = new Board(testArray2);
        StdOut.print(testBoard.equals(testBoard2));
    }
}
