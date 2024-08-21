package pl.magzik;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.List;

public class Model {
    public enum MouseButton {
        LEFT, RIGHT, MIDDLE;

        public static MouseButton get(String key) {
            return switch (key) {
                case "LOC_LMB" -> LEFT ;
                case "LOC_RMB" -> RIGHT;
                case "LOC_MMB" -> MIDDLE;
                default -> throw new IllegalStateException("Unexpected value: " + key);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case LEFT ->  "LOC_LMB";
                case RIGHT ->  "LOC_RMB";
                case MIDDLE ->  "LOC_MMB";
            };
        }
    }
    public enum ClickType {
        SINGLE, DOUBLE;

        public static ClickType get(String key) {
            return switch (key) {
                case "LOC_SINGLE_CLICK" -> SINGLE ;
                case "LOC_DOUBLE_CLICK" -> DOUBLE;
                default -> throw new IllegalStateException("Unexpected value: " + key);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case SINGLE -> "LOC_SINGLE_CLICK";
                case DOUBLE -> "LOC_DOUBLE_CLICK";
            };
        }
    }
    public enum State {
        RUNNING, RESET
    }

    private MouseButton mouseButton;
    private ClickType clickType;
    private Runnable clickTask;
    private State state;
    private int clickCount;

    private final ExecutorService executor;

    private final File configFile;

    private String language, theme, startHotkey, stopHotkey, toggleHotkey;

    public Model() throws IOException {

        File configDirectory = new File(System.getProperty("user.home") + File.separator + ".magnesiumAutoClicker");
        if (!configDirectory.exists()) {
            configDirectory.mkdir();
        }

        configFile = new File(configDirectory, "config.cfg");

        if (!configFile.exists()) {
            try (InputStream resourceStream = Model.class.getClassLoader().getResourceAsStream("default.cfg")) {
                if (resourceStream == null) {
                    throw new FileNotFoundException("default.cfg not found");
                }

                Files.copy(resourceStream, configFile.toPath());
            }
        }


        List<String> lines = Files.readAllLines(configFile.toPath());

        for (String line : lines) {
            String[] split = line.split(": ");
            String key = split[0], value = split[1];

            switch (key) {
                case "lang" -> language = value;
                case "theme" -> theme = value.toUpperCase();
                case "start_hotkey" -> startHotkey = value.toUpperCase();
                case "stop_hotkey" -> stopHotkey = value.toUpperCase();
                case "toggle_hotkey" -> toggleHotkey = value.toUpperCase();
                default -> throw new IllegalStateException("Unexpected key: " + key);
            }
        }

        mouseButton = MouseButton.LEFT;
        clickType = ClickType.SINGLE;

        clickTask = null;
        state = State.RESET;
        clickCount = 0;

        executor = Executors.newSingleThreadExecutor();
    }

    public void startClicker(int interval, int times, String mouseButton, String clickType, int x, int y) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        clickTask = () -> {
            state = State.RUNNING;

            try {
                Robot robot = new Robot();
                MouseButton button = MouseButton.get(mouseButton);
                ClickType type = ClickType.get(clickType);

                int buttonId;
                if (button == MouseButton.LEFT) buttonId = InputEvent.BUTTON1_DOWN_MASK;
                else if (button == MouseButton.RIGHT) buttonId = InputEvent.BUTTON3_DOWN_MASK;
                else buttonId = InputEvent.BUTTON2_DOWN_MASK;

                int n = 0;
                while ((n++ < times || times == -1) && state == State.RUNNING) {
                    if (x != -1 && y != -1) {
                        robot.mouseMove(x, y);
                    }

                    robot.mousePress(buttonId);
                    robot.mouseRelease(buttonId);

                    if (type == ClickType.DOUBLE) {
                        robot.mousePress(buttonId);
                        robot.mouseRelease(buttonId);
                    }

                    clickCount++;

                    Thread.sleep(interval);
                }
            } catch (AWTException | InterruptedException e) {
                throw new RuntimeException(e); // todo
            } finally {
                state = State.RESET;
                latch.countDown();
            }
        };

        executor.execute(clickTask);
        latch.await();
    }

    public void startClicker(int interval, int times, String mouseButton, String clickType) throws InterruptedException {
        startClicker(interval, times, mouseButton, clickType, -1, -1);
    }

    public void stopClicker() {
        synchronized (this) {
            state = State.RESET;
        }
    }

    public MouseButton getMouseButton() {
        return mouseButton;
    }

    public void setMouseButton(MouseButton mouseButton) {
        this.mouseButton = mouseButton;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public void setClickType(ClickType clickType) {
        this.clickType = clickType;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getLanguage() {
        return language;
    }

    public String getTheme() {
        return theme;
    }

    public String getStartHotkey() {
        return startHotkey;
    }

    public String getStopHotkey() {
        return stopHotkey;
    }

    public String getToggleHotkey() {
        return toggleHotkey;
    }
}
