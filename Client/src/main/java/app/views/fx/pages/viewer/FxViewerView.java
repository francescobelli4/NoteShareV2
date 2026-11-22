package app.views.fx.pages.viewer;

import app.entities.LibraryNoteEntity;
import app.models.notemodel.NoteModel;
import app.models.notemodel.NoteModelListener;
import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.utils.PDFObject;
import app.utils.PageInfo;
import app.utils.ScrollbarManager;
import app.utils.Utils;
import app.views.fx.FxView;
import app.views.fx.graphiccontrollers.home.FxHomeViewController;
import app.views.fx.graphiccontrollers.viewer.FxViewerController;
import app.views.fx.pages.home.FxUserBoxView;
import app.views.fx.pages.home.explorer.FxExplorerView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.function.Supplier;

public class FxViewerView extends FxView<FxViewerController> implements NoteModelListener {

    private static final double SPACING = 16;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox pagesBox;

    private LibraryNoteEntity noteEntity;
    private final NoteModel noteModel;
    private PDFObject pdfObject;
    private final ScrollbarManager scrollbarManager = new ScrollbarManager();
    private int mainPageIndex = 0;

    public FxViewerView(FxViewerController controller, NoteModel noteModel) {
        super(controller);
        this.noteModel = noteModel;
    }

    @Override
    protected String getFxmlPath() {
        return "/fxml/viewer/Viewer.fxml";
    }

    @Override
    protected void onLoaded() {

        pagesBox.setSpacing(SPACING);
        subscribe(noteModel.addListener(this));
    }

    public void setNote(LibraryNoteEntity noteEntity) {

        this.noteEntity = noteEntity;
        controller.openNote(noteEntity);
    }

    @Override
    protected void onClose() {
        // Still not done :D
    }

    @Override
    public void onPdfOpened(PDFObject pdfObject) {

        this.pdfObject = pdfObject;

        prepareScrollbarManager(pdfObject);

        for (PageInfo pageInfo : pdfObject.pages()) {
            addPage(pageInfo);
        }

        scrollPane.vvalueProperty().addListener(_ -> onScroll());
    }

    private void addPage(PageInfo pageInfo) {

        StackPane page = new StackPane();
        page.setPrefSize(pageInfo.widthPx(), pageInfo.heightPx());
        page.setMinSize(pageInfo.widthPx(), pageInfo.heightPx());
        page.setMaxSize(pageInfo.widthPx(), pageInfo.heightPx());
        page.setStyle(
                "-fx-background-color: white;"
        );

        pagesBox.getChildren().add(page);
    }

    private void prepareScrollbarManager(PDFObject pdfObject) {
        double[] heights = new double[pdfObject.getPageCount()];
        heights[0] = 0;

        for (int i = 1; i < pdfObject.getPageCount(); i++) {
            heights[i] = heights[i-1] + SPACING + pdfObject.getPage(i-1).heightPx();
        }

        scrollbarManager.setHeights(heights);
    }

    private void onScroll() {
        // "Window height"
        double viewportHeight = scrollPane.getViewportBounds().getHeight();
        // The max amount of pixels that can be scrolled down with that "window height"
        double maxScrollablePixels = Math.max(0, pagesBox.getHeight() - viewportHeight);
        // The current amount of scrolled down pixels referring to the center of the window
        double currentY = (scrollPane.getVvalue() * maxScrollablePixels) + viewportHeight / 2.0;


        double min = scrollbarManager.getHeights()[mainPageIndex];
        double max = mainPageIndex + 1 >= pdfObject.getPageCount() ? Double.POSITIVE_INFINITY : scrollbarManager.getHeights()[mainPageIndex + 1];

        if (Utils.inInterval(currentY, min, max)) return;

        mainPageIndex = scrollbarManager.pageIndexForHeight(currentY);
        controller.onNewPageChanged(mainPageIndex);
    }

}
