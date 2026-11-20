package app.utils.dialogs;

public class DialogTextField extends DialogField {

    private final String prompt;

    public DialogTextField(String id, String prompt) {
        super(id);
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
