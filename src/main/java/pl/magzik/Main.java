package pl.magzik;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", "Magnesium Auto Clicker");
        System.setProperty("apple.awt.application.appearance", "system");
        setupUiManager();
        FlatDarculaLaf.setup();

        View view = new View();

        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    private static void setupUiManager() {
        UIManager.put("TextComponent.arc", 5);
        UIManager.put("Component.focusWidth", 0);
    }
}