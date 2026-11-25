package app.utils.dialogs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DialogSpecsTest {

    private DialogSpecs dialogSpecs;

    @BeforeEach
    void setUp() {
        dialogSpecs = new DialogSpecs();
    }

    @Test
    void addTextField() {
        dialogSpecs.addTextField("testId", "Prompt");

        assertEquals(1, dialogSpecs.getFields().size());
        assertInstanceOf(DialogTextField.class, dialogSpecs.getFields().getFirst());
        assertEquals("testId", dialogSpecs.getFields().getFirst().getId());
    }

    @Test
    void addComboBox() {
        List<String> testOptions = new ArrayList<>();
        testOptions.add("Option1");
        testOptions.add("Option2");

        dialogSpecs.addComboBox("testId", testOptions);

        assertEquals(1, dialogSpecs.getFields().size());
        assertInstanceOf(DialogComboBoxField.class, dialogSpecs.getFields().getFirst());
        assertEquals("testId", dialogSpecs.getFields().getFirst().getId());
    }

    @Test
    void addFileChooser() {

        dialogSpecs.addFileChooser("testId");

        assertEquals(1, dialogSpecs.getFields().size());
        assertInstanceOf(DialogFileChooserField.class, dialogSpecs.getFields().getFirst());
        assertEquals("testId", dialogSpecs.getFields().getFirst().getId());
    }
}