package b565.hw2.kmeans;

import junit.framework.TestCase;

public class EuclideanDistanceTest extends TestCase {
    public void testCalculate() {
        double x1[] = {1.0, 1.0, 1.0, 1.0};
        double x2[] = {1.0, 1.0, 1.0, 1.0};

        EuclideanDistance distance = new EuclideanDistance();
        assertEquals(distance.calculate(x1, x2), 0.0, .1);

        double y1[] = {2.0, 1.0, 1.0, 1.0};
        double y2[] = {1.0, 1.0, 1.0, 1.0};

        assertEquals(distance.calculate(y1, y2), 1, .1);

        double c1[] = {2.0, 1.0, 1.0, 1.0};
        double c2[] = {1.0, 1.0, 1.0};
        try {
            distance.calculate(c1, c2);
            assertEquals(true, false);
        } catch (IllegalArgumentException e) {
            assertEquals(true, true);
        }
    }
}
