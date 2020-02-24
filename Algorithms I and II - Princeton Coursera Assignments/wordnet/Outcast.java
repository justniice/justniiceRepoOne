/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wn;

    public Outcast(WordNet wordnet) {
        this.wn = wordnet;
    }

    public String outcast(String[] nouns) {
        int distanceSum = -1;
        String toReturn = "";
        for (int i = 0; i < nouns.length; i++) {
            int temp = 0;
            for (int j = 0; j < nouns.length; j++) {
                temp += wn.distance(nouns[i], nouns[j]);
            }
            if (temp > distanceSum) {
                distanceSum = temp;
                toReturn = nouns[i];
            }
        }
        return toReturn;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
