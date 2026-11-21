package app.utils;

import java.util.List;

public record PDFObject(List<PageInfo> pages) {
    public int getPageCount() {
        return pages.size();
    }

    public PageInfo getPage(int index) {
        return pages.get(index);
    }
}


