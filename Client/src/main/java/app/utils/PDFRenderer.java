package app.utils;

import stirling.software.jpdfium.PdfDocument;
import stirling.software.jpdfium.PdfPage;
import stirling.software.jpdfium.doc.PageBoxes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PDFRenderer {

    /** PDF files do not calculate width in pixels... This const px = pt * PT_TO_PX */
    private static final double PT_TO_PX = 96.0 / 72.0;

    /** Max number of images in loading process */
    private static final int MAX_LOADING_IMAGES = 7;


    /** An ExecutorService is basically a pool of n threads. Every thread executes a task, and it's blocked until the
     *  execution finishes.
     *  The oldest thread can be discarded if a new one is added.
     *  MAX_LOADING_IMAGES is the max number of threads that can work at the same time.
     *
     *  This executorService allows to load a small number of images and also manages "scrolling" and "fast scrolling"
     *  in the right way: it always prefers the pages that the user is actually watching.
     */
    ExecutorService executorService = new ThreadPoolExecutor(
            MAX_LOADING_IMAGES,
            MAX_LOADING_IMAGES,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(MAX_LOADING_IMAGES),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    PdfDocument pdfDocument;

    public PDFObject open(Path path) {
        List<PageInfo> pages = new ArrayList<>();

        pdfDocument = PdfDocument.open(path);

        for (int i = 0; i < pdfDocument.pageCount(); i++) {
            PdfPage page = pdfDocument.page(i);
            pages.add(new PageInfo(i, page.size().width() * PT_TO_PX, page.size().height() * PT_TO_PX));
        }

        return new PDFObject(pages);
    }

    public void loadRange(int visiblePageIndex) {
        // lancio executorservice da visiblePageIndex - 3 a visiblePageIndex + 3 che chiamano il model con l'output del render
    }
}
