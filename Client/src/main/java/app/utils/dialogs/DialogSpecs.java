package app.utils.dialogs;

import java.util.ArrayList;
import java.util.List;

public class DialogSpecs {

    private final List<DialogField> fields = new ArrayList<>();

    public void addTextField(String id, String prompt) {
        fields.add(new DialogTextField(id, prompt));
    }

    public void addComboBox(String id, List<String> options) {
        fields.add(new DialogComboBoxField(id, options));
    }

    public List<DialogField> getFields() {
        return fields;
    }
}
