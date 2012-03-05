package b565.hw2.kmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A Class to run the K-Mean algorithm according to given conditions.
 */
public class Application {
    public static final int DISTANCE_EUCLIDEAN = 0;
    public static final int DISTANCE_EUCLIDEAN_SQUARED = 1;
    public static final int DISTANCE_MANHATTAN = 2;

    /** File name to be used */
    private String fileName = "breast-cancer-wisconsin.data";
    /** file to save the incomplete data */
    private String incompleteDataFile = "incomplete.data";
    /* file to save the clusters */
    private String clusterFile = "cluster";

    /** Centroid initialization algorithm */
    private int centroidInitialization = Kmeans.RANDOM_CENTROIDS;
    /** Splitting method to be used */
    private int splittingMethod = 0;
    /** the number of clusters */
    private int k = 2;
    /** Method used to check the stop condition */
    private int stopMethod = Kmeans.STOP_EPOCHS;
    /** if the ConstantEpochs method is used number of epochs */
    private int stopEpochs = 10;
    /** if the same centroid method is used the comparison margin for doubles */
    private double comparisonMargin = 0.05;
    /** the distance algorithm to be used */
    private int distanceAlgorithm = DISTANCE_EUCLIDEAN;
    /** attributes to be considers */
    private int []attributesUsed = null;
    /** make true if we want to do cross validation */
    private boolean crossFoldValidation = false;
    /** remove the duplicate data points */
    private boolean removeDuplicates = true;
    /** Comparison for ties */
    private double comparisonMarginTies = .005;
    /** split method to be used */
    private int splitMethod = Kmeans.SPLIT_SIMPLE;

    public static void main(String[] args) {
        Application app = new Application();
        try {
            app.readConsole();
        } catch (IOException e) {
            System.out.println("Error occurred while reading input data.....");
        }

        app.run();
    }

    /**
     * Function for reading the input values from the console.
     *
     * @throws IOException if an exception occurs
     */
    private void readConsole() throws IOException {
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        String input;
        do {
            System.out.print("File Name (breast-cancer-wisconsin.data):");
            input = in.readLine();
            if (input == null || input.trim().equals("") || new File(input).exists()) {
                break;
            } else {
                System.out.println("File doesn't exists");
            }
        } while (true);

        if (input != null && !input.trim().equals("")) {
            fileName = input;
        }

        do {
            System.out.print("Remove duplicates (1) (0 false, 1 true):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    int i = Integer.parseInt(input);
                    if (i == 1) {
                        removeDuplicates = true;
                        break;
                    } else if (i == 0) {
                        removeDuplicates = false;
                    } else {
                        System.out.println("Invalid number, 0 or 1 expected");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);

        do {
            System.out.print("K (2):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    k = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);


        do {
            System.out.print("Centroid initialization (0) (0 Foggy, 1 Random):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    int i = Integer.parseInt(input);
                    if (i == 0 || i == 1) {
                        centroidInitialization = i;
                        break;
                    } else {
                        System.out.println("Invalid number, 0 or 1 expected");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);

        do {
            System.out.print("Distance Algorithm (0) (0 Euclidean, 1 Euclidean Squared, 2 Manhattan):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    int i = Integer.parseInt(input);
                    if (i == 0 || i == 1 || i == 2) {
                        distanceAlgorithm = i;
                        break;
                    } else {
                        System.out.println("Invalid number, 0, 1 or 2 expected");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);

        do {
            System.out.print("Comparison error, ties (.005):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    comparisonMarginTies = Double.parseDouble(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);

        do {
            System.out.print("Split (0) (0 Largest partition, 1 Largest variance):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    int i = Integer.parseInt(input);
                    if (i == 0 || i == 1) {
                        splitMethod = i;
                        break;
                    } else {
                        System.out.println("Invalid number, 0 or 1 expected");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);

        do {
            System.out.print("Threshold (0) (0 Constant Epochs, 1 Same Centroids):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    int i = Integer.parseInt(input);
                    if (i == 0 || i == 1) {
                        stopMethod = i;
                        break;
                    } else {
                        System.out.println("Invalid number, 0 or 1 expected");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);

        if (stopMethod == 0) {
            do {
                System.out.print("Epochs (10):");
                input = in.readLine();
                if (input == null || input.trim().equals("")) {
                    break;
                } else {
                    try {
                        stopEpochs = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number");
                    }
                }
            } while (true);
        } else if (stopMethod == 1) {
            do {
                System.out.print("Comparison error (.05):");
                input = in.readLine();
                if (input == null || input.trim().equals("")) {
                    break;
                } else {
                    try {
                        comparisonMargin = Double.parseDouble(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number");
                    }
                }
            } while (true);
        }

        do {
            System.out.print("Attributes used (1, 2, 3, 4, 5, 6, 7, 8, 9):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    String []attrs = input.split(",");
                    if (attrs.length == 0) {
                        System.out.println("Invalid input");
                        continue;
                    }
                    int [] attributes = new int[attrs.length];
                    for (int i = 0; i < attrs.length; i++) {
                        attributes[i] = Integer.parseInt(attrs[i].trim());
                    }
                    attributesUsed = attributes;
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);

        do {
            System.out.print("V-fold cross validation (0) (0 false, 1 true):");
            input = in.readLine();
            if (input == null || input.trim().equals("")) {
                break;
            } else {
                try {
                    int i = Integer.parseInt(input);
                    if (i == 1) {
                        crossFoldValidation = true;
                        break;
                    } else if (i == 0) {
                        crossFoldValidation = false;
                    } else {
                        System.out.println("Invalid number, 0 or 1 expected");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
            }
        } while (true);
    }

    /**
     * Run the K-Means algorithm, with the configured settings
     */
    private void run() {
        BDataReader reader = new BDataReader(fileName);

        if (attributesUsed != null) {
            reader.setAttributesUsed(attributesUsed);
        }

        // read the data from the file
        Data d = reader.read();
        System.out.println("\n\n\nResults");
        System.out.println("Total number of records: " +
                (d.getRecords().size() + d.getIncompleteRecords().size()));
        System.out.println("Complete Records: " + d.getRecords().size());
        System.out.println("Incomplete Records: " + d.getIncompleteRecords().size());

        if (removeDuplicates) {
            Preprocessor p = new Preprocessor();
            p.process(d);
            System.out.println("Duplicate Records: " + d.getDuplicateRecords().size());
            System.out.println("Removing duplicate records.......\n");
        } else {
            System.out.println("Not Removing duplicate records.......\n");
        }

        // printIncompleteData(d.getIncompleteRecords());

        Partition[] partitions;
        if (!crossFoldValidation) {
            partitions = runKmeans(d.getRecords());
            System.out.println("Total number of centroids: " + partitions.length + "\n");
            for (Partition partition : partitions) {
                printCentroid(partition);
                System.out.println("\n");
            }
            System.out.println("PPV: " + calculatePPV(partitions));
            savePartitions(partitions);
        } else {
            int size =  d.getRecords().size() / 10;
            double weightedPPV = 0;
            for (int i = 0; i < 10; i++) {
                ArrayList<Record> trainingSet = new ArrayList<Record>();
                ArrayList<Record> testSet = new ArrayList<Record>();
                for (int j = i * size ; j < (i + 1) * size; j++) {
                    testSet.add(d.getRecords().get(j));
                }
                trainingSet.addAll(d.getRecords());
                trainingSet.removeAll(testSet);

                partitions = runKmeans(trainingSet);
                ArrayList<double []> cs = new ArrayList<double[]>();
                for (int k = 0; k < partitions.length; k++) {
                    cs.add(partitions[k].getCentroid());
                }
                double ppv = vFoldCrossValidation(testSet, cs);
                weightedPPV += ppv;
                System.out.println("PPV for d" + (i + 1) + " " + ppv);
            }

            System.out.println("Weighted ppv: " + weightedPPV);
        }
    }

    /**
     * Run K-Means using the specific records
     * @param records records containing the values
     * @return the partitions
     */
    private Partition[] runKmeans(ArrayList<Record> records) {
        Kmeans kmeans = new Kmeans(records, k);

        // set the centroid initialization method
        kmeans.setInitCentroids(centroidInitialization);

        // set the stopping criteria
        if (stopMethod == Kmeans.STOP_EPOCHS) {
            kmeans.setThreshold(new ConstantEpochsThreshold(stopEpochs));
        } else if (stopEpochs == Kmeans.STOP_SAME_CENTROIDS) {
            kmeans.setThreshold(new SameCentroidThreshold(comparisonMargin));
        }

        // set the distance metric
        if (distanceAlgorithm == DISTANCE_EUCLIDEAN) {
            kmeans.setDistanceAlgorithm(new EuclideanDistance());
        } else if (distanceAlgorithm == DISTANCE_EUCLIDEAN_SQUARED) {
            kmeans.setDistanceAlgorithm(new EuclideanSquaredDistance());
        } else if (distanceAlgorithm == DISTANCE_MANHATTAN) {
            kmeans.setDistanceAlgorithm(new ManhattanDistance());
        }

        kmeans.setComparisonMargin(comparisonMarginTies);

        kmeans.setSplitMethod(splitMethod);

        // initialize the algorithm
        kmeans.init();
        // run the algorithm
        return kmeans.run();
    }

    private static void printIncompleteData(ArrayList<String> records) {
        System.out.println("Incomplete data:");
        for (int i = 0; i < records.size(); i++) {
            System.out.println(records.get(i));
        }
        System.out.println("");
    }

    private static void printPartition(Partition p) {
        printCentroid(p);
        System.out.print("");

        System.out.println("Data:");
        ArrayList<Record> records = p.getRecords();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            for (int j = 0; j < record.getData().length; j++) {
                System.out.print(record.getData()[j]);
                if (j != record.getData().length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println("");
        }
    }

    private static void printCentroid(Partition p) {
        int []values = getMajorityClass(p);
        System.out.printf("Centroid: \nRecords %d\nClass 2 = %d, Class 4 = %d\n",
                p.getRecords().size(), values[0], values[1]);
        double[] centroid = p.getCentroid();
        for (int j = 0; j < centroid.length; j++) {
            System.out.printf("%.5f", centroid[j]);
            if (j != centroid.length - 1) {
                System.out.print(",");
            }
        }
    }

    private static void printPartitionOnlyRecords(Partition p) {
        printCentroid(p);
        System.out.print("");

        System.out.println("Data:");
        ArrayList<Record> records = p.getRecords();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            System.out.println(record.getActual());
        }
    }

    private static double calculatePPV(Partition []partitions) {
        double ppv = 0;
        int tp = 0;
        int fp = 0;
        for (Partition p : partitions) {
            int []pps = getMajorityClass(p);
            tp += pps[0] > pps[1] ? pps[0] : pps[1];
            fp += pps[0] < pps[1] ? pps[0] : pps[1];
        }
        ppv = ((double) (tp)) /(fp + tp);
        return ppv;
    }

    private static int [] getMajorityClass(Partition partition) {
        int twos = 0, fours = 0;
        for (int i = 0; i < partition.getRecords().size(); i++) {
            if (partition.getRecords().get(i).getCorrectClassifier() == 2) {
                twos++;
            } else if (partition.getRecords().get(i).getCorrectClassifier() == 4) {
                fours++;
            }
        }

        return new int[]{twos, fours};
    }

    /**
     * Do vFold Cross Validation for the records
     * @param records records to be used
     * @param centroids centroids used in the calculation
     * @return PPV value
     */
    private double vFoldCrossValidation(ArrayList<Record> records, ArrayList<double []> centroids) {
        Distance distanceAlgo = null;
        if (distanceAlgorithm == DISTANCE_EUCLIDEAN) {
            distanceAlgo = new EuclideanDistance();
        } else if (distanceAlgorithm == DISTANCE_EUCLIDEAN_SQUARED) {
            distanceAlgo = new EuclideanSquaredDistance();
        } else if (distanceAlgorithm == DISTANCE_MANHATTAN) {
            distanceAlgo = new ManhattanDistance();
        } else {
            return -1;
        }

        Partition []partitions = new Partition[centroids.size()];
        for (int i = 0; i < partitions.length; i++) {
            partitions[i] = new Partition(centroids.get(i));
        }

        double currentDistance = 0, distance = 0;
        int currentCentroid = 0;
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            // find the closest centroid for this record
            currentDistance = 0;
            for (int j = 0; j < centroids.size(); j++) {
                double[] centroid = centroids.get(j);
                distance = distanceAlgo.calculate(record.getData(), centroid);
                if (currentDistance < distance) {
                    currentCentroid = j;
                    currentDistance = distance;
                }
            }

            partitions[currentCentroid].addRecord(record);
        }

        return calculatePPV(partitions);
    }

    private void savePartitions(Partition []partitions) {
        for (int i = 0; i < partitions.length; i++) {
            File f = new File(clusterFile + i + ".part");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                for (Record r : partitions[i].getRecords()) {
                    bw.write(r.getActual());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                System.out.println("error writing file");
            }
        }
    }
}
