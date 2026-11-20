package app.utils.dialogs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DialogComboBoxFieldTest {

    private final DialogComboBoxField testComboBox = new DialogComboBoxField("testId", new ArrayList<>(List.of("Option1", "Option2")));

    @Test
    void getOptions() {
        assertEquals(2, testComboBox.getOptions().size());
        assertEquals("Option1", testComboBox.getOptions().getFirst());
        assertEquals("Option2", testComboBox.getOptions().getLast());
    }
}