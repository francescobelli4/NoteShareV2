package app.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScrollbarManagerTest {

    private final double[] heights = {0, 10, 20, 30, 40, 50, 60, 70, 80};
    private final ScrollbarManager scrollbarManager = new ScrollbarManager();

    @Test
    void setHeights() {
        scrollbarManager.setHeights(heights);
        assertEquals(heights, scrollbarManager.getHeights());
    }

    @Test
    void pageIndexForHeight() {
        scrollbarManager.setHeights(heights);
        assertEquals(2, scrollbarManager.pageIndexForHeight(25));
    }

    @Test
    void pageIndexForHeight_Fail() {

        assertEquals(-1, scrollbarManager.pageIndexForHeight(25));
        scrollbarManager.setHeights(new double[]{0});
        assertEquals(0, scrollbarManager.pageIndexForHeight(25));
        assertEquals(0, scrollbarManager.pageIndexForHeight(-25));
        scrollbarManager.setHeights(heights);
        assertEquals(8, scrollbarManager.pageIndexForHeight(1000));
    }
}