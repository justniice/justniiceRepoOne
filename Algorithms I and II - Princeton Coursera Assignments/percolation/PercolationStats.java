import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trialTotal;
    private final double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        results = new double[trials];
        trialTotal = trials;
        for (int i = 0; i < trials; i++) {
            int flipOpen = 0;
            Percolation perc = new Percolation(n);
            int[] dummy = new int[n * n];
            for (int j = 0; j < n * n; j++) {
                dummy[j] = j;
            }
            StdRandom.shuffle(dummy);
            int k = -1;
            while (!perc.percolates()) {
                k++;
                perc.open(dummy[k] / n + 1, dummy[k] % n + 1);
                flipOpen++;
            }
            results[i] = ((double) flipOpen) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - (1.96 * this.stddev() / Math.sqrt(trialTotal));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + (1.96 * this.stddev() / Math.sqrt(trialTotal));
    }

    // test client (see below)
    public static void main(String[] args) {

        PercolationStats go = new PercolationStats(Integer.parseInt(args[0]),
                                                   Integer.parseInt(args[1]));
        System.out.printf("%-25s %3s %f", "mean", "=", go.mean());
        System.out.println(" ");
        System.out.printf("%-25s %3s %f", "stddev", "=", go.stddev());
        System.out.println(" ");
        System.out.printf("%-25s %3s %f %f", "95% confidence interval", "=", go.confidenceLo(),
                          go.confidenceHi());

    }

}
