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
        this.setPreferredSize(new Dimension(600, 550));
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("LOC_TITLE");
    }

    public ClickerPanel getClickerPanel() {
        return clickerPanel;
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public List<JPanel> getPanels() {
        return panels;
    }

    public void changeScene(int idx) {
        if (idx >= panels.size() || idx < 0) throw new IndexOutOfBoundsException();

        panels.forEach(this::remove);

        this.add(panels.get(idx));

        repaint();
        revalidate();
    }
}
