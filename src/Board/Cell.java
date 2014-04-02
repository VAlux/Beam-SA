package Board;

/**
 * Created by Lux on 24.03.2014.
 */
public class Cell {

    private CellType type;
    // logical positions on the board:
    private int yPos;
    private int xPos;
    //

    public Cell(CellType type) {
        this.type = type;
    }

    public Cell(CellType type, int xPos, int yPos) {
        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public int getyPos() {
        return yPos;
    }

    public int getxPos() {
        return xPos;
    }

    @Override
    public String toString() {
        return "X: " + xPos + " Y: " + yPos;
    }
}