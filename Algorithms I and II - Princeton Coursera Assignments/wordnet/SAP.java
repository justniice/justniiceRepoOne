/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph dg;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        this.dg = new Digraph(G.V());
        for (int i = 0; i < G.V(); i++) {
            for (int j : G.adj(i)) {
                dg.addEdge(i, j);
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= dg.V() || w >= dg.V() || v < 0 || w < 0) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        int shortest = dg.V() + 1;
        BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths BFSW = new BreadthFirstDirectedPaths(dg, w);
        for (int i = 0; i < dg.V(); i++) {
            if (BFSV.hasPathTo(i) && BFSW.hasPathTo(i)) {
                int candidateDistance = BFSV.distTo(i) + BFSW.distTo(i);
                if (candidateDistance < shortest) {
                    shortest = candidateDistance;
                }
            }
        }
        if (shortest < dg.V() + 1) {
            return shortest;
        }
        else {
            return -1;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v >= dg.V() || w >= dg.V() || v < 0 || w < 0) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        int shortest = dg.V() + 1;
        BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths BFSW = new BreadthFirstDirectedPaths(dg, w);
        int candidateNode = -1;
        for (int i = 0; i < dg.V(); i++) {
            if (BFSV.hasPathTo(i) && BFSW.hasPathTo(i)) {
                int candidateDistance = BFSV.distTo(i) + BFSW.distTo(i);
                if (candidateDistance < shortest) {
                    shortest = candidateDistance;
                    candidateNode = i;
                }
            }
        }
        return candidateNode;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        for (Integer i : v) {
            if (i == null || i >= dg.V() || i < 0) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
        for (Integer j : w) {
            if (j == null || j >= dg.V() || j < 0) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
        int shortest = dg.V() + 1;
        BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths BFSW = new BreadthFirstDirectedPaths(dg, w);
        for (int i = 0; i < dg.V(); i++) {
            if (BFSV.hasPathTo(i) && BFSW.hasPathTo(i)) {
                int candidateDistance = BFSV.distTo(i) + BFSW.distTo(i);
                if (candidateDistance < shortest) {
                    shortest = candidateDistance;
                }
            }
        }
        if (shortest < dg.V() + 1) {
            return shortest;
        }
        else {
            return -1;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        for (Integer i : v) {
            if (i == null || i >= dg.V() || i < 0) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
        for (Integer j : w) {
            if (j == null || j >= dg.V() || j < 0) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
        int shortest = dg.V() + 1;
        BreadthFirstDirectedPaths BFSV = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths BFSW = new BreadthFirstDirectedPaths(dg, w);
        int candidateNode = -1;
        for (int i = 0; i < dg.V(); i++) {
            if (BFSV.hasPathTo(i) && BFSW.hasPathTo(i)) {
                int candidateDistance = BFSV.distTo(i) + BFSW.distTo(i);
                if (candidateDistance < shortest) {
                    shortest = candidateDistance;
                    candidateNode = i;
                }
            }
        }
        return candidateNode;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
