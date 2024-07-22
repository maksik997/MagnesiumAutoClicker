// TODO:
//  Redundancy etc.,
//  Stock action listeners (turn off text fields if radio button isn't clicked),

package pl.magzik.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import static pl.magzik.ui.utils.Utility.createIntegerTextField;

public class ClickerPanel extends JPanel {

    private JButton settingsButton, pickLocationButton, startButton, stopButton, toggleButton;

    private JTextField hoursTextField, minutesTextField, secondsTextField, milisecondsTextField, timesTextField, xLocationTextField, yLocationTextField;

    private ButtonGroup timesButtonGroup, locationButtonGroup;

    private JComboBox<String> buttonComboBox, typeComboBox;

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

        JRadioButton infiniteTimes = new JRadioButton("LOC_CP_INFINITE_TIMES");
        infiniteTimes.setSelected(true);
        infiniteTimes.setAlignmentX(LEFT_ALIGNMENT);
        infiniteTimes.setBorder(new EmptyBorder(5, 7, 5, 7));

        timesButtonGroup.add(infiniteTimes);
        clickRepeatPanel.add(infiniteTimes);

        JPanel timesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timesPanel.setAlignmentX(LEFT_ALIGNMENT);

        JRadioButton timesRadioButton = new JRadioButton();
        timesButtonGroup.add(timesRadioButton);
        timesPanel.add(timesRadioButton);

        timesTextField = createIntegerTextField("LOC_CP_TIMES_TITLE");
        timesPanel.add(timesTextField);

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
        JRadioButton currentLocationRadioButton = new JRadioButton("LOC_CP_CURRENT_LOCATION");
        currentLocationRadioButton.setSelected(true);
        locationButtonGroup.add(currentLocationRadioButton);
        currentLocationPanel.add(currentLocationRadioButton);
        locationPanel.add(currentLocationPanel);

        JPanel customLocationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton customLocationRadioButton = new JRadioButton("LOC_CP_CUSTOM_LOCATION");
        locationButtonGroup.add(customLocationRadioButton);
        customLocationPanel.add(customLocationRadioButton);

        xLocationTextField = createIntegerTextField("X");
        customLocationPanel.add(xLocationTextField);

        yLocationTextField = createIntegerTextField("Y");
        customLocationPanel.add(yLocationTextField);

        pickLocationButton = new JButton("LOC_CP_PICK_LOCATION_BUTTON");
        customLocationPanel.add(pickLocationButton);

        locationPanel.add(customLocationPanel);

        mainPanel.add(locationPanel);
    }

    private void initializeFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.X_AXIS));
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
}