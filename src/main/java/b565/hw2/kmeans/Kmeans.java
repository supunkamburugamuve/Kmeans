package b565.hw2.kmeans;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.util.ArrayList;

/**
 * Implementation of the Kmeans algorithm. We assume a clean data set is given to the algorithm.
 */
public class Kmeans {
    /** centroid initialization */
    public static final int FOGGY_CENTROIDS = 0;
    public static final int RANDOM_CENTROIDS = 1;
    /** Constants for stopping criteria */
    public static final int STOP_EPOCHS = 0;
    public static final int STOP_SAME_CENTROIDS = 1;

    public static final int SPLIT_SIMPLE = 0;
    public static final int SPLIT_VARIANCE = 1;

    /* This class is used to calculate the distance between two data points */
    private Distance distanceAlgorithm = new EuclideanDistance();
    /* This class is used to calculate the stopping criteria */
    private Threshold threshold = new ConstantEpochsThreshold(20);
    /* Initial centroids */
    private ArrayList<double []> centroids = new ArrayList<double[]>();
    /* Represent the actual records read from the file */
    private ArrayList<Record> records;

    /* Initial centroids method */
    private int initCentroids = FOGGY_CENTROIDS;

    private double comparisonMargin = .05;

    /* number of clusters */
    private int k;
    /** size of the data set */
    private int size;
    /** Split method to be used */
    private int splitMethod = SPLIT_VARIANCE;

    public Kmeans(ArrayList<Record> records, int k) {
        if (k <= 1) {
            throw new IllegalArgumentException("K should be greater than 1");
        }
        if (records.size() <= k) {
            throw new IllegalArgumentException("Number of records should be greater than or equal to k");
        }
        this.records = records;
        this.k = k;
    }

    public void setInitCentroids(int initCentroids) {
        this.initCentroids = initCentroids;
    }

    public void setThreshold(Threshold threshold) {
        this.threshold = threshold;
    }

    public void setDistanceAlgorithm(Distance distanceAlgorithm) {
        this.distanceAlgorithm = distanceAlgorithm;
    }

    public void setComparisonMargin(double comparisonMargin) {
        this.comparisonMargin = comparisonMargin;
    }

    public void setSplitMethod(int splitMethod) {
        this.splitMethod = splitMethod;
    }

    public void init() {
        size = records.size();
        if (initCentroids == FOGGY_CENTROIDS) {
            foggyCentroids();
        } else if (initCentroids == RANDOM_CENTROIDS) {
            randomCentroids();
        }
    }

    public Partition[] run() {
        Partition []partitions = new Partition[k];

        for (int i = 0; i < partitions.length; i++) {
            partitions[i] = new Partition(centroids.get(i));
        }

        double currentDistance = 0;
        double distance = 0;
        int currentCentroid = 0;
        while (!threshold.isDone(partitions)) {
            // calculateCentroid the centroids
            for (Partition partition : partitions) {
                partition.reset();
            }

            for (int i = 0; i < size; i++) {
                Record record = records.get(i);
                // find the closest centroid for this record
                currentDistance = 0;
                for (int j = 0; j < centroids.size(); j++) {
                    double [] centroid = centroids.get(j);
                    distance = distanceAlgorithm.calculate(record.getData(), centroid);
                    if (Math.abs(currentDistance - distance) < comparisonMargin) {
                        if (partitions[j].getRecords().size() < partitions[currentCentroid].getRecords().size()) {
                            currentCentroid = j;
                            currentDistance = distance;
                        }
                    } else if (currentDistance < distance){
                        currentCentroid = j;
                        currentDistance = distance;
                    }
                }

                partitions[currentCentroid].addRecord(record);
            }

            // calculateCentroid the centroids
            for (Partition partition : partitions) {
                if (partition.getRecords().isEmpty()) {
                    int p = getPartitionToSplit(partitions);
                    split(partition, partitions[p]);
                }
                partition.calculateCentroid();
            }
        }

        return partitions;
    }

    /**
     * First allocate the data records to K partitions and calculate the mean of these partitions.
     * The mean is taken as the initial centroids.
     */
    private void randomCentroids() {
        Partition [] partitions = new Partition[k];
        for (int i = 0; i < partitions.length; i++) {
            partitions[i] = new Partition(new double[records.get(0).getData().length]);
        }
        // pick a random partition and add the record to it
        for (int i = 0; i < records.size(); i++) {
            int random = (int) (Math.random() * (k - 1));
            partitions[random].addRecord(new Record("", records.get(i).getData()));
        }

        // make sure no partition is empty
        for (int i = 0; i < partitions.length; i++) {
            if (partitions[i].getRecords().isEmpty()) {
                int k = getPartitionToSplit(partitions);
                split(partitions[i], partitions[k]);
            }
        }

        // calculate the initial centroids
        for (int i = 0; i < partitions.length; i++) {
            partitions[i].calculateCentroid();

            double []c = new double[partitions[i].getCentroid().length];
            System.arraycopy(partitions[i].getCentroid(), 0, c, 0,
                    partitions[i].getCentroid().length);
            centroids.add(c);
        }
    }

    private boolean isUnique(int[] picks, int i) {
        for (int j = 0; j < i; j++) {
            if (picks[i] == picks[j]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Pick K records randomly from the record set and assign them as partitions.
     */
    private void foggyCentroids() {
        // initialize the centroids
        // pick k random data points as centroids
        int []picks = new int[k];
        for (int i = 0; i < k; i++) {
            // we have to make sure they are unique
            while (true) {
                picks[i] = (int)(Math.random() * (size - 1));
                if (isUnique(picks, i)) {
                    break;
                }
            }
        }

        for (int i = 0; i < k; i++) {
            Record record = records.get(picks[i]);
            double []c = new double[record.getData().length];
            System.arraycopy(record.getData(), 0, c, 0, record.getData().length);
            centroids.add(c);
        }
    }

    private int getPartitionToSplit(Partition []partitions) {
        if (splitMethod == SPLIT_SIMPLE) {
            int size = 0;
            int partition = 0;
            for (int i = 0; i < partitions.length; i++) {
                if (size < partitions[i].getRecords().size()) {
                    size = partitions[i].getRecords().size();
                    partition = i;
                }
            }
            return partition;
        } else if (splitMethod == SPLIT_VARIANCE) {
            return largestVariance(partitions);
        }
        return 0;
    }

    private void split(Partition emptyPartition, Partition splitPartition) {
        ArrayList<Record> splitRecords = splitPartition.getRecords();
        if (splitMethod == SPLIT_SIMPLE) {
            for (int i = 0; i < splitRecords.size() / 2; i++) {
                Record record = splitRecords.remove(0);
                emptyPartition.addRecord(record);
            }
        } else if (splitMethod == SPLIT_VARIANCE) {
            Record r = furthestFromMean(splitPartition);
            splitPartition.getRecords().remove(r);
            emptyPartition.addRecord(r);
        }
    }

    public Record furthestFromMean(Partition partition) {
        ArrayList<Record> records = partition.getRecords();
        double []mean = calculateMean(records);
        double distance = 0, curDistance = 0;
        int rec = 0;
        for (int i = 0; i < records.size(); i++) {
            distance = distanceAlgorithm.calculate(mean, records.get(i).getData());
            if (distance > curDistance) {
                curDistance = distance;
                rec = i;
            }
        }

        return records.get(rec);
    }

    public int largestVariance(Partition []partitions) {
        double currentVariance = 0, v = 0;
        int partition = 0;
        for (int i = 0; i < partitions.length; i++) {
            v = calculateVariance(partitions[i]);
            if (currentVariance < v) {
                partition = i;
                currentVariance = v;
            }
        }

        return partition;
    }

    public double calculateVariance(Partition partition) {
        ArrayList<Record> records = partition.getRecords();

        if (records.size() == 0) return 0;

        double []mean = calculateMean(records);
        double []variance = new double[records.get(0).getData().length];
        for (Record r : records) {
            double [] values = r.getData();
            for (int i = 0; i < values.length; i++) {
                variance[i] += (mean[i] - values[i]) * (mean[i] - values[i]);
            }
        }

        double sum = 0;
        for (int i = 0; i < variance.length; i++) {
            variance[i] = variance[i]/ records.size();
            sum += variance[i];
        }
        return sum;
    }

    public double [] calculateMean(ArrayList<Record> records) {
        double []means = new double[records.get(0).getData().length];
        for (Record r : records) {
            double [] values = r.getData();
            for (int i = 0; i < values.length; i++) {
                means[i] += values[i];
            }
        }

        for (int i = 0; i < means.length; i++) {
            means[i] = means[i]/ records.size();
        }

        return means;
    }
}
