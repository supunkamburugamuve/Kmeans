package b565.hw2.kmeans;

/**
 * Calculate the Manhattan distance.
 */
public class ManhattanDistance implements Distance{
    public double calculate(double[] x1, double[] x2) {
        if (x1.length != x2.length) {
            throw new IllegalArgumentException(
                    "Vectors should be of equal length: x1 length = "
                            + x1.length + " x2 length = " + x2.length);
        }
        double distance = 0;
        for (int i = 0; i < x1.length; i++) {
            distance += Math.abs(x1[i] - x2[i]);
        }
        return distance;
    }
}
