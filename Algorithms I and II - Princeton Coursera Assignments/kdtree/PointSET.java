/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private int size;
    private TreeSet<Point2D> tree;

    public PointSET() {
        this.size = 0;
        this.tree = new TreeSet<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return (this.size == 0);
    }                  // is the set empty?

    public int size() {
        return this.size;
    }                     // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (!this.tree.contains(p)) {
            this.tree.add(p);
            this.size++;
        }
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        return this.tree.contains(p);
    }        // does the set contain point p?

    public void draw() {
        for (Point2D p : this.tree) {
            p.draw();
        }
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : this.tree) {
            if (rect.contains(p)) {
                q.enqueue(p);
            }
        }
        return q;
    }             // all points that are inside the rectangle (or on the boundary)


    public Point2D nearest(Point2D p) {
        if (p == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (this.tree.isEmpty()) {
            return null;
        }
        else {
            Point2D toReturn = new Point2D(2.1, 2.1);
            for (Point2D point : this.tree) {
                if (p.distanceTo(point) < p.distanceTo(toReturn)) {
                    toReturn = point;
                }
            }
            return toReturn;
        }
    }             // a nearest neighbor in the set to point p; null if the set is empty
}
