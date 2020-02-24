/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private final LineSegment[] toReturn;

    public BruteCollinearPoints(Point[] points) {
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
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        Point[] tempArray = { points[i], points[j], points[k], points[m] };
                        Arrays.sort(tempArray);
                        Comparator<Point> comparer = tempArray[0].slopeOrder();
                        if (comparer.compare(tempArray[1], tempArray[2]) == 0
                                && comparer.compare(tempArray[1], tempArray[3]) == 0) {
                            segments.add(new LineSegment(tempArray[0], tempArray[3]));
                        }
                    }
                }
            }
        }
        toReturn = new LineSegment[segments.size()];
        int returnNumber = segments.size();
        for (int i = 0; i < returnNumber; i++) {
            toReturn[i] = segments.remove();
        }
    }   // finds all line segments containing 4 points

    public int numberOfSegments() {
        return toReturn.length;
    }     // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] toReturnA = toReturn.clone();
        return toReturnA;
    }        // the line segments

}
