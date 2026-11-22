package app.utils;

import app.appcontrollers.NotesController;
import stirling.software.jpdfium.PdfDocument;
import stirling.software.jpdfium.PdfPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class PDFRenderer {

    private static final int RENDER_DPI = 96;
    /** PDF files do not calculate width in pixels... This const px = pt * PT_TO_PX */
    private static final double PT_TO_PX = RENDER_DPI / 72.0;

    /** Max number of images in loading process */
    private static final int MAX_LOADING_IMAGES = 7;

    /** Max number of cached images */
    private static final int MAX_CACHED_IMAGES = 10;


    /** An ExecutorService is basically a pool of n threads. Every thread executes a task, and it's blocked until the
     *  execution finishes.
     *  The oldest thread can be discarded if a new one is added.
     *  MAX_LOADING_IMAGES is the max number of threads that can work at the same time.
     *
     *  This executorService allows to load a small number of images and also manages "scrolling" and "fast scrolling"
     *  in the right way: it always prefers the pages that the user is actually watching.
     */
    private final ExecutorService executorService = new ThreadPoolExecutor(
            MAX_LOADING_IMAGES,
            MAX_LOADING_IMAGES,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(MAX_LOADING_IMAGES),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );


    private final Set<Integer> inFlight = ConcurrentHashMap.newKeySet();

    /** <a href="https://www.baeldung.com/java-linked-hashmap">LINK</a>
     * A LinkedHashmap with accessOrder is perfect to represent the loaded pages and to remember (and remove) the pages
     * that are no longer needed.
     *
     * This provides a simple way to clean up the memory: searching and deleting elements is done in O(1).
     * It removes an Image only if there are more than MAX_CACHED_IMAGES loaded and the distance between an image and
     * the actual visible page is greater than UNUSED_PAGE_THRESHOLD.
     */
    private final LinkedHashMap<Integer, byte[]> loadedPages = new LinkedHashMap<>(MAX_CACHED_IMAGES, (float) 3 /4, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, byte[]> eldest) {

            if (size() > MAX_CACHED_IMAGES) {
                notesController.pageDeleted(eldest.getKey());
                return true;
            }

            return false;
        }
    };

    private Path path;
    private final List<PdfDocument> workersDocuments = new ArrayList<>();

    private PdfDocument pdfDocument;
    private final NotesController notesController;

    public PDFRenderer(NotesController notesController) {
        this.notesController = notesController;
    }


    public PDFObject open(Path path) {
        this.path = path;
        List<PageInfo> pages = new ArrayList<>();

        pdfDocument = PdfDocument.open(path);

        for (int i = 0; i < pdfDocument.pageCount(); i++) {

            try (PdfPage page = pdfDocument.page(i)) {
                pages.add(new PageInfo(i, page.size().width() * PT_TO_PX, page.size().height() * PT_TO_PX));
            }
        }

        return new PDFObject(pages);
    }

    public void renderRange(int visiblePage) {

        for (int i = -MAX_LOADING_IMAGES/2; i <= MAX_LOADING_IMAGES/2; i++) {

            int pageId = visiblePage + i;

            if (pageId < 0 || pageId >= pdfDocument.pageCount() || loadedPages.get(pageId) != null || inFlight.contains(pageId)) {
                continue;
            }

            inFlight.add(pageId);

            executorService.submit(() -> {

                PdfDocument workerDoc = PdfDocument.open(path);
                workersDocuments.add(workerDoc);

                try {
                    byte[] image = renderPage(workerDoc, pageId);

                    loadedPages.put(pageId, image);
                    notesController.pageRendered(pageId, image);
                } catch (IOException _) {
                    // No-op, blank page
                } finally {
                    inFlight.remove(pageId);
                    workerDoc.close();
                }
            });
        }
    }

    private byte[] renderPage(PdfDocument workerDoc, int index) throws IOException {

        try (PdfPage page = workerDoc.page(index)) {

            BufferedImage img = page.renderAt(RENDER_DPI).toBufferedImage();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "PNG", out);

            return out.toByteArray();
        }
    }

    public void destroy() {
        executorService.shutdownNow();

        // In case pdfium is still working when the executorService gets shut down... We can wait for the workers to
        // finish or just catch the exception and interrupt the thread
        try {
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }

        workersDocuments.forEach(PdfDocument::close);
        workersDocuments.clear();

        if (pdfDocument != null) pdfDocument.close();
        loadedPages.clear();
        inFlight.clear();
    }
}
