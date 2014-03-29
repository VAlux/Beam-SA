package Board;
/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

public class BoardController {

    private WorkField workField;
    //private Point prevState;

    public BoardController(WorkField workField) {
        if(workField == null)
            return;
        this.workField = workField;
       // prevState = new Point(workField.getCollsAmount(), workField.getRowsAmount());
    }

//    private void findEmptyBrickPos(){
//        if(workField == null)
//            return;
//        for (int m = 0; m < workField.getRowsAmount(); m++){
//            for (int n = 0; n < workField.getCollsAmount(); n++){
//                if(workField.getCell(m, n) == WorkField.EMPTY_BRICK){
//                    emptyBrick.x = m;
//                    emptyBrick.y = n;
//                    return;
//                }
//            }
//        }
//    }

    public void initField(){
        for (int m = 0; m < workField.getRowsAmount(); m++){
            for (int n = 0; n < workField.getCollsAmount(); n++){
                workField.setCell(m, n, new Cell(CellType.FREE));
            }
        }
    }

//    public void shuffle(int times){
//        Random rnd = new Random();
//        ArrayList<Point> directions = MoveDirections.toList();
//        int index;
//        for (int i = 0; i < times; i++){
//            index = rnd.nextInt(4);
//            performMove(directions.get(index).x, directions.get(index).y);
//        }
//    }

//    private void swap(int i, int j){
//        workField.setCell(emptyBrick.x, emptyBrick.y, workField.getCell(i, j));
//        workField.setCell(i, j, WorkField.EMPTY_BRICK);
//        emptyBrick.x = i;
//        emptyBrick.y = j;
//    }

//    public boolean performMove(int i, int j){
//        if(prevState.x == emptyBrick.x + j && prevState.y == emptyBrick.y + i){
//            return false;
//        }
//        if(emptyBrick.x + j >= workField.getFieldSize() || emptyBrick.x + j < 0 ||
//           emptyBrick.y + i >= workField.getFieldSize() || emptyBrick.y + i < 0){
//           return false;
//        }
//        swap(emptyBrick.x + j, emptyBrick.y + i); // i and j should be used in invert order because of 2d array indexing
//        return true;
//    }

//    public void memorizeState(){
//        prevState.x = emptyBrick.x;
//        prevState.y = emptyBrick.y;
//    }

    public void setWorkField(WorkField field){
        this.workField = field;
        //findEmptyBrickPos();
    }
}