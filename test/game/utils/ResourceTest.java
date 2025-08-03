package game.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {

    @Test
    public void shouldClampAboveCapacity() {
        // capacity = 100, starting amount = 50
        Resource r = new Resource(50, 100);
        // add a huge amount → should cap at 100
        r.addAmount(1000);
        assertEquals(100, r.getAmount());
    }

    @Test
    public void shouldClampBelowZero() {
        Resource r = new Resource(50, 100);
        // subtract more than you have → should bottom out at 0
        r.addAmount(-500);
        assertEquals(0, r.getAmount());
    }

    @Test
    public void testExactBoundaryAdds() {
        Resource r = new Resource(20, 100);
        r.addAmount(80);
        assertEquals(100, r.getAmount());
        r.addAmount(-100);
        assertEquals(0, r.getAmount());
    }

    @Test
    public void testSetAmountClamping() {
        Resource r = new Resource(10, 50);
        r.setAmount(60);
        // currently your setAmount allows >capacity; assert what you expect:
        assertEquals("If you want to clamp here, adjust setAmount",
                60, r.getAmount());
        r.setAmount(-10);
        assertEquals("Negative via setter? decide clamp or allow",
                -10, r.getAmount());
    }

    @Test
    public void testAddCapacityAndSubsequentAdd() {
        Resource r = new Resource(50, 100);
        r.addCapacity(50);
        // capacity is now 150
        r.addAmount(100);
        assertEquals(150, r.getAmount());
    }
}