package ac.echo.util.item;

public enum ItemDataColor {

    WHITE((short) 0),
    ORANGE((short) 1),
    MAGENTA((short) 2),
    LIGHT_BLUE((short) 3),
    YELLOW((short) 4),
    LIME((short) 5),
    PINK((short) 6),
    GRAY((short) 7),
    LIGHT_GRAY((short) 8),
    CYAN((short) 9),
    PURPLE((short) 10),
    BLUE((short) 11),
    BROWN((short) 12),
    GREEN((short) 13),
    RED((short) 14),
    BLACK((short) 15);

    private final short value;

    ItemDataColor(short value) {
        if (value > 15 || value < 0) {
            throw new IllegalArgumentException("Value must be between 0 and 15.");
        }

        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public static ItemDataColor getByValue(short value) {
        for (ItemDataColor color : ItemDataColor.values()) {
            if (value == color.value) return color;
        }

        return null;
    }

}