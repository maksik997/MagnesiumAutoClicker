package pl.magzik.ui;

import pl.magzik.ui.utils.IntegerDocumentFilter;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class ClickerPanel extends JPanel {

    private final JButton settingsButton;

    private final JTextField hoursTextField, minutesTextField, secondsTextField, milisecondsTextField;

    public ClickerPanel() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, Color.GRAY),
            new EmptyBorder(10, 5, 10, 5)
        ));

        JLabel titleLabel = new JLabel("LOC_TITLE"); // todo add logo
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setAlignmentY(CENTER_ALIGNMENT);

        settingsButton = new JButton("LOC_BUTTON_SETTINGS");
        settingsButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        settingsButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        settingsButton.setAlignmentY(CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(settingsButton);

        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel intervalPanel = new JPanel();
        intervalPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(5, 10, 5, 10);

        hoursTextField = createIntegerTextField("LOC_CP_HOURS_TITLE");

        intervalPanel.add(hoursTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        minutesTextField = createIntegerTextField("LOC_CP_MINUTES_TITLE");

        intervalPanel.add(minutesTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        secondsTextField = createIntegerTextField("LOC_CP_SECONDS_TITLE");

        intervalPanel.add(secondsTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        milisecondsTextField = createIntegerTextField("LOC_CP_MILLISECONDS_TITLE");

        intervalPanel.add(milisecondsTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        intervalPanel.add(Box.createHorizontalGlue());

        mainPanel.add(intervalPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JTextField createIntegerTextField(String title) {
        JTextField textField = new JTextField("0", 10);
        textField.setBorder(new TitledBorder(
            new EmptyBorder(5, 5, 5, 5),
            title
        ));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());

        return textField;
    }
}
