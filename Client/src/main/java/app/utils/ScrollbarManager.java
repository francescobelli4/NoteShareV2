package app.utils;

public class ScrollbarManager {

    private double[] heights;

    public void setHeights(double[] heights) {
        this.heights = heights;
    }

    public double[] getHeights() {
        return heights;
    }

    public int pageIndexForHeight(double height) {

        if (heights == null || heights.length == 0) return -1;
        if (heights.length == 1) return 0;
        if (height <= 0) return 0;

        int left = 0;
        int right = heights.length - 1;
        int mid = left + (right - left) / 2;

        if (height >= heights[right]) return right;

        while (left != mid && right != mid) {

            if (heights[mid] == height) return mid;

            if (height > heights[mid]) {
                left = mid;
                mid = left + (right - left) / 2;
                continue;
            }

            if (height < heights[mid]) {
                right = mid;
                mid = left + (right - left) / 2;
            }
        }

        return mid;
    }
}
