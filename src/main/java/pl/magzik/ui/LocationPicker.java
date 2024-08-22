package pl.magzik.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Window for location picking. Is an instance of {@link JFrame}
 * */
public class LocationPicker extends JFrame {

    private int x, y;

    public LocationPicker() throws HeadlessException {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setUndecorated(true);
        setAlwaysOnTop(true);
        setResizable(false);
        setType(Type.UTILITY);
        requestFocusInWindow();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        Rectangle screenBounds = devices[0].getDefaultConfiguration().getBounds();
        for (GraphicsDevice device : devices) {
            screenBounds = screenBounds.union(device.getDefaultConfiguration().getBounds());
        }

        setBounds(screenBounds);

        setBackground(new Color(0,0,0,0.5f));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                dispose();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                update();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                update();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    x = 0;
                    y = 0;
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    dispose();
                }
            }
        });
    }

    /**
     * Updates saved mouse position.
     * */
    private void update() {
        Point cursorLocation = MouseInfo.getPointerInfo().getLocation();
        x = cursorLocation.x;
        y = cursorLocation.y;
    }

    public int getXPoint() {
        return x;
    }

    public int getYPoint() {
        return y;
    }
}
