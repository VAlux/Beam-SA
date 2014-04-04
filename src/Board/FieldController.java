package Board;
/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

import java.io.*;
import java.util.Random;

public class FieldController {

    private WorkField workField;

    public FieldController(WorkField workField) {
        if(workField == null)
            return;
        this.workField = workField;
    }

    public Cell findCell(CellType type){
        if(workField == null)
            return null;
        for (int m = 0; m < workField.getRowsAmount(); m++){
            for (int n = 0; n < workField.getCollsAmount(); n++){
                if(workField.getCell(m, n).getType() == type){
                    return workField.getCell(m, n);
                }
            }
        }
        return null;
    }

    public void initField(){
        for (int m = 0; m < workField.getRowsAmount(); m++){
            for (int n = 0; n < workField.getCollsAmount(); n++){
                workField.setCell(m, n, new Cell(CellType.FREE, m, n));
            }
        }
    }

    public void clearField(){
        for (int m = 0; m < workField.getRowsAmount(); m++){
            for (int n = 0; n < workField.getCollsAmount(); n++) {
                workField.getCell(m, n).setType(CellType.FREE);
            }
        }
    }

    public void morphCells(CellType sourceType, CellType targetType) {
        for (int m = 0; m < workField.getRowsAmount(); m++) {
            for (int n = 0; n < workField.getCollsAmount(); n++) {
                if (workField.getCell(m, n).getType() == sourceType)
                    workField.setCellType(m, n, targetType);
            }
        }
    }

    public void generateObjects(CellType type) {
        Random rnd = new Random(System.currentTimeMillis());
        for (int m = 0; m < workField.getRowsAmount(); m++) {
            for (int n = 0; n < workField.getCollsAmount(); n++) {
                if (rnd.nextInt(5) == 0)
                    workField.setCellType(m, n, type);
            }
        }
    }

    public void saveToFile(String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (int m = 0; m < workField.getRowsAmount(); m++) {
                for (int n = 0; n < workField.getCollsAmount(); n++) {
                    writer.write(String.valueOf(workField.getCellType(m, n).getValue()) + " ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String path) {
        clearField();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String row;
            String [] cellTypes;
            for (int i = 0; i < workField.getRowsAmount(); i++) {
                row = reader.readLine();
                cellTypes = row.split(" ");
                for (int j = 0; j < workField.getCollsAmount(); j++) {
                    workField.setCellType(i, j, CellType.fromValue(Integer.parseInt(cellTypes[j])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWorkField(WorkField field){
        this.workField = field;
    }
}