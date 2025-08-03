package game.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PositionTest {

    @Test
    public void distanceTest() {
        Position p0 = new Position(0, 0);
        Position p1 = new Position(3, 4);
        double dist = p0.distance(p1);
        // expected=5.0, actual=dist, allowable delta
        assertEquals(5.0, dist, 1e-6);
    }

    @Test
    public void distanceIsSymmetric() {
        Position a = new Position(2, 7);
        Position b = new Position(-1, 3);
        double d1 = a.distance(b);
        double d2 = b.distance(a);
        assertEquals(d1, d2, 1e-6);
    }

    @Test
    public void distanceToSelfIsZero() {
        Position p = new Position(5, -3);
        assertEquals(0.0, p.distance(p), 0.0);
    }

    @Test
    public void settersAndGettersWork() {
        Position p = new Position(0, 0);
        p.setX(12);
        p.setY(-8);
        assertEquals(12, p.getX());
        assertEquals(-8, p.getY());
    }

    @Test
    public void negativeCoordinatesDistance() {
        Position p1 = new Position(-2, -3);
        Position p2 = new Position(-5, -7);
        // Δx = 3, Δy = 4 → distance = 5
        assertEquals(5.0, p1.distance(p2), 1e-6);
    }

    @Test
    public void symmetryAcrossMultiplePairs() {
        Position[] pts = {
                new Position(1,1),
                new Position(4,5),
                new Position(-2,3),
                new Position(10,-4)
        };
        for (int i = 0; i < pts.length; i++) {
            for (int j = 0; j < pts.length; j++) {
                double d1 = pts[i].distance(pts[j]);
                double d2 = pts[j].distance(pts[i]);
                assertEquals("Distance symmetry",
                        d1, d2, 1e-6);
            }
        }
    }
}
