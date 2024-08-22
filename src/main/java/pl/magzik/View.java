package pl.magzik;

import pl.magzik.ui.ClickerPanel;
import pl.magzik.ui.SettingsPanel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all GUI elements. Extends {@link JFrame}
 * */
public class View extends JFrame {

    public static final int CLICKER_SCENE = 0, SETTINGS_SCENE = 1;

    private final ClickerPanel clickerPanel;

    private final SettingsPanel settingsPanel;

    private final List<JPanel> panels;

    private int currentScene;

    public View() throws HeadlessException {
        clickerPanel = new ClickerPanel();
        settingsPanel = new SettingsPanel();

        panels = new ArrayList<>();
        panels.add(clickerPanel);
        panels.add(settingsPanel);

        add(clickerPanel);

        currentScene = 0;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(600, 550));
        pack();
        setResizable(false);
        setTitle("LOC_TITLE");

        URL iconURL = View.class.getClassLoader().getResource("icon.png");
        // Probably should be checked,
        // but due to the fact that icon is supposed to be in jar package
        // that shouldn't happen
        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);

        setIconImage(icon.getImage());
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

    public int getCurrentScene() {
        return currentScene;
    }

    /**
     * Changes scene.
     * @param idx Index of scene. Can be:
     *            <ul>
     *              <li>{@link #CLICKER_SCENE} Clicker Scene</li>
     *              <li>{@link #SETTINGS_SCENE} Settings Scene</li>
     *            </ul>
     * @throws IndexOutOfBoundsException When given index is out-of-bounds
     * */
    public void changeScene(int idx) {
        if (idx >= panels.size() || idx < 0) throw new IndexOutOfBoundsException();

        panels.forEach(this::remove);

        add(panels.get(idx));
        currentScene = idx;

        repaint();
        revalidate();
    }
}
