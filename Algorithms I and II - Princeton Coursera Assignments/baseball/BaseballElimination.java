/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BaseballElimination {
    private final int nTeams;
    private final String[] names;
    private final int[] wins;
    private final int[] losses;
    private final int[] left;
    private final int[][] gamesToPlay;
    private final SeparateChainingHashST<String, Integer> namesToInts;
    private FordFulkerson ff;

    public BaseballElimination(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
            nTeams = sc.nextInt();
            sc.nextLine();
            names = new String[nTeams];
            wins = new int[nTeams];
            losses = new int[nTeams];
            left = new int[nTeams];
            gamesToPlay = new int[nTeams][nTeams];
            namesToInts = new SeparateChainingHashST<String, Integer>();
            for (int i = 0; i < nTeams; i++) {
                String[] data = sc.nextLine().trim().split("\\s+");
                names[i] = data[0];
                wins[i] = Integer.parseInt(data[1]);
                losses[i] = Integer.parseInt(data[2]);
                left[i] = Integer.parseInt(data[3]);
                for (int j = 4; j < nTeams + 4; j++) {
                    gamesToPlay[i][j - 4] = Integer.parseInt(data[j]);
                }
                namesToInts.put(names[i], i);
            }
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }

    }

    public int numberOfTeams() {
        return nTeams;
    }

    public Iterable<String> teams() {
        Bag<String> b = new Bag<String>();
        for (String s : names) {
            b.add(s);
        }
        return b;
    }

    public int wins(String team) {
        if (!namesToInts.contains(team)) {
            throw new IllegalArgumentException();
        }
        int index = namesToInts.get(team);
        return wins[index];
    }

    public int losses(String team) {
        if (!namesToInts.contains(team)) {
            throw new IllegalArgumentException();
        }
        int index = namesToInts.get(team);
        return losses[index];
    }

    public int remaining(String team) {
        if (!namesToInts.contains(team)) {
            throw new IllegalArgumentException();
        }
        int index = namesToInts.get(team);
        return left[index];
    }

    public int against(String team1, String team2) {
        if (!namesToInts.contains(team1) || !namesToInts.contains(team2)) {
            throw new IllegalArgumentException();
        }
        int index1 = namesToInts.get(team1);
        int index2 = namesToInts.get(team2);
        assert (gamesToPlay[index1][index2] == gamesToPlay[index2][index1]);
        return gamesToPlay[index1][index2];
    }

    public boolean isEliminated(String team) {
        if (!namesToInts.contains(team)) {
            throw new IllegalArgumentException();
        }
        int index = namesToInts.get(team);
        for (int i = 0; i < nTeams; i++) {
            if (wins[index] + left[index] < wins[i]) {
                return true;
            }
        }
        FlowNetwork fn = new FlowNetwork(1 + nTeams * nTeams + nTeams + 1);
        int nVertices = fn.V();
        int upperLimit = 0;
        for (int i = 0; i < nTeams; i++) {
            for (int j = i; j < nTeams; j++) {
                if (i != index && j != index && gamesToPlay[i][j] > 0) {
                    FlowEdge srcToGame = new FlowEdge(0, nTeams * i + j + 1, gamesToPlay[i][j]);
                    upperLimit += gamesToPlay[i][j];
                    fn.addEdge(srcToGame);
                    FlowEdge gameToTeam1 = new FlowEdge(nTeams * i + j + 1, nTeams * nTeams + i + 1,
                                                        Double.POSITIVE_INFINITY);
                    FlowEdge gameToTeam2 = new FlowEdge(nTeams * i + j + 1, nTeams * nTeams + j + 1,
                                                        Double.POSITIVE_INFINITY);
                    fn.addEdge(gameToTeam1);
                    fn.addEdge(gameToTeam2);
                }
            }
        }
        for (int i = 0; i < nTeams; i++) {
            int capacity = wins[index] + left[index] - wins[i];
            if (i != index && capacity > 0) {
                FlowEdge teamToSink = new FlowEdge(nTeams * nTeams + i + 1, nVertices - 1,
                                                   capacity);
                fn.addEdge(teamToSink);
            }
        }
        ff = new FordFulkerson(fn, 0, nVertices - 1);
        return (upperLimit > ff.value());
    }

    public Iterable<String> certificateOfElimination(String team) {
        Bag<String> b = new Bag<String>();
        int index = namesToInts.get(team);
        for (int i = 0; i < nTeams; i++) {
            if (wins[index] + left[index] < wins[i]) {
                b.add(names[i]);
            }
            if (!b.isEmpty()) {
                return b;
            }
        }
        if (isEliminated(team)) {
            for (int i = nTeams * nTeams + 1; i <= nTeams * (nTeams + 1); i++) {
                if (ff.inCut(i)) {
                    b.add(names[i - (nTeams * nTeams + 1)]);
                }
            }
            return b;
        }
        else {
            return null;
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}
