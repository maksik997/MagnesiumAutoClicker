package pl.magzik;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import pl.magzik.ui.ClickerPanel;
import pl.magzik.ui.LocationPicker;
import pl.magzik.ui.SettingsPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Controller of the app. Handles all user interaction and all view-model interactions.
 * */
public class Controller implements NativeKeyListener {

    private static final List<String> languages = new ArrayList<>(),
                                      themes = new ArrayList<>();
    static {
        languages.add("en-US");
        languages.add("pl-PL");
        themes.add("DARK");
        themes.add("LIGHT");
    }

    private final View view;
    private final Model model;
    private final ResourceBundle resourceBundle;

    private SwingWorker<Void, Void> clickerWorker;

    public Controller(View view, Model model) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(model);

        this.view = view;
        this.model = model;

        // Picking Locale from user's settings.
        Locale loc = Locale.forLanguageTag(model.getLanguage());
        this.resourceBundle = ResourceBundle.getBundle("localization", loc);

        // View elements translation.
        this.view.setTitle(translate(view.getTitle()));
        this.view.getPanels().forEach(this::translateComponents);

        init();
    }

    /**
     * Translates given key into specified in resourceBundle value
     * @param key
     *          Localization key to be translated
     * @return Value of given key or key if there is no translation available
     */
    public String translate(String key) {
        Objects.requireNonNull(key);

        if (resourceBundle.containsKey(key)) return resourceBundle.getString(key);
        return key;
    }

    /**
     * Translates given value into translation key.
     * @param value
     *            Value to be translated.
     * @return Localization key or value if no key is available.
     * */
    public String reverseTranslate(String value) {
        Objects.requireNonNull(value);

        return resourceBundle.keySet().stream()
                .filter(k -> resourceBundle.getString(k).equals(value))
                .findFirst()
                .orElse(value);
    }

    /**
     * Translates swing components using ResourceBundle. This method works recursively.
     * @param container
     *                 {@link Container} to translate
     * */
    private void translateComponents(Container container) {
        Objects.requireNonNull(container);

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

    /**
     * Initializes Swing components, links ActionListeners etc.
     * */
    private void init() {
        initClickerPanel();
        initSettingsPanel();
    }

    /**
     * Initializes Swing components, links ActionListeners etc. in Clicker Panel
     * */
    private void initClickerPanel() {
        // Clicker Panel
        ClickerPanel cp = view.getClickerPanel();

        // Initialize
        // Set one millisecond interval.
        cp.getMillisecondsTextField().setText("1");

        // MouseButton ComboBox
        for (Model.MouseButton mb : Model.MouseButton.values()) {
            cp.getButtonComboBox().addItem(
                translate(mb.toString())
            );
        }
        cp.getButtonComboBox().setSelectedItem(
            translate(model.getMouseButton().toString())
        );

        // ClickType ComboBox
        for (Model.ClickType ct : Model.ClickType.values()) {
            cp.getTypeComboBox().addItem(
                translate(ct.toString())
            );
        }
        cp.getTypeComboBox().setSelectedItem(
            translate(model.getClickType().toString())
        );

        // Buttons shortcut text
        cp.getStartButton().setText(
            String.format("%s (%s)", cp.getStartButton().getText(), model.getStartHotkey())
        );
        cp.getStopButton().setText(
            String.format("%s (%s)", cp.getStopButton().getText(), model.getStopHotkey())
        );
        cp.getToggleButton().setText(
            String.format("%s (%s)", cp.getToggleButton().getText(), model.getToggleHotkey())
        );

        cp.getStopButton().setEnabled(false);

        // Action Listeners
        // Button ComboBox
        cp.getButtonComboBox().addActionListener(_ -> model.setMouseButton(
            Model.MouseButton.get(
                reverseTranslate((String) cp.getButtonComboBox().getSelectedItem())
            )
        ));

        // ClickType ComboBox
        cp.getTypeComboBox().addActionListener(_ -> model.setClickType(
            Model.ClickType.get(
                reverseTranslate((String) cp.getTypeComboBox().getSelectedItem())
            )
        ));

        // StartButton Listener
        cp.getStartButton().addActionListener(_ -> {
            resetClickerWorker();

            cp.getStopButton().setEnabled(true);
            cp.getStartButton().setEnabled(false);

            clickerWorker.execute();
        });

        // StopButton Listener
        cp.getStopButton().addActionListener(_ -> model.stopClicker());

        // ToggleButton Listener
        cp.getToggleButton().addActionListener(_ -> {
            if (!model.isClickerRunning()) cp.getStartButton().doClick();
            else cp.getStopButton().doClick();
        });

        // PickLocationButton Listener
        cp.getPickLocationButton().addActionListener(_ -> {
            view.setExtendedState(Frame.ICONIFIED);

            LocationPicker lp = new LocationPicker();

            // This way I can take X, Y coordinates without bigger problem.
            // After click window is disposed. See: LocationPicker
            lp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cp.getXLocationTextField().setText(Integer.toString(lp.getXPoint()));
                    cp.getYLocationTextField().setText(Integer.toString(lp.getYPoint()));
                    view.setExtendedState(Frame.NORMAL);
                }
            });

            SwingUtilities.invokeLater(() -> lp.setVisible(true));
        });

        // SettingsButton Listener
        cp.getSettingsButton().addActionListener(_ -> {
            resetSettingsPanel();
            view.changeScene(View.SETTINGS_SCENE);
        });
    }

    /**
     * Initializes Swing components, links ActionListeners etc. in Settings Panel
     * */
    private void initSettingsPanel() {
        // Settings Panel
        SettingsPanel sp = view.getSettingsPanel();

        // Initialize
        // Languages ComboBox
        for (String lang : languages) {
            String key = "LOC_LANG_" + lang;
            sp.getLanguageComboBox().addItem(
                translate(key)
            );
        }

        // Themes ComboBox
        for (String theme : themes) {
            String key = "LOC_THEME_" + theme;
            sp.getThemeComboBox().addItem(
                translate(key)
            );
        }

        resetSettingsPanel();

        // Listeners
        // BackButton Listener
        sp.getBackButton().addActionListener(_ -> view.changeScene(View.CLICKER_SCENE));

        // SaveButton Listener
        sp.getSaveButton().addActionListener(_ -> {
            ClickerPanel cp = view.getClickerPanel();

            // Take data
            String l = reverseTranslate(
                    Objects.requireNonNull(sp.getLanguageComboBox().getSelectedItem()).toString()
                ),
                t = reverseTranslate(
                    Objects.requireNonNull(sp.getThemeComboBox().getSelectedItem()).toString()
                ),
                startHotkey = reverseTranslate(sp.getStartHotkey().getText()),
                stopHotkey = reverseTranslate(sp.getStopHotkey().getText()),
                toggleHotkey = reverseTranslate(sp.getToggleHotkey().getText());

            String[] splitL = l.split("_");
            l = splitL[splitL.length - 1];

            String[] splitT = t.split("_");
            t = splitT[splitT.length - 1];

            if (!model.getLanguage().equals(l) || !model.getTheme().equals(t)) {
                JOptionPane.showMessageDialog(
                    view,
                    translate("LOC_RESTART_NEEDED_MESSAGE"),
                    translate("LOC_RESTART_NEEDED_MESSAGE_TITLE"),
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

            try {
                model.saveConfig(l, t, startHotkey, stopHotkey, toggleHotkey);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    view,
                    translate("LOC_SAVE_ERROR_MESSAGE"),
                    translate("LOC_SAVE_ERROR_TITLE"),
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            cp.getStartButton().setText(
                    String.format("%s (%s)", cp.getStartButton().getText().split(" ")[0], model.getStartHotkey())
            );
            cp.getStopButton().setText(
                    String.format("%s (%s)", cp.getStopButton().getText().split(" ")[0], model.getStopHotkey())
            );
            cp.getToggleButton().setText(
                    String.format("%s (%s)", cp.getToggleButton().getText().split(" ")[0], model.getToggleHotkey())
            );

            resetSettingsPanel();
        });
    }

    /**
     * Resets {@link SwingWorker} for Clicker Task. Can be used for initialization of worker.
     * <p>
     * {@code Note: Easiest solution is to create new instance of SwingWorker}.
     * */
    private void resetClickerWorker() {
        ClickerPanel cp = view.getClickerPanel();

        clickerWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int interval =
                    Integer.parseInt(cp.getMilliseconds()) +
                    Integer.parseInt(cp.getSeconds()) * 1000 +
                    Integer.parseInt(cp.getMinutes()) * 60_000 +
                    Integer.parseInt(cp.getHours()) * 3_600_000;

                if (interval == 0) interval++; // Safety reasons

                int times = cp.getInfiniteTimes().isSelected() ? -1 : Integer.parseInt(cp.getTimes());

                String mouseButton = reverseTranslate((String) cp.getButtonComboBox().getSelectedItem());
                String clickType = reverseTranslate((String) cp.getTypeComboBox().getSelectedItem());

                int x, y;
                if (cp.getCustomLocationRadioButton().isSelected()) {
                    x = Integer.parseInt(cp.getXLocation());
                    y = Integer.parseInt(cp.getYLocation());
                    model.startClicker(interval, times, mouseButton, clickType, x, y);
                } else {
                    model.startClicker(interval, times, mouseButton, clickType);
                }

                return null;
            }

            @Override
            protected void done() {
                super.done();

                JOptionPane.showMessageDialog(
                    view,
                    String.format(translate("LOC_MESSAGE_1"), model.getClickCount()),
                    translate("LOC_TITLE"),
                    JOptionPane.INFORMATION_MESSAGE
                );

                cp.getStartButton().setEnabled(true);
                cp.getStopButton().setEnabled(false);

                model.setClickCount(0);
            }
        };
    }

    /**
     * Resets GUI elements in Settings Panel. In case user left settings and didn't save.
     * */
    private void resetSettingsPanel() {
        SettingsPanel sp = view.getSettingsPanel();

        String language = "LOC_LANG_" + model.getLanguage();
        sp.getLanguageComboBox().setSelectedItem(
            translate(language)
        );

        String theme = "LOC_THEME_" + model.getTheme();
        sp.getThemeComboBox().setSelectedItem(
            translate(theme)
        );

        sp.getStartHotkey().setText(model.getStartHotkey());
        sp.getStopHotkey().setText(model.getStopHotkey());
        sp.getToggleHotkey().setText(model.getToggleHotkey());
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        ClickerPanel cp = view.getClickerPanel();

        if (view.getCurrentScene() != View.CLICKER_SCENE) return;

        if (e.getKeyCode() == model.getStartHotkeyKeyEvent()) {
            cp.getStartButton().doClick();

        } else if (e.getKeyCode() == model.getStopHotkeyKeyEvent()) {
            cp.getStopButton().doClick();

        } else if (e.getKeyCode() == model.getToggleHotkeyKeyEvent()) {
            cp.getToggleButton().doClick();
        }
    }
}
