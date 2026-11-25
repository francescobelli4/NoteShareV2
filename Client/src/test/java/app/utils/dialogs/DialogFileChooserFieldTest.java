package app.utils.dialogs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogFileChooserFieldTest {

    DialogFileChooserField dialogFileChooserField = new DialogFileChooserField("testId");

    @Test
    void getId() {
        assertEquals("testId", dialogFileChooserField.getId());
    }
}