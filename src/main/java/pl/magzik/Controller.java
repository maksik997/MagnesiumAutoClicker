package pl.magzik;

import pl.magzik.ui.ClickerPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
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
        translateComponents(view);
        init();
    }

    private String translate(String key) {
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        }
        return key;
    }

    private String reverseTranslate(String key) {
        return resourceBundle.keySet().stream()
                .filter(k -> k.equals(key))
                .findFirst()
                .orElse(key);
    }

    private void translateComponents(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JFrame frame) {
                frame.setTitle(
                    translate(frame.getTitle())
                );
            } else if (component instanceof JLabel label) {
                label.setText(
                    translate(label.getText())
                );
            } else if (component instanceof JButton button) {
                button.setText(
                    translate(button.getText())
                );
            } else if (component instanceof JRadioButton radioButton) {
                radioButton.setText(
                    translate(radioButton.getText())
                );
            }

            if (component instanceof JComponent panel) {
                if (panel.getBorder() instanceof TitledBorder border) {
                    border.setTitle(
                        translate(border.getTitle())
                    );
                } else if (panel.getBorder() instanceof CompoundBorder border) {
                    if (border.getOutsideBorder() instanceof TitledBorder titledBorder) {
                        titledBorder.setTitle(
                            translate(titledBorder.getTitle())
                        );
                    }
                    if (border.getInsideBorder() instanceof TitledBorder titledBorder) {
                        titledBorder.setTitle(
                            translate(titledBorder.getTitle())
                        );
                    }
                }
            }

            if (component instanceof Container c) translateComponents(c);

        }
    }

    private void init() {
        ClickerPanel cp = view.getClickerPanel();
        // Initialize Combo boxes
        for (Model.MouseButton mb : Model.MouseButton.values()) {
            cp.getButtonComboBox().addItem(
                translate(mb.toString())
            );
        }
        cp.getButtonComboBox().setSelectedItem(
            translate(model.getMouseButton().toString())
        );

        for (Model.ClickType ct : Model.ClickType.values()) {
            cp.getTypeComboBox().addItem(
                translate(ct.toString())
            );
        }
        cp.getTypeComboBox().setSelectedItem(
          translate(model.getClickType().toString())
        );
    }
}
