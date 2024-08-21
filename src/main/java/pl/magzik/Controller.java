package pl.magzik;

import pl.magzik.ui.ClickerPanel;
import pl.magzik.ui.LocationPicker;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {
    private final View view;
    private final Model model;
    private final ResourceBundle resourceBundle;

    private SwingWorker<Void, Void> clickerWorker;

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

        // Initialize Buttons
        cp.getStartButton().addActionListener(_ -> {
            resetClickerWorker();
            cp.getStopButton().setEnabled(true);

            clickerWorker.execute();

            cp.getStartButton().setEnabled(false); // todo
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

        cp.getMillisecondsTextField().setText("1");

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
            }
        };
    }
}
