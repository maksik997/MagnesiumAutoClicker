// TODO:
//  Redundancy etc.,

package pl.magzik.ui;

import pl.magzik.ui.utils.Utility;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static pl.magzik.ui.utils.Utility.createUnsignedIntegerTextField;
import static pl.magzik.ui.utils.Utility.createIntegerTextField;

public class ClickerPanel extends JPanel {

    private JButton settingsButton, pickLocationButton, startButton, stopButton, toggleButton;

    private JTextField hoursTextField, minutesTextField, secondsTextField, millisecondsTextField, timesTextField, xLocationTextField, yLocationTextField;

    private ButtonGroup timesButtonGroup, locationButtonGroup;

    private JComboBox<String> buttonComboBox, typeComboBox;

    private JRadioButton infiniteTimes, timesRadioButton, currentLocationRadioButton, customLocationRadioButton;

    public ClickerPanel() {
        setLayout(new BorderLayout());

        initializeHeaderPanel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        initializeIntervalPanel(mainPanel);

        initializeClickSettingPanel(mainPanel);

        initializeLocationPanel(mainPanel);

        add(mainPanel, BorderLayout.CENTER);

        initializeFooterPanel();
    }

    private void initializeHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel("LOC_TITLE"); // todo add logo
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setAlignmentY(CENTER_ALIGNMENT);

        settingsButton = new JButton("LOC_CP_SETTINGS_BUTTON");
        settingsButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        settingsButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        settingsButton.setAlignmentY(CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(settingsButton);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initializeIntervalPanel(JPanel mainPanel) {
        JPanel intervalPanel = new JPanel();
        intervalPanel.setLayout(new GridBagLayout());
        intervalPanel.setBorder(new CompoundBorder(
            new TitledBorder(
                new LineBorder(Color.GRAY, 1, true),
                "LOC_INTERVAL_TITLE"
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));

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

        hoursTextField = Utility.createUnsignedIntegerTextField("LOC_CP_HOURS_TITLE");

        intervalPanel.add(hoursTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        minutesTextField = Utility.createUnsignedIntegerTextField("LOC_CP_MINUTES_TITLE");

        intervalPanel.add(minutesTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        secondsTextField = Utility.createUnsignedIntegerTextField("LOC_CP_SECONDS_TITLE");

        intervalPanel.add(secondsTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        millisecondsTextField = Utility.createUnsignedIntegerTextField("LOC_CP_MILLISECONDS_TITLE");

        intervalPanel.add(millisecondsTextField, c);
        c.gridx = GridBagConstraints.RELATIVE;

        intervalPanel.add(Box.createHorizontalGlue());
        mainPanel.add(intervalPanel);
    }

    private void initializeClickSettingPanel(JPanel mainPanel) {
        JPanel clickSettingPanel = new JPanel();
        clickSettingPanel.setLayout(new GridLayout(1, 2));
        clickSettingPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel clickRepeatPanel = new JPanel();
        clickRepeatPanel.setLayout(new BoxLayout(clickRepeatPanel, BoxLayout.Y_AXIS));
        clickRepeatPanel.setBorder(new CompoundBorder(
            new TitledBorder(
                new LineBorder(Color.GRAY, 1, true),
                "LOC_CP_CLICK_REPEAT_TITLE"
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));

        timesButtonGroup = new ButtonGroup();

        infiniteTimes = new JRadioButton("LOC_CP_INFINITE_TIMES");
        infiniteTimes.setSelected(true);
        infiniteTimes.setAlignmentX(LEFT_ALIGNMENT);
        infiniteTimes.setBorder(new EmptyBorder(5, 7, 5, 7));

        timesButtonGroup.add(infiniteTimes);
        clickRepeatPanel.add(infiniteTimes);

        JPanel timesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timesPanel.setAlignmentX(LEFT_ALIGNMENT);

        timesRadioButton = new JRadioButton();
        timesButtonGroup.add(timesRadioButton);
        timesPanel.add(timesRadioButton);

        timesTextField = Utility.createUnsignedIntegerTextField("LOC_CP_TIMES_TITLE");
        timesTextField.setEnabled(false);
        timesPanel.add(timesTextField);

        infiniteTimes.addActionListener(_ -> timesTextField.setEnabled(false));
        timesRadioButton.addActionListener(_ -> timesTextField.setEnabled(true));

        clickRepeatPanel.add(timesPanel);

        clickSettingPanel.add(clickRepeatPanel);

        JPanel clickOptionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
//        clickOptionPanel.setLayout(new BoxLayout(clickOptionPanel, BoxLayout.Y_AXIS));
        clickOptionPanel.setBorder(new CompoundBorder(
            new TitledBorder(
                new LineBorder(Color.GRAY, 1, true),
                "LOC_CP_CLICK_OPTION_TITLE"
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel buttonLabel = new JLabel("LOC_CP_BUTTON_LABEL");
        clickOptionPanel.add(buttonLabel);
        buttonComboBox = new JComboBox<>();
        clickOptionPanel.add(buttonComboBox);


        JLabel typeLabel = new JLabel("LOC_CP_TYPE_LABEL");
        clickOptionPanel.add(typeLabel);
        typeComboBox = new JComboBox<>();
        clickOptionPanel.add(typeComboBox);

        clickSettingPanel.add(clickOptionPanel);

        mainPanel.add(clickSettingPanel);
    }

    private void initializeLocationPanel(JPanel mainPanel) {
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.Y_AXIS));
        locationPanel.setBorder(new CompoundBorder(
            new TitledBorder(
                new LineBorder(Color.GRAY, 1, true),
                "LOC_CP_POSITION_TITLE"
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));

        locationButtonGroup = new ButtonGroup();

        JPanel currentLocationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        currentLocationRadioButton = new JRadioButton("LOC_CP_CURRENT_LOCATION");
        currentLocationRadioButton.setSelected(true);
        locationButtonGroup.add(currentLocationRadioButton);
        currentLocationPanel.add(currentLocationRadioButton);
        locationPanel.add(currentLocationPanel);

        JPanel customLocationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customLocationRadioButton = new JRadioButton("LOC_CP_CUSTOM_LOCATION");
        locationButtonGroup.add(customLocationRadioButton);
        customLocationPanel.add(customLocationRadioButton);

        xLocationTextField = createIntegerTextField("X");
        xLocationTextField.setEnabled(false);
        customLocationPanel.add(xLocationTextField);

        yLocationTextField = createIntegerTextField("Y");
        yLocationTextField.setEnabled(false);
        customLocationPanel.add(yLocationTextField);

        pickLocationButton = new JButton("LOC_CP_PICK_LOCATION_BUTTON");
        pickLocationButton.setEnabled(false);
        customLocationPanel.add(pickLocationButton);

        currentLocationRadioButton.addActionListener(_ -> {
            pickLocationButton.setEnabled(false);
            xLocationTextField.setEnabled(false);
            yLocationTextField.setEnabled(false);
        });
        customLocationRadioButton.addActionListener(_ -> {
            pickLocationButton.setEnabled(true);
            xLocationTextField.setEnabled(true);
            yLocationTextField.setEnabled(true);
        });

        locationPanel.add(customLocationPanel);

        mainPanel.add(locationPanel);
    }

    private void initializeFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new GridLayout(1, 3));
        footerPanel.setBorder(new EmptyBorder(5, 12,  10, 12));

        startButton = new JButton("LOC_CP_START_BUTTON");
        startButton.setBorder(new EmptyBorder(10, 5, 10, 5));

        footerPanel.add(startButton);
        footerPanel.add(Box.createHorizontalGlue());

        stopButton = new JButton("LOC_CP_STOP_BUTTON");
        stopButton.setBorder(new EmptyBorder(10, 5, 10, 5));

        footerPanel.add(stopButton);
        footerPanel.add(Box.createHorizontalGlue());

        toggleButton = new JButton("LOC_CP_TOGGLE_BUTTON");
        toggleButton.setBorder(new EmptyBorder(10, 5, 10, 5));

        footerPanel.add(toggleButton);

        add(footerPanel, BorderLayout.SOUTH);
    }

    public JComboBox<String> getButtonComboBox() {
        return buttonComboBox;
    }

    public JComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }

    public JTextField getMillisecondsTextField() {
        return millisecondsTextField;
    }

    public String getHours() {
        return hoursTextField.getText();
    }

    public String getMinutes() {
        return minutesTextField.getText();
    }

    public String getSeconds() {
        return secondsTextField.getText();
    }

    public String getMilliseconds() {
        return millisecondsTextField.getText();
    }

    public JRadioButton getInfiniteTimes() {
        return infiniteTimes;
    }

    public JRadioButton getTimesRadioButton() {
        return timesRadioButton;
    }

    public JRadioButton getCurrentLocationRadioButton() {
        return currentLocationRadioButton;
    }

    public JRadioButton getCustomLocationRadioButton() {
        return customLocationRadioButton;
    }

    public String getTimes() {
        return timesTextField.getText();
    }

    public String getXLocation() {
        return xLocationTextField.getText();
    }

    public String getYLocation() {
        return yLocationTextField.getText();
    }

    public JTextField getXLocationTextField() {
        return xLocationTextField;
    }

    public JTextField getYLocationTextField() {
        return yLocationTextField;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JButton getToggleButton() {
        return toggleButton;
    }

    public JButton getPickLocationButton() {
        return pickLocationButton;
    }
}
