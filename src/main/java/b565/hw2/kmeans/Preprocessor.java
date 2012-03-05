package b565.hw2.kmeans;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Pre-process the data depending on the given conditions.
 */
public class Preprocessor {
    /**
     * Remove the duplicates
     * @param d remove the duplicates
     */
    public void process(Data d) {
        ArrayList<Record> newRecords = new ArrayList<Record>();
        ArrayList<Record> dupRecords = new ArrayList<Record>();
        ArrayList<Record> records = d.getRecords();
        // remove the duplicate records
        Iterator<Record> it = records.iterator();
        while (it.hasNext()) {
            Record r = it.next();
            boolean recFound = false;
            for (int j = 0; j < newRecords.size(); j++) {
                if (newRecords.get(j).getActual().trim().equals(r.getActual().trim())) {
                    recFound = true;
                    it.remove();
                    dupRecords.add(r);
                    break;
                }
            }

            if (!recFound) {
                newRecords.add(r);
            }
        }
        d.setDuplicateRecords(dupRecords);
    }
}
