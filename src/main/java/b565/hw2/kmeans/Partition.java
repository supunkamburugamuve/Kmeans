package b565.hw2.kmeans;

import java.util.ArrayList;

/**
 * Represents a calculated partition. A partition contains the centroid and
 * the data points.
 */
public class Partition {
    /* centroid of the partition */
    private double []centroid;
    /* records belonging to this partition */
    private ArrayList<Record> records = new ArrayList<Record>();

    public Partition(double[] centroid) {
        this.centroid = centroid;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    /**
     * Calculate the new centroid from the data points. Mean of the
     * data points belonging to this partition is the new centroid.
     */
    public void calculateCentroid() {
        if (records.size() == 0) {
            throw new IllegalArgumentException("The calculated class should be non empty");
        }

        int size  = records.get(0).getData().length;
        for (Record record : records) {
            double []r = record.getData();
            for (int j = 0; j < size; j++) {
                centroid[j] += r[j];
            }
        }
        for (int i = 0; i < size; i++) {
            centroid[i] /= records.size();
        }
    }

    /**
     * Clears the data records
     */
    public void reset() {
        // now clear the records
        records.clear();
    }
}
