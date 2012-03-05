package b565.hw2.kmeans;

/**
 * Calculate the Euclidean Squared Distance.
 */
public class EuclideanSquaredDistance implements Distance {
    public double calculate(double[] x1, double[] x2) {
        if (x1.length != x2.length) {
            throw new IllegalArgumentException(
                    "Vectors should be of equal length: x1 length = "
                            + x1.length + " x2 length = " + x2.length);
        }

        double val = 0;
        double temp;
        for (int i = 0; i < x1.length; i++) {
            temp = x1[i] - x2[i];
            temp *= temp;
            val += temp;
        }

        return val;
    }
}
