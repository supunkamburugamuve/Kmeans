package b565.hw2.kmeans;

/**
 * Implementing a threshold where K-Means stop after it is getting the
 * same set of centroids.
 */
public class SameCentroidThreshold implements Threshold {
    /** Keep track of the last partition */
    private Partition [] lastPartitions = null;
    /** Comparison margin for two double values */
    private double comparisonMargin = .05;

    public SameCentroidThreshold(double comparisonMargin) {
        this.comparisonMargin = comparisonMargin;
    }

    public boolean isDone(Partition[] partitions) {
        if (lastPartitions.length != partitions.length) {
            throw new IllegalStateException("Partition lengths should be equal, " +
                    "now lastPartition = " + lastPartitions.length +
                    " newPartition = " + partitions.length);
        }

        for (int i = 0; i < partitions.length; i++) {
            double [] lastCentroid = lastPartitions[i].getCentroid();
            double [] newCentroid = partitions[i].getCentroid();

            for (int j = 0; j < newCentroid.length; j++) {
                if (Math.abs(lastCentroid[j] - newCentroid[j]) > comparisonMargin) {
                    lastPartitions = partitions;
                    return false;
                }
            }
        }
        lastPartitions = partitions;
        return true;
    }
}
