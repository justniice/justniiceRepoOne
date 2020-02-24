/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private final Digraph wordGraph;
    private final SeparateChainingHashST<String, Bag<Integer>> synsetsHashT;
    private final String[] synsetsArray;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        In synsetStream = new In(synsets);
        In hypernymsStream = new In(hypernyms);
        Queue<String[]> hypernymsQueue = new Queue<String[]>();
        while (hypernymsStream.hasNextLine()) {
            hypernymsQueue.enqueue(hypernymsStream.readLine().split(","));
        }
        SeparateChainingHashST<String, Bag<Integer>> synsetshash
                = new SeparateChainingHashST<String, Bag<Integer>>();
        String[] synsetsA = new String[hypernymsQueue.size()];
        for (int i = 0; i < hypernymsQueue.size(); i++) {
            String[] temp = synsetStream.readLine().split(",");
            synsetsA[i] = temp[1];
            for (String s : temp[1].split(" ")) {
                if (synsetshash.contains(s)) {
                    Bag<Integer> b = synsetshash.get(s);
                    b.add(Integer.parseInt(temp[0]));
                    synsetshash.put(s, b);
                }
                else {
                    Bag<Integer> b = new Bag<Integer>();
                    b.add(Integer.parseInt(temp[0]));
                    synsetshash.put(s, b);
                }
            }
        }
        this.synsetsArray = synsetsA;
        this.synsetsHashT = synsetshash;
        Digraph dg = new Digraph(hypernymsQueue.size());
        while (!hypernymsQueue.isEmpty()) {
            String[] toParse = hypernymsQueue.dequeue();
            for (int i = 1; i < toParse.length; i++) {
                dg.addEdge(Integer.parseInt(toParse[0]), Integer.parseInt(toParse[i]));
            }
        }
        int roots = 0;
        for (int i = 0; i < dg.V(); i++) {
            if (dg.outdegree(i) == 0) {
                StdOut.print(i + " is the root!");
                roots++;
            }
        }
        if (roots != 1) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        boolean[] searched = new boolean[dg.V()];
        boolean[] marked = new boolean[dg.V()];
        for (int i = 0; i < dg.V(); i++) {
            marked[i] = false;
            searched[i] = false;
        }
        for (int j = 0; j < dg.V(); j++) {
            if (searched[j] == false) {
                Stack<Integer> recursionStack = new Stack<Integer>();
                MyDFS(dg, j, marked, searched, recursionStack);
            }
        }
        this.wordGraph = dg;
        this.sap = new SAP(this.wordGraph);
    }

    private void MyDFS(Digraph dg, int i, boolean[] marked, boolean[] searched,
                       Stack<Integer> recursionStack) {
        if (marked[i]) {
            StdOut.print("Loop found.");
            while (!recursionStack.isEmpty()) {
                StdOut.print(recursionStack.pop());
            }
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        marked[i] = true;
        searched[i] = true;
        recursionStack.push(i);
        for (int k : dg.adj(i)) {
            MyDFS(dg, k, marked, searched, recursionStack);
        }
        while (!recursionStack.isEmpty()) {
            marked[recursionStack.pop()] = false;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsHashT.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        return synsetsHashT.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        return sap.length(synsetsHashT.get(nounA), synsetsHashT.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        return synsetsArray[sap.ancestor(synsetsHashT.get(nounA), synsetsHashT.get(nounB))];
    }

    /*
     do unit testing of this class
     */
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets6.txt", "hypernyms6InvalidCycle.txt");
    }
}
