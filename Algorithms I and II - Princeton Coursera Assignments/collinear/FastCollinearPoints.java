/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final LineSegment[] toReturn;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        for (int n = 0; n < points.length; n++) {
            if (points[n] == null) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
        Point[] points1 = points.clone();
        Arrays.sort(points1);
        for (int n = 0; n < points1.length - 1; n++) {
            if (points1[n].compareTo(points1[n + 1]) == 0) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
        ArrayDeque<LineSegment> segments = new ArrayDeque<LineSegment>();
        for (int i = 0; i < points1.length; i++) {
            Comparator<Point> comparer = points[i].slopeOrder();
            Arrays.sort(points1, comparer);
            for (int j = 1; j < points1.length - 2; j++) {
                int hits = 1;
                int pointer = j + 1;
                while (comparer.compare(points1[j], points1[pointer]) == 0) {
                    hits++;
                    pointer++;
                    if (pointer == points1.length) {
                        break;
                    }
                }
                if (hits >= 3) {
                    Point[] inALine = new Point[hits + 1];
                    for (int m = j; m < pointer; m++) {
                        inALine[m - j] = points1[m];
                    }
                    inALine[hits] = points1[0];
                    Arrays.sort(inALine);
                    if (inALine[0].equals(points1[0])) {
                        segments.add(new LineSegment(inALine[0], inALine[hits]));
                    }
                }
                j = pointer - 1;
            }
        }
        toReturn = new LineSegment[segments.size()];
        int returnNumber = segments.size();
        for (int i = 0; i < returnNumber; i++) {
            toReturn[i] = segments.remove();
        }

    }


    public int numberOfSegments() {
        return toReturn.length;
    }     // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] toReturnA = toReturn.clone();
        return toReturnA;
    }
}
