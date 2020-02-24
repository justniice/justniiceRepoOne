/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {
    private double[][] energies;
    private Picture thePicture;
    private int[][] colourArray;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.thePicture = new Picture(picture);
        this.width = thePicture.width();
        this.height = thePicture.height();
        this.colourArray = new int[picture.width()][picture.height()];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                colourArray[i][j] = thePicture.getRGB(i, j);
            }
        }
        this.calculateEnergies();
    }

    private void calculateEnergies() {
        this.energies = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
                    energies[i][j] = 1000;
                }
                else {
                    int rgb1 = colourArray[i - 1][j];
                    int r1 = (rgb1 >> 16) & 0xFF;
                    int g1 = (rgb1 >> 8) & 0xFF;
                    int b1 = (rgb1 >> 0) & 0xFF;
                    int rgb2 = colourArray[i + 1][j];
                    int r2 = (rgb2 >> 16) & 0xFF;
                    int g2 = (rgb2 >> 8) & 0xFF;
                    int b2 = (rgb2 >> 0) & 0xFF;
                    double xEnergySquared = (r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2)
                            + (b1 - b2) * (b1 - b2);
                    int rgb3 = colourArray[i][j - 1];
                    int r3 = (rgb3 >> 16) & 0xFF;
                    int g3 = (rgb3 >> 8) & 0xFF;
                    int b3 = (rgb3 >> 0) & 0xFF;
                    int rgb4 = colourArray[i][j + 1];
                    int r4 = (rgb4 >> 16) & 0xFF;
                    int g4 = (rgb4 >> 8) & 0xFF;
                    int b4 = (rgb4 >> 0) & 0xFF;
                    double yEnergySquared = (r3 - r4) * (r3 - r4) + (g3 - g4) * (g3 - g4)
                            + (b3 - b4) * (b3 - b4);
                    energies[i][j] = Math.sqrt(xEnergySquared + yEnergySquared);
                }
            }
        }
    }

    public Picture picture() {
        return this.thePicture;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IllegalArgumentException();
        }
        return energies[x][y];
    }

    public int[] findHorizontalSeam() {
        int[] edgeTo = new int[width() * height() + 2];
        double[] distTo = new double[width() * height() + 2];
        for (int i = 0; i < width() * height() + 2; i++) {
            if (i == 0) {
                edgeTo[i] = 0;
                distTo[i] = 0;
            }
            if (1 <= i && i <= height()) {
                edgeTo[i] = 0;
                distTo[i] = energies[(i - 1) / height()][(i - 1) % height()];
            }
            else {
                distTo[i] = Double.POSITIVE_INFINITY;
            }
        }
        for (int k = 1; k <= width() * height(); k++) {
            for (int j : horizontalNeighbours(k)) {
                if (j == width() * height() + 1) {
                    if (distTo[j] > distTo[k]) {
                        distTo[j] = distTo[k];
                        edgeTo[j] = k;
                    }
                }
                else {
                    if (distTo[j] > (distTo[k] + energies[(j - 1) / height()][(j - 1)
                            % height()])) {
                        distTo[j] = distTo[k] + energies[(j - 1) / height()][(j - 1) % height()];
                        edgeTo[j] = k;
                    }
                }
            }
        }
        int path = width() * height() + 1;
        int[] toReturn = new int[width()];
        for (int l = width() - 1; l >= 0; l--) {
            path = edgeTo[path];
            toReturn[l] = (path - 1) % height();
        }
        return toReturn;
    }

    private Iterable<Integer> horizontalNeighbours(int x) {
        Bag<Integer> b = new Bag<Integer>();
        if (x == 0) {
            for (int i = 1; i <= height(); i++) {
                b.add(i);
            }
        }
        else if (x > height() * (width() - 1)) {
            b.add(height() * width() + 1);
        }
        else if (x % height() == 0) {
            b.add(x + height());
            b.add(x + height() - 1);
        }
        else if (x % height() == 1) {
            b.add(x + height());
            b.add(x + height() + 1);
        }
        else {
            b.add(x + height());
            b.add(x + height() - 1);
            b.add(x + height() + 1);
        }
        return b;
    }

    public int[] findVerticalSeam() {
        int[] edgeTo = new int[width() * height() + 2];
        double[] distTo = new double[width() * height() + 2];
        for (int i = 0; i < width() * height() + 2; i++) {
            if (i == 0) {
                edgeTo[i] = 0;
                distTo[i] = 0;
            }
            if (1 <= i && i <= width()) {
                edgeTo[i] = 0;
                distTo[i] = energies[(i - 1) % width()][(i - 1) / width()];
            }
            else {
                distTo[i] = Double.POSITIVE_INFINITY;
            }
        }
        for (int k = 1; k <= width() * height(); k++) {
            for (int j : verticalNeighbours(k)) {
                if (j == width() * height() + 1) {
                    if (distTo[j] > distTo[k]) {
                        distTo[j] = distTo[k];
                        edgeTo[j] = k;
                    }
                }
                else {
                    if (distTo[j] > (distTo[k] + energies[(j - 1) % width()][(j - 1)
                            / width()])) {
                        distTo[j] = distTo[k] + energies[(j - 1) % width()][(j - 1) / width()];
                        edgeTo[j] = k;
                    }
                }
            }
        }
        int path = width() * height() + 1;
        int[] toReturn = new int[height()];
        for (int l = height() - 1; l >= 0; l--) {
            path = edgeTo[path];
            toReturn[l] = (path - 1) % width();
        }
        return toReturn;
    }

    private Iterable<Integer> verticalNeighbours(int x) {
        Bag<Integer> b = new Bag<Integer>();
        if (x == 0) {
            for (int i = 1; i <= width(); i++) {
                b.add(i);
            }
        }
        else if (x > width() * (height() - 1)) {
            b.add(height() * width() + 1);
        }
        else if (x % width() == 0) {
            b.add(x + width());
            b.add(x + width() - 1);
        }
        else if (x % width() == 1) {
            b.add(x + width());
            b.add(x + width() + 1);
        }
        else {
            b.add(x + width());
            b.add(x + width() - 1);
            b.add(x + width() + 1);
        }
        return b;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != width || height <= 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height) {
                throw new IllegalArgumentException();
            }
            if (i < seam.length - 1) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
        int[][] newColourArray = new int[width()][height() - 1];
        for (int i = 0; i < width(); i++) {
            System.arraycopy(colourArray[i], 0, newColourArray[i], 0, seam[i]);
            System.arraycopy(colourArray[i], seam[i] + 1, newColourArray[i], seam[i],
                             height() - seam[i] - 1);
        }
        this.height--;
        this.colourArray = newColourArray;
        Picture newPicture = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newPicture.setRGB(i, j, newColourArray[i][j]);
            }
        }
        this.thePicture = newPicture;
        this.calculateEnergies();
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != height || width <= 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width) {
                throw new IllegalArgumentException();
            }
            if (i < seam.length - 1) {
                if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
        int[][] newColourArray = new int[width - 1][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i < seam[j]) {
                    newColourArray[i][j] = colourArray[i][j];
                }
                else if (i > seam[j]) {
                    newColourArray[i - 1][j] = colourArray[i][j];
                }
            }
        }
        this.width--;
        this.colourArray = newColourArray;
        Picture newPicture = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newPicture.setRGB(i, j, newColourArray[i][j]);
            }
        }
        this.thePicture = newPicture;
        this.calculateEnergies();
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        StdOut.print(Arrays.toString(sc.findVerticalSeam()));
        sc.removeVerticalSeam(sc.findVerticalSeam());
        StdOut.print(Arrays.toString(sc.findVerticalSeam()));
    }
}
