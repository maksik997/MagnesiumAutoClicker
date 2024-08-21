package pl.magzik;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("apple.awt.application.name", "Magnesium Auto Clicker");
        System.setProperty("apple.awt.application.appearance", "system");
        setupUiManager();
//        FlatDarculaLaf.setup();

        Model model = new Model();

        switch (model.getTheme()) {
            case "dark" -> FlatDarculaLaf.setup();
            case "light" -> FlatLightLaf.setup();
            default -> FlatLightLaf.setup();
        }

        View view = new View();
        new Controller(view, model);

        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    private static void setupUiManager() {
        UIManager.put("TextComponent.arc", 5);
        UIManager.put("Component.focusWidth", 0);
    }
}