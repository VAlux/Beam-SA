package Board;
/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

/**
 * User: Lux
 * Date: 27.11.13
 * Time: 15:37
 */

public class WorkField {

    private Cell [][] cells;
    private int collsAmount;
    private int rowsAmount;

    public WorkField(int rows, int colls) {
        rowsAmount = rows;
        collsAmount = colls;
        cells = new Cell[rowsAmount][collsAmount];
    }

    @Override
    public WorkField clone() throws CloneNotSupportedException {
        WorkField workField = new WorkField(rowsAmount, collsAmount);
        for (int i = 0; i < rowsAmount; i++) {
            for (int j = 0; j < collsAmount; j++) {
                workField.setCell(i, j, this.getCell(i, j));
            }
        }
        return workField;
    }

    @Override
    public String toString(){
        String field = "";
        for (int i = 0; i < rowsAmount; i++){
            for (int j = 0; j < collsAmount; j++){
                field += cells[i][j] + "\t";
            }
            field += "\n";
        }
        return field;
    }

    public int getCollsAmount() {
        return collsAmount;
    }

    public int getRowsAmount() {
        return rowsAmount;
    }

    public Cell getCell(int row, int column){
        return cells[row][column];
    }

    public void setCell(int row, int column, Cell cell){
        cells[row][column] = cell;
    }

    public void setCellType(int row, int column, CellType type) {
        cells[row][column].setType(type);
    }

    public int getBricksAmount(){
        return collsAmount * rowsAmount;
    }
}