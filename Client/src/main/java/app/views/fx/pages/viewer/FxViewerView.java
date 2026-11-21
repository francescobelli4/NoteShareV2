package app.views.fx.pages.viewer;

import app.entities.LibraryNoteEntity;
import app.models.notemodel.NoteModel;
import app.models.notemodel.NoteModelListener;
import app.models.usermodel.UserModel;
import app.models.usermodel.UserModelListener;
import app.utils.PDFObject;
import app.utils.PageInfo;
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

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox pagesBox;

    private LibraryNoteEntity noteEntity;
    private final NoteModel noteModel;

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

        subscribe(noteModel.addListener(this));
        //scrollPane.vvalueProperty().addListener(_ -> calculateNewVisiblePage());
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

        for (PageInfo pageInfo : pdfObject.pages()) {
            System.out.println(String.format("W: %s | H: %s", pageInfo.widthPx(), pageInfo.heightPx()));
            addPage(pageInfo);
        }
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
}
