package Board;

/**
 * Created by Lux on 24.03.2014.
 */
public enum CellType {
    PATH(0),
    FREE(1),
    OBSTACLE(11),
    EMITTER_START(12),
    EMITTER_FINISH(18);

    private final int value;

    CellType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "value: [ " + value + " ] name: [ " + name() + " ]";
    }
}
