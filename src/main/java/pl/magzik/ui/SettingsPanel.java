package pl.magzik.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SettingsPanel extends JPanel {

    JComboBox<String> languageComboBox, themeComboBox;

    JTextField startHotkey, stopHotkey, toggleHotkey;

    JButton saveButton, backButton, startCapture, stopCapture, toggleCapture;

    public SettingsPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 10, 10);

        JLabel header = new JLabel("LOC_SETTINGS");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16));

        add(header, c);
        c.gridy++;

        JLabel languageLabel = new JLabel("LOC_LANGUAGE");
        languageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(languageLabel, c);
        c.gridx = 1;

        languageComboBox = new JComboBox<>();

        add(languageComboBox, c);
        c.gridy++;
        c.gridx = 0;


        JLabel themeLabel = new JLabel("LOC_THEME");
        themeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(themeLabel, c);
        c.gridx = 1;

        themeComboBox = new JComboBox<>();
        add(themeComboBox, c);
        c.gridx = 0;
        c.gridy++;

        JLabel startHotkeyLabel = new JLabel("LOC_START_HOTKEY");
        startHotkeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(startHotkeyLabel, c);
        c.gridx = 1;

        JPanel startHotkeyPanel = new JPanel();
        startHotkeyPanel.setLayout(new BoxLayout(startHotkeyPanel, BoxLayout.X_AXIS));

        startHotkey = new JTextField();
        startHotkey.setEditable(false);
        startHotkey.setFocusable(false);
        startHotkeyPanel.add(startHotkey);
        startHotkeyPanel.add(Box.createHorizontalStrut(10));

        startCapture = new JButton("LOC_CAPTURE");
        startHotkeyPanel.add(startCapture);

        add(startHotkeyPanel, c);
        c.gridy++;
        c.gridx = 0;

        JLabel stopHotkeyLabel = new JLabel("LOC_STOP_HOTKEY");
        stopHotkeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(stopHotkeyLabel, c);
        c.gridx = 1;

        JPanel stopHotkeyPanel = new JPanel();
        stopHotkeyPanel.setLayout(new BoxLayout(stopHotkeyPanel, BoxLayout.X_AXIS));

        stopHotkey = new JTextField();
        stopHotkey.setEditable(false);
        stopHotkey.setFocusable(false);
        stopHotkeyPanel.add(stopHotkey);
        stopHotkeyPanel.add(Box.createHorizontalStrut(10));

        stopCapture = new JButton("LOC_CAPTURE");
        stopHotkeyPanel.add(stopCapture);

        add(stopHotkeyPanel, c);
        c.gridy++;
        c.gridx = 0;

        JLabel toggleHotkeyLabel = new JLabel("LOC_TOGGLE_HOTKEY");
        toggleHotkeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(toggleHotkeyLabel, c);
        c.gridx = 1;

        JPanel toggleHotkeyPanel = new JPanel();
        toggleHotkeyPanel.setLayout(new BoxLayout(toggleHotkeyPanel, BoxLayout.X_AXIS));

        toggleHotkey = new JTextField();
        toggleHotkey.setEditable(false);
        toggleHotkey.setFocusable(false);
        toggleHotkeyPanel.add(toggleHotkey);
        toggleHotkeyPanel.add(Box.createHorizontalStrut(10));

        toggleCapture = new JButton("LOC_CAPTURE");
        toggleHotkeyPanel.add(toggleCapture);

        add(toggleHotkeyPanel, c);
        c.gridy++;
        c.gridx = 0;

        c.weighty = 1;

        add(new JPanel(), c);
        c.gridy++;
        c.weighty = 0;
        c.anchor = GridBagConstraints.SOUTH;

        saveButton = new JButton("LOC_SAVE");
        saveButton.setFont(saveButton.getFont().deriveFont(Font.PLAIN, 16));
        add(saveButton, c);
        c.gridx++;

        backButton = new JButton("LOC_BACK");
        backButton.setFont(backButton.getFont().deriveFont(Font.PLAIN, 16));
        add(backButton, c);


        startCapture.addActionListener(_ -> {
           startCapture.addKeyListener(new KeyAdapter() {
               @Override
               public void keyReleased(KeyEvent e) {
                   int keyCode = e.getKeyCode();
                   startHotkey.setText(KeyEvent.getKeyText(keyCode));

                   startCapture.removeKeyListener(this);
               }
           });

           startCapture.requestFocusInWindow();
        });

        stopCapture.addActionListener(_ -> {
            stopCapture.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    stopHotkey.setText(KeyEvent.getKeyText(keyCode));

                    stopCapture.removeKeyListener(this);
                }
            });

            stopCapture.requestFocusInWindow();
        });

        toggleCapture.addActionListener(_ -> {
            toggleCapture.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    toggleHotkey.setText(KeyEvent.getKeyText(keyCode));

                    toggleCapture.removeKeyListener(this);
                }
            });

            toggleCapture.requestFocusInWindow();
        });
    }

    public JComboBox<String> getLanguageComboBox() {
        return languageComboBox;
    }

    public JComboBox<String> getThemeComboBox() {
        return themeComboBox;
    }

    public JTextField getStartHotkey() {
        return startHotkey;
    }

    public JTextField getStopHotkey() {
        return stopHotkey;
    }

    public JTextField getToggleHotkey() {
        return toggleHotkey;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
