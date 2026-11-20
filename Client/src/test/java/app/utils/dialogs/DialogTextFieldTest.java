package app.utils.dialogs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogTextFieldTest {

    private final DialogTextField testTextField = new DialogTextField("testId", "Prompt");

    @Test
    void getPrompt() {
        assertEquals("Prompt", testTextField.getPrompt());
    }
}