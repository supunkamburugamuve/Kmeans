package b565.hw2.kmeans;

/**
 * An interface for Distance calculations. Various distance functions should
 * implement this functionality.
 */
public interface Distance {
    /**
     * Calculate the distance between two vectors
     *
     * @param x1 vector1
     * @param x2 vector2
     * @return return the distance value
     */
    public double calculate(double [] x1, double [] x2);
}
