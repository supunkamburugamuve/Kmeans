package b565.hw2.kmeans;

import java.util.ArrayList;

/**
 * Represent the read data.
 */
public class Data {
    /** Data records */
    private ArrayList<Record> records;
    /** Incomplete records */
    private ArrayList<String> incompleteRecords;
    /** Duplicate records */
    private ArrayList<Record> duplicateRecords;

    public Data(ArrayList<Record> records, ArrayList<String> incompleteRecords) {
        this.records = records;
        this.incompleteRecords = incompleteRecords;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public ArrayList<String> getIncompleteRecords() {
        return incompleteRecords;
    }

    public ArrayList<Record> getDuplicateRecords() {
        return duplicateRecords;
    }

    public void setDuplicateRecords(ArrayList<Record> duplicateRecords) {
        this.duplicateRecords = duplicateRecords;
    }
}
