package pl.magzik;

public class Model {
    public enum MouseButton {
        LEFT, RIGHT, MIDDLE;

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

        @Override
        public String toString() {
            return switch (this) {
                case SINGLE -> "LOC_SINGLE_CLICK";
                case DOUBLE -> "LOC_DOUBLE_CLICK";
            };
        }
    }

    private MouseButton mouseButton;
    private ClickType clickType;

    public Model() {
        mouseButton = MouseButton.LEFT;
        clickType = ClickType.SINGLE;
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
}
