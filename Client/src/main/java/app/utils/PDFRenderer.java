package app.utils;

import app.appcontrollers.NotesController;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

/**
 * This class is not testable at the moment.
 */
public class PDFRenderer {

    private static final int RENDER_DPI = 96;
    /** PDF files do not calculate width in pixels... This const px = pt * PT_TO_PX */
    private static final double PT_TO_PX = RENDER_DPI / 72.0;

    /** Max number of images in loading process */
    private static final int MAX_LOADING_IMAGES = 7;

    /** Max number of cached images */
    private static final int MAX_CACHED_IMAGES = 10;

    /**
     * This ExecutorService should only create MAX_LOADING_IMAGES worker threads as daemon threads.
     * When a page needs to be rendered, executorService.submit(...) assigns the new page to a free worker thread.
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_LOADING_IMAGES, threadRunnable -> {
        Thread t = new Thread(threadRunnable);
        t.setDaemon(true);
        return t;
    });


    /** These pages are currently being rendered by the workers, any other requests on these pages should be ignored */
    private final Set<Integer> inFlight = ConcurrentHashMap.newKeySet();

    /** <a href="https://www.baeldung.com/java-linked-hashmap">LINK</a>
     * A LinkedHashmap with accessOrder is perfect to represent the loaded pages and to remember (and remove) the pages
     * that are no longer needed.
     * This provides a simple way to clean up the memory: searching and deleting elements is done in O(1).
     * It removes an Image only if there are more than MAX_CACHED_IMAGES loaded.
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

    /**
     * PDFBox needs a pdfbox.PDFRenderer object associated with every PDDocument to render the pages. WorkerData
     * records are passed to each worker thread when it's looking for a new page to render.
     */
    private record WorkerData(PDDocument workerDoc, org.apache.pdfbox.rendering.PDFRenderer workerDocRenderer) {}

    /**
     * This BlockingQueue allows every worker thread to obtain a WorkerData object that is not currently used by any
     * other worker thread. workerDocs.take() blocks the calling thread until a WorkerData object is free.
     */
    private final BlockingQueue<WorkerData> workersDocs = new LinkedBlockingQueue<>();

    private final NotesController notesController;
    private int pageCount = 0;

    public PDFRenderer(NotesController notesController) {
        this.notesController = notesController;
    }

    /**
     * This function should prepare PDFRenderer to render the selected note and return the PDFObject used by the view
     * to prepare a slot for every rendered image.
     * @param path the PDF file path
     * @return a PDFObject that contains info about the pages dimensions
     * @throws IOException in case of errors when opening the PDF file
     */
    public PDFObject open(Path path) throws IOException {
        List<PageInfo> pages = new ArrayList<>();

        try (PDDocument pdfDocument = Loader.loadPDF(path.toFile())) {

            pageCount = pdfDocument.getNumberOfPages();

            for (int i = 0; i < pageCount; i++) {
                PDPage page = pdfDocument.getPage(i);
                PDRectangle box = page.getCropBox();

                pages.add(new PageInfo(i, box.getWidth() * PT_TO_PX, box.getHeight() * PT_TO_PX));
            }
        }

        for (int i = 0; i < MAX_LOADING_IMAGES; i++) {
            PDDocument document = Loader.loadPDF(path.toFile());
            workersDocs.add(new WorkerData(document, new org.apache.pdfbox.rendering.PDFRenderer(document)));
        }

        return new PDFObject(pages);
    }

    /**
     * This function should actually handle the rendering of a range of pages, basing on visiblePage index.
     * It should also ignore the rendering for pages that are already rendered or that are currently being rendered from
     * a worker thread.
     * @param visiblePage the page that the user is currently seeing
     */
    public void renderRange(int visiblePage) {

        for (int i = -MAX_LOADING_IMAGES/2; i <= MAX_LOADING_IMAGES/2; i++) {

            int pageId = visiblePage + i;

            if (pageId < 0 || pageId >= pageCount || loadedPages.get(pageId) != null || inFlight.contains(pageId)) {
                continue;
            }

            inFlight.add(pageId);

            executorService.submit(() -> {

                WorkerData thisWorkerData = null;
                
                try {
                    thisWorkerData = workersDocs.take();
                    byte[] image = renderPage(thisWorkerData, pageId);

                    loadedPages.put(pageId, image);
                    notesController.pageRendered(pageId, image);
                } catch (InterruptedException _) {
                    // When this worker is interrupted, simply interrupt the thread anyway
                    Thread.currentThread().interrupt();
                } catch (IOException _) {
                    // No-op, blank page
                } finally {
                    inFlight.remove(pageId);
                    if (thisWorkerData != null) workersDocs.add(thisWorkerData);
                }
            });
        }
    }

    private byte[] renderPage(WorkerData workerData, int index) throws IOException {

        BufferedImage img = workerData.workerDocRenderer().renderImageWithDPI(index, RENDER_DPI, ImageType.RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", out);

        return out.toByteArray();
    }

    /**
     * This function should shut down the PDFRenderer by killing all the worker threads and closing all the workerDocs.
     */
    public void destroy() {
        executorService.shutdownNow();

        // In case pdfbox is still working when the executorService gets shut down... We can wait for the workers to
        // finish or just catch the exception and interrupt the thread
        try {
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }

        workersDocs.forEach(workerData -> {
            try {
                workerData.workerDoc().close();
            } catch (IOException _) {
                // No-op, the doc is closed anyway...
            }
        });

        workersDocs.clear();
        loadedPages.clear();
        inFlight.clear();
    }
}
