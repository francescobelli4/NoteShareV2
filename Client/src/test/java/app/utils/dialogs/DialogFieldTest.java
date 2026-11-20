package app.utils.dialogs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DialogFieldTest {

    private final DialogField dialogField = new DialogField("testId");

    @Test
    void getId() {
        assertEquals("testId", dialogField.getId());
    }
}