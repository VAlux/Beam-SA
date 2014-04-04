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

    public static CellType fromValue(int value) {
        switch (value){
            case 0: {
                return PATH;
            }
            case 1: {
                return FREE;
            }
            case 11: {
                return OBSTACLE;
            }
            case 12: {
                return EMITTER_START;
            }
            case 18: {
                return EMITTER_FINISH;
            }
        }
        return FREE; // by default.
    }

    @Override
    public String toString() {
        return "value: [ " + value + " ] name: [ " + name() + " ]";
    }
}
