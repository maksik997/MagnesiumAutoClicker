package pl.magzik;

import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {
    private final View view;
    private final Model model;
    private final ResourceBundle resourceBundle;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        this.resourceBundle = ResourceBundle.getBundle("localization", Locale.getDefault()); // todo tmp
    }

    private void init() {

    }
}
