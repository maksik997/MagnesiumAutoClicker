package pl.magzik;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import javax.swing.*;
import java.io.IOException;

/**
 * Main class... Nothing more... Nothing less.
 * */
public class Main {
    public static void main(String[] args) {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.setProperty("apple.awt.application.name", "Magnesium Auto Clicker");
            System.setProperty("apple.awt.application.appearance", "system");
        }
        setupUiManager();

        // Model initialization
        Model model = null;
        try {
            model = new Model();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Couldn't initialize application. Please try again.\nError: " + e.getMessage(),
                "Error:",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        // Theme setup.
        if (model.getTheme().equals("DARK")) FlatDarculaLaf.setup();
        else FlatLightLaf.setup();


        // View and Controller initialization.
        View view = new View();
        Controller controller = new Controller(view, model);

        // JNativeHook initialization.
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(controller);
        } catch (NativeHookException e) {
            JOptionPane.showMessageDialog(
                view,
                controller.translate("LOC_JNATIVEHOOK_ERROR_MESSAGE"),
                controller.translate("LOC_JNATIVEHOOK_ERROR_TITLE"),
                JOptionPane.ERROR_MESSAGE
            );

            System.exit(1);
        }

        // GUI creation
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /**
     * FlatLaf settings
     * */
    private static void setupUiManager() {
        UIManager.put("TextComponent.arc", 5);
        UIManager.put("Component.focusWidth", 0);
    }
}