package app.utils.dialogs;

import java.util.List;

public class DialogComboBoxField extends DialogField {
    private final List<String> options;

    public DialogComboBoxField(String id, List<String> options) {
        super(id);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }
}
