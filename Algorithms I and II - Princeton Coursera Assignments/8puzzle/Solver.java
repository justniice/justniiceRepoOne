/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Node solution;
    private final boolean solvable;
    private final int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        MinPQ<Node> originalQ = new MinPQ<Node>();
        MinPQ<Node> mirrorQ = new MinPQ<Node>();
        originalQ.insert(new Node(initial, 0));
        mirrorQ.insert(new Node(initial.twin(), 0));
        while (true) {
            Node stateOne = originalQ.delMin();
            if (stateOne.getTheBoard().isGoal()) {
                solution = stateOne;
                solvable = true;
                this.moves = stateOne.getDepth();
                break;
            }
            Node stateTwo = mirrorQ.delMin();
            if (stateTwo.getTheBoard().isGoal()) {
                solution = stateTwo;
                solvable = false;
                this.moves = stateOne.getDepth();
                break;
            }
            for (Board nextBoards : stateOne.getTheBoard().neighbors()) {
                if (!nextBoards.equals(stateOne.parent.getTheBoard())) {
                    originalQ.insert(new Node(nextBoards, stateOne.getDepth() + 1, stateOne));
                }
            }
            for (Board nextBoards2 : stateTwo.getTheBoard().neighbors()) {
                if (!nextBoards2.equals(stateTwo.parent.getTheBoard())) {
                    mirrorQ.insert(new Node(nextBoards2, stateTwo.getDepth() + 1, stateTwo));
                }
            }
        }
    }

    private class Node implements Comparable<Node> {
        private final int manhattanPriority;
        private final int hammingPriority;
        private final Board theBoard;
        private final int depth;
        private final Node parent;

        public Node(Board nodeBoard, int depth) {
            this.manhattanPriority = depth + nodeBoard.manhattan();
            this.hammingPriority = depth + nodeBoard.hamming();
            this.depth = depth;
            this.theBoard = nodeBoard;
            this.parent = this;
        }

        public Node(Board nodeBoard, int depth, Node parent) {
            this.manhattanPriority = depth + nodeBoard.manhattan();
            this.hammingPriority = depth + nodeBoard.hamming();
            this.depth = depth;
            this.theBoard = nodeBoard;
            this.parent = parent;
        }

        public int compareTo(Node that) {
            return this.manhattanPriority - that.manhattanPriority;
        }

        public Board getTheBoard() {
            return this.theBoard;
        }

        public int getDepth() {
            return this.depth;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> s = new Stack<Board>();
        Node aNode = solution;
        while (true) {
            if (aNode.getTheBoard().equals(aNode.parent.getTheBoard())) {
                s.push(aNode.getTheBoard());
                break;
            }
            s.push(aNode.getTheBoard());
            aNode = aNode.parent;
        }
        return s;
    }

    // test client (see below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
