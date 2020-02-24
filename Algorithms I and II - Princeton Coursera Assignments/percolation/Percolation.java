public class Percolation {
    private final int sideLength;
    private int[] theBox;
    private boolean[] openSquares;
    private int[] ranks;
    private int opened;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        opened = 0;
        sideLength = n;
        theBox = new int[n * n + 2];
        openSquares = new boolean[n * n + 2];
        ranks = new int[n * n + 2];
        for (int i = 0; i <= (n * n + 1); i++) {
            if (i == 0 || i == n * n + 1) {
                openSquares[i] = true;
            }
            else {
                openSquares[i] = false;
            }
            theBox[i] = i;
            ranks[i] = 0;
        }
    }

    private int find(int box) {
        while (theBox[box] != theBox[theBox[box]]) {
            theBox[box] = find(theBox[box]);
        }
        return theBox[box];
    }

    private void union(int box1, int box2) {
        int a = find(box1);
        int b = find(box2);
        if (a != b) {
            if (ranks[a] < ranks[b]) {
                theBox[a] = b;
            }
            else {
                if (ranks[a] == ranks[b]) {
                    ranks[a] += 1;
                }
                theBox[b] = a;
            }
        }
    }

    private boolean connected(int box1, int box2) {
        return (find(box1) == find(box2));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        opened++;
        int sqNo = (row - 1) * sideLength + col;
        if (!(1 <= sqNo) || !(sqNo <= sideLength * sideLength)) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (openSquares[sqNo]) {
            return;
        }
        openSquares[sqNo] = true;
        if ((sqNo + 1) % sideLength != 1 && openSquares[sqNo + 1]) {
            union(sqNo, sqNo + 1);
        }
        if ((sqNo - 1) % sideLength != 0 && openSquares[sqNo - 1]) {
            union(sqNo, sqNo - 1);
        }
        if (openSquares[Math.max(sqNo - sideLength, 0)]) {
            union(sqNo, Math.max(sqNo - sideLength, 0));
        }
        if (openSquares[Math.min(sqNo + sideLength, sideLength * sideLength + 1)]) {
            union(sqNo, Math.min(sqNo + sideLength, sideLength * sideLength + 1));
        }
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int sqNo = (row - 1) * sideLength + col;
        if (!(1 <= sqNo) || !(sqNo <= sideLength * sideLength)) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        return openSquares[sqNo];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int sqNo = (row - 1) * sideLength + col;
        if (!(1 <= sqNo) || !(sqNo <= sideLength * sideLength)) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        return connected(sqNo, 0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opened;
    }

    // does the system percolate?
    public boolean percolates() {
        return connected(0, sideLength * sideLength + 1);
    }

    // test client (optional)
    // public static void main(String[] args)
}
