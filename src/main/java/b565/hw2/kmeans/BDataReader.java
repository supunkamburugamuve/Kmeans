package b565.hw2.kmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is a data reader for reading the Breast Cancer Wisconsin Data.
 */
public class BDataReader {
    /** file name */
    private String fileName = "breast-cancer-wisconsin.data";
    /** number of attributes */
    private int attributes = 9;
    /** separator */
    private final String separator = ",";
    /** index of Attributes to be used */
    private int[] attributesUsed = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    public BDataReader(String fileName) {
        this.fileName = fileName;
    }

    public void setAttributesUsed(int[] attributesUsed) {
        this.attributesUsed = attributesUsed;
        this.attributes = attributesUsed.length;
    }

    /**
     * Read the data from the given file
     * @return the data from the file
     */
    public Data read() {
        File file = new File(fileName);

        BufferedReader in = null;
        ArrayList<Record> records = new ArrayList<Record>();
        ArrayList<String> incompleteRecords = new ArrayList<String>();
        if (file.exists()) {
            try {
                in = new BufferedReader(new FileReader(file));

                String line  = in.readLine();
                while (line != null) {
                    readRecord(records, incompleteRecords, line);
                    line = in.readLine();
                }

            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                System.out.println("Error occurred while reading file: "
                        + file + " " + e.getMessage());
            }
        } else {
            return null;
        }

        return new Data(records, incompleteRecords);
    }

    /**
     * Read a single records and store it in records
     * @param records records list
     * @param incompleteRecords incomplete record list
     * @param line string to extract the record fom
     */
    private void readRecord(ArrayList<Record> records,
                            ArrayList<String> incompleteRecords,
                            String line) {
        String [] strRecord = line.split(separator);
        double[] record = new double[attributes];
        for (int i = 0; i < attributesUsed.length; i++) {
            try {
                record[i] = Double.parseDouble(strRecord[attributesUsed[i]].trim());
            } catch (NumberFormatException e) {
                incompleteRecords.add(line);
                return;
            }
        }
        Record r = new Record(line, record);
        try {
            r.setCorrectClassifier(Integer.parseInt(strRecord[strRecord.length - 1]));
        } catch (NumberFormatException e) {
            incompleteRecords.add(line);
            return;
        }
        records.add(r);
    }
}
