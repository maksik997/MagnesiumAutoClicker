package pl.magzik;

import pl.magzik.ui.ClickerPanel;
import pl.magzik.ui.SettingsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame {

    private ClickerPanel clickerPanel;

    private SettingsPanel settingsPanel;

    private List<JPanel> panels;

    public View() throws HeadlessException {
        panels = new ArrayList<>();
        clickerPanel = new ClickerPanel();
        settingsPanel = new SettingsPanel();
        panels.add(clickerPanel);
        panels.add(settingsPanel);

        this.add(clickerPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 600));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("LOC_TITLE");
    }
}
