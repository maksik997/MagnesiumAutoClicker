package pl.magzik;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

/**
 * Model class, implements all app logic (or at least most of it).
 * */
public class Model {

    public static final Map<String, Integer> keyMap = new HashMap<>();

    static {
        // Supported keyboard buttons for shortcuts
        keyMap.put("F1", NativeKeyEvent.VC_F1);
        keyMap.put("F2", NativeKeyEvent.VC_F2);
        keyMap.put("F3", NativeKeyEvent.VC_F3);
        keyMap.put("F4", NativeKeyEvent.VC_F4);
        keyMap.put("F5", NativeKeyEvent.VC_F5);
        keyMap.put("F6", NativeKeyEvent.VC_F6);
        keyMap.put("F7", NativeKeyEvent.VC_F7);
        keyMap.put("F8", NativeKeyEvent.VC_F8);
        keyMap.put("F9", NativeKeyEvent.VC_F9);
        keyMap.put("F10", NativeKeyEvent.VC_F10);
        keyMap.put("F11", NativeKeyEvent.VC_F11);
        keyMap.put("F12", NativeKeyEvent.VC_F12);
        keyMap.put("Caps Lock", NativeKeyEvent.VC_CAPS_LOCK);
        keyMap.put("Ctrl", NativeKeyEvent.VC_CONTROL);
        keyMap.put("Alt", NativeKeyEvent.VC_ALT);
        keyMap.put("Shift", NativeKeyEvent.VC_SHIFT);
        keyMap.put("Enter", NativeKeyEvent.VC_ENTER);
        keyMap.put("Space", NativeKeyEvent.VC_SPACE);
        keyMap.put("Escape", NativeKeyEvent.VC_ESCAPE);
        keyMap.put("Backspace", NativeKeyEvent.VC_BACKSPACE);
        keyMap.put("Delete", NativeKeyEvent.VC_DELETE);
        keyMap.put("Context Menu", NativeKeyEvent.VC_CONTEXT_MENU);
    }

    /**
     * Enum that represents mouse button (Left, Right, Middle)
     * */
    public enum MouseButton {
        LEFT, RIGHT, MIDDLE;

        /**
         * Translates string into enum.
         * @param key
         *          Text to be translated. Can be: {@code "LOC_LMB"}, {@code "LOC_RMB"}, {@code "LOC_MMB"}
         * @return Expected enum value e.g. for {@code "LOC_LMB"} will return {@code LEFT}.
         * @throws IllegalStateException when given key is not supported.
         * */
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

    /**
     * Enum that represents click type (single, double)
     * */
    public enum ClickType {
        SINGLE, DOUBLE;

        /**
         * Translates string into enum.
         * @param key
         *          Text to be translated. Can be: {@code "LOC_SINGLE_CLICK"}, {@code "LOC_DOUBLE_CLICK"}
         * @return Expected enum value e.g. for {@code "LOC_SINGLE_CLICK"} will return {@code SINGLE}.
         * @throws IllegalStateException when given key is not supported.
         * */
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

    /**
     * Enum that represents State of clicker task.
     * */
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
        if (!configDirectory.exists() && !configDirectory.mkdirs()) {
            throw new IOException("Unable to create directory: " + configDirectory.getAbsolutePath());
        }

        configFile = new File(configDirectory, "config.cfg");
        loadConfig();

        mouseButton = MouseButton.LEFT;
        clickType = ClickType.SINGLE;

        clickTask = null;
        state = State.RESET;
        clickCount = 0;

        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Saves user configuration into config file if any change occurred.
     * @param lang Language to be saved
     * @param theme Theme to be saved
     * @param startHotkey Start shortcut to be saved
     * @param stopHotkey Stop shortcut to be saved
     * @param toggleHotkey Toggle shortcut to be saved
     * @throws IOException When I/O error occurs.
     * */
    public void saveConfig(String lang, String theme, String startHotkey, String stopHotkey, String toggleHotkey) throws IOException {
        if (
            Objects.equals(lang, this.language) &&
            Objects.equals(theme, this.theme) &&
            Objects.equals(startHotkey, this.startHotkey) &&
            Objects.equals(stopHotkey, this.stopHotkey) &&
            Objects.equals(toggleHotkey, this.toggleHotkey)
        ) {
            return;
        }

        this.language = lang;
        this.theme = theme;
        this.startHotkey = startHotkey;
        this.stopHotkey = stopHotkey;
        this.toggleHotkey = toggleHotkey;

        try (BufferedWriter writer = Files.newBufferedWriter(configFile.toPath())) {
            writer.write(String.format("lang: %s", lang));
            writer.newLine();
            writer.write(String.format("theme: %s", theme.toLowerCase()));
            writer.newLine();
            writer.write(String.format("start_hotkey: %s", startHotkey));
            writer.newLine();
            writer.write(String.format("stop_hotkey: %s", stopHotkey));
            writer.newLine();
            writer.write(String.format("toggle_hotkey: %s", toggleHotkey));

            writer.flush();
        }
    }

    /**
     * Loads user configuration.
     * @throws IOException When I/O error occurs.
     * */
    private void loadConfig() throws IOException {
        // If there is no user configuration copy default configuration.
        if (!configFile.exists()) {
            try (InputStream resourceStream = Model.class.getClassLoader().getResourceAsStream("default.cfg")) {
                if (resourceStream == null) throw new FileNotFoundException("File default.cfg not found.");

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
    }

    /**
     * Starts clicker task. Important: You should call this method in separate thread.
     * @param interval Interval of clicks.
     * @param times How many clicks should happen.
     * @param mouseButton Which mouse button should be used.
     * @param clickType What kind of click should occur.
     * @param x At which horizontal position should be mouse cursor.
     * @param y At which vertical position should be mouse cursor.
     * @throws InterruptedException When clicker task is interrupted.
     * */
    public void startClicker(int interval, int times, String mouseButton, String clickType, int x, int y) throws InterruptedException {
        Objects.requireNonNull(mouseButton);
        Objects.requireNonNull(clickType);

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
                throw new RuntimeException(e); // will be probably ignored...
            } finally {
                state = State.RESET;
                latch.countDown();
            }
        };

        executor.execute(clickTask);
        latch.await();
    }

    /**
     * Starts clicker task. Important: You should call this method in separate thread.
     * @param interval Interval of clicks.
     * @param times How many clicks should happen.
     * @param mouseButton Which mouse button should be used.
     * @param clickType What kind of click should occur.
     * @throws InterruptedException When clicker task is interrupted.
     * */
    public void startClicker(int interval, int times, String mouseButton, String clickType) throws InterruptedException {
        startClicker(interval, times, mouseButton, clickType, -1, -1);
    }

    /**
     * Stops the clicker lazily (just set desired state and wait for loop iteration to finish)
     * */
    public synchronized void stopClicker() {
        state = State.RESET;
    }

    public synchronized boolean isClickerRunning() {
        return state == State.RUNNING;
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

    public int getStartHotkeyKeyEvent() {
        return keyMap.get(startHotkey);
    }

    public String getStopHotkey() {
        return stopHotkey;
    }

    public int getStopHotkeyKeyEvent() {
        return keyMap.get(stopHotkey);
    }

    public String getToggleHotkey() {
        return toggleHotkey;
    }

    public int getToggleHotkeyKeyEvent() {
        return keyMap.get(toggleHotkey);
    }
}
