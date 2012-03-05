package b565.hw2.kmeans;

/**
 * A single data records read from the file.
 */
public class Record {
    /** Actual record as in the file */
    private String actual;
    /** Double array as data */
    private double []data;
    /** The true classifier */
    private int correctClassifier = 0;

    public Record(String actual, double[] data) {
        this.actual = actual;
        this.data = data;
    }

    public void setCorrectClassifier(int correctClassifier) {
        this.correctClassifier = correctClassifier;
    }

    public int getCorrectClassifier() {
        return correctClassifier;
    }

    /**
     * Get the actual string read from the file
     * @return the actual record as in the file
     */
    public String getActual() {
        return actual;
    }

    /**
     * Get the data as a double array
     * @return data as a double array
     */
    public double[] getData() {
        return data;
    }
}
