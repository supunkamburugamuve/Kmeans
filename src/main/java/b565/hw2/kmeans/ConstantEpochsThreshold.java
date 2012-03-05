package b565.hw2.kmeans;

/**
 * Implements a constant iteration K-Means. After a constant number of
 * iteration isDone return true.
 */
public class ConstantEpochsThreshold implements Threshold {
    /** Number of iterations */
    private final int itr ;
    /** current iteration number */
    private int curItr = 0;

    public ConstantEpochsThreshold(int itr) {
        this.itr = itr;
    }

    /**
     * Returns true if the iteration counter is greater than or
     * equal to the configure value.
     *
     * @param partitions set of partitions
     * @return true if the no of iterations >= iteration count
     */
    public boolean isDone(Partition []partitions) {
        curItr++;
        return curItr >= itr;
    }
}
