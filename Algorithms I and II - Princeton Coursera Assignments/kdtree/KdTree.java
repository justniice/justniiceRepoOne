import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private boolean isHorizontal;
        private Point2D point;
        private Node left;
        private Node right;

        private Node(Point2D point, boolean isHorizontal) {
            this.isHorizontal = isHorizontal;
            this.point = point;
        }

        private void put(Point2D p) {
            double compare;
            if (this.isHorizontal) {
                compare = point.y() - p.y();
            }
            else {
                compare = point.x() - p.x();
            }
            if (compare < 0) {
                if (this.right == null) {
                    this.right = new Node(p, !isHorizontal);
                }
                else {
                    this.right.put(p);
                }
            }
            else {
                if (this.left == null) {
                    this.left = new Node(p, !isHorizontal);
                }
                else {
                    this.left.put(p);
                }
            }
        }

        private Node search(Point2D p) {
            if (this.point.x() == p.x() && this.point.y() == p.y()) {
                return this;
            }
            double compare;
            if (this.isHorizontal) {
                compare = point.y() - p.y();
            }
            else {
                compare = point.x() - p.x();
            }
            if (compare < 0) {
                if (this.right != null) {
                    return this.right.search(p);
                }
                else return null;
            }
            else {
                if (this.left != null) {
                    return this.left.search(p);
                }
                else return null;
            }
        }

        private void inOrder(Queue<Point2D> q) {
            if (this.left != null) {
                this.left.inOrder(q);
            }
            q.enqueue(this.point);
            if (this.right != null) {
                this.right.inOrder(q);
            }
        }

        private void drawSearch(double xMin, double yMin, double xMax, double yMax) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            point.draw();
            StdDraw.setPenRadius(0.005);
            if (isHorizontal) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(xMin, point.y(), xMax, point.y());
                if (this.left != null) {
                    this.left.drawSearch(xMin, yMin, xMax, point.y());
                }
                if (this.right != null) {
                    this.right.drawSearch(xMin, point.y(), xMax, yMax);
                }
            }
            if (!isHorizontal) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), yMin, point.x(), yMax);
                if (this.left != null) {
                    this.left.drawSearch(xMin, yMin, point.x(), yMax);
                }
                if (this.right != null) {
                    this.right.drawSearch(point.x(), yMin, xMax, yMax);
                }
            }
        }

        private Point2D proximitySearch(Point2D p) {
            Point2D best = this.point;
            if (p.x() <= this.point.x()) {
                if (this.left != null) {
                    best = this.left.proximitySearch(p, best);
                }
                if (best.distanceSquaredTo(p) > (this.point.x() - p.x()) * (this.point.x() - p.x())
                        && this.right != null) {
                    best = this.right.proximitySearch(p, best);
                }
            }
            else {
                if (this.right != null) {
                    best = this.right.proximitySearch(p, best);
                }
                if (best.distanceSquaredTo(p) > (p.x() - this.point.x()) * (p.x() - this.point.x())
                        && this.left != null) {
                    best = this.left.proximitySearch(p, best);
                }
            }
            return best;
        }

        private Point2D proximitySearch(Point2D p, Point2D best) {
            Point2D bestest = best;
            if (p.distanceSquaredTo(this.point) < p.distanceSquaredTo(bestest)) {
                bestest = this.point;
            }
            if (this.isHorizontal) {
                if (p.y() <= this.point.y()) {
                    if (this.left != null) {
                        bestest = this.left.proximitySearch(p, bestest);
                    }
                    if (bestest.distanceSquaredTo(p) > (this.point.y() - p.y()) * (this.point.y()
                            - p.y())
                            && this.right != null) {
                        bestest = this.right.proximitySearch(p, bestest);
                    }
                }
                else {
                    if (this.right != null) {
                        bestest = this.right.proximitySearch(p, bestest);
                    }
                    if (bestest.distanceSquaredTo(p) > (p.y() - this.point.y()) * (p.y()
                            - this.point.y())
                            && this.left != null) {
                        bestest = this.left.proximitySearch(p, bestest);
                    }
                }
            }
            else {
                if (p.x() <= this.point.x()) {
                    if (this.left != null) {
                        bestest = this.left.proximitySearch(p, bestest);
                    }
                    if (bestest.distanceSquaredTo(p) > (this.point.x() - p.x()) * (this.point.x()
                            - p.x())
                            && this.right != null) {
                        bestest = this.right.proximitySearch(p, bestest);
                    }
                }
                else {
                    if (this.right != null) {
                        bestest = this.right.proximitySearch(p, bestest);
                    }
                    if (bestest.distanceSquaredTo(p) > (p.x() - this.point.x()) * (p.x()
                            - this.point.x())
                            && this.left != null) {
                        bestest = this.left.proximitySearch(p, bestest);
                    }
                }

            }
            return bestest;
        }

        private void rectSearch(RectHV rect, Queue<Point2D> q) {
            if (this.isHorizontal) {
                if (rect.ymin() <= point.y() && this.left != null) {
                    this.left.rectSearch(rect, q);
                }
                if (rect.ymax() >= point.y() && this.right != null) {
                    this.right.rectSearch(rect, q);
                }
            }
            if (rect.contains(this.point)) {
                q.enqueue(this.point);
            }
            if (!this.isHorizontal) {
                if (rect.xmin() <= point.x() && this.left != null) {
                    this.left.rectSearch(rect, q);
                }
                if (rect.xmax() >= point.x() && this.right != null) {
                    this.right.rectSearch(rect, q);
                }
            }
        }
    }

    public KdTree() {
        this.root = null;
        this.size = 0;
    }                              // construct an empty set of points

    public boolean isEmpty() {
        return this.size == 0;
    }                    // is the set empty?

    public int size() {
        return this.size;
    }                    // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        else if (this.root == null) {
            this.size++;
            this.root = new Node(p, false);
        }
        else {
            if (!this.contains(p)) {
                this.size++;
                this.root.put(p);
            }
        }
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (size == 0) {
            return false;
        }
        return (this.root.search(p) != null);
    }            // does the set contain point p?

    public void draw() {
        root.drawSearch(0, 0, 1, 1);
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (this.size == 0) {
            return null;
        }
        Queue<Point2D> q = new Queue<Point2D>();
        root.rectSearch(rect, q);
        return q;
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        if (size == 0) {
            return null;
        }
        return root.proximitySearch(p);
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        KdTree kd = new KdTree();
        kd.insert(new Point2D(0.5, 0.5));
        kd.insert(new Point2D(0.3, 0.3));
        kd.insert(new Point2D(0.7, 0.7));
        kd.insert(new Point2D(0.4, 0.4));
        kd.insert(new Point2D(0.6, 0.6));
        kd.insert(new Point2D(0.9, 0.9));
        kd.draw();

    }
}
