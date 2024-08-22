package pl.magzik;

import pl.magzik.ui.ClickerPanel;
import pl.magzik.ui.LocationPicker;
import pl.magzik.ui.SettingsPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Controller {
    private final View view;
    private final Model model;
    private final ResourceBundle resourceBundle;

    private SwingWorker<Void, Void> clickerWorker;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        Locale loc = Locale.forLanguageTag(model.getLanguage());

        this.resourceBundle = ResourceBundle.getBundle("localization", loc); // todo tmp
        view.getPanels().forEach(this::translateComponents);

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
                .filter(k -> resourceBundle.getString(k).equals(key))
                .findFirst()
                .orElse(key);
    }

    private void translateComponents(Container container) {
        view.setTitle(translate(view.getTitle()));

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
        // Clicker Panel
        ClickerPanel cp = view.getClickerPanel();

        cp.getMillisecondsTextField().setText("1");

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

        cp.getButtonComboBox().addActionListener(_ -> {
            model.setMouseButton(
                Model.MouseButton.get(
                    reverseTranslate((String) cp.getButtonComboBox().getSelectedItem())
                )
            );
        });

        cp.getTypeComboBox().addActionListener(_ -> {
            model.setClickType(
                Model.ClickType.get(
                    reverseTranslate((String) cp.getTypeComboBox().getSelectedItem())
                )
            );
        });

        // Initialize Buttons
        cp.getStartButton().addActionListener(_ -> {
            resetClickerWorker();
            cp.getStopButton().setEnabled(true);

            clickerWorker.execute();

            cp.getStartButton().setEnabled(false);
        });

        cp.getStopButton().setEnabled(false);
        cp.getStopButton().addActionListener(_ -> {
            model.stopClicker();
        });

        cp.getToggleButton().addActionListener(_ -> {
            if (cp.getStartButton().isEnabled()) {
                cp.getStartButton().doClick();
            } else {
                cp.getStopButton().doClick();
            }
        });

        cp.getPickLocationButton().addActionListener(_ -> {
            view.setExtendedState(Frame.ICONIFIED);

            LocationPicker lp = new LocationPicker();
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

        cp.getSettingsButton().addActionListener(_ -> {
            view.changeScene(1);
        });

        // Settings Panel
        SettingsPanel sp = view.getSettingsPanel();

        // Initialize
        List<String> langs = new ArrayList<>();
        langs.add("en-US");
        langs.add("pl-PL");

        for (String lang : langs) {
            String key = "LOC_LANG_" + lang;
            sp.getLanguageComboBox().addItem(
                translate(key)
            );
        }

        String language = "LOC_LANG_" + model.getLanguage();
        sp.getLanguageComboBox().setSelectedItem(
            translate(language)
        );

        List<String> themes = new ArrayList<>();
        themes.add("DARK");
        themes.add("LIGHT");

        for (String theme : themes) {
            String key = "LOC_THEME_" + theme;
            sp.getThemeComboBox().addItem(
                translate(key)
            );
        }

        String theme = "LOC_THEME_" + model.getTheme();
        sp.getThemeComboBox().setSelectedItem(
            translate(theme)
        );

        // TODO JAVA NATIVE HOOK?
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (KeyStroke.getKeyStrokeForEvent(e).equals(KeyStroke.getKeyStroke(model.getStartHotkey()))) {
                    cp.getStartButton().doClick();
                    return true;
                } else if (KeyStroke.getKeyStrokeForEvent(e).equals(KeyStroke.getKeyStroke(model.getStopHotkey()))) {
                    cp.getStopButton().doClick();
                    return true;
                } else if (KeyStroke.getKeyStrokeForEvent(e).equals(KeyStroke.getKeyStroke(model.getToggleHotkey()))) {
                    cp.getToggleButton().doClick();
                    return true;
                }
            }
            return false;
        });

        sp.getStartHotkey().setText(model.getStartHotkey());
        cp.getStartButton().setText(
            String.format("%s (%s)", cp.getStartButton().getText(), model.getStartHotkey())
        );

        sp.getStopHotkey().setText(model.getStopHotkey());
        cp.getStopButton().setText(
            String.format("%s (%s)", cp.getStopButton().getText(), model.getStopHotkey())
        );

        sp.getToggleHotkey().setText(model.getToggleHotkey());
        cp.getToggleButton().setText(
            String.format("%s (%s)", cp.getToggleButton().getText(), model.getToggleHotkey())
        );

        // Buttons
        sp.getBackButton().addActionListener(_ -> view.changeScene(0));

        // TODO SAVING
        sp.getSaveButton().addActionListener(_ -> {
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
                throw new RuntimeException(e); // todo
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

        });

    }

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

                int times;
                if (cp.getInfiniteTimes().isSelected())
                    times = -1;
                else if (cp.getTimesRadioButton().isSelected())
                    times = Integer.parseInt(cp.getTimes());
                else
                    throw new NullPointerException("Click Repeat settings is null");

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
}
