package Graphics;

import AI.Brain;
import AI.GraphVertex;
import Board.BoardController;
import Board.CellType;
import Board.WorkField;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lux on 29.03.2014.
 */
public class ViewController extends JFrame {
    private JPanel pnlRoot;
    private JPanel pnlSurface;
    private Canvas canvas;
    private ArrayList<GraphVertex> solution;
    private BoardController boardController;
    private Brain brain;
    private WorkField workField;
    private CellType selectedCellType;

    private final int FIELD_WIDTH = 6;
    private final int FIELD_HEIGHT = 6;

    public ViewController() {
        //frame initialization
        setSize(600, 600);
        setTitle("RAY");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(pnlRoot);
        solution = new ArrayList<GraphVertex>();
        workField = new WorkField(FIELD_WIDTH, FIELD_HEIGHT);
        boardController = new BoardController(workField);
        boardController.initField();
        canvas.setWorkField(workField);
        canvas.loadTileTextures("res/tileset.png");
        MouseHandler handler = new MouseHandler();
        canvas.addMouseListener(handler);
        canvas.addMouseMotionListener(handler);
        selectedCellType = CellType.EMITTER;
        this.setVisible(true);
    }

    private void resetInitBoard(){
        workField = new WorkField(FIELD_WIDTH, FIELD_HEIGHT);
        boardController.setWorkField(workField);
    }

    private void reset(){
        solution.clear();
        resetInitBoard();
    }

    private int getFieldRow(int y){
        double tileHeight = getHeight() / FIELD_HEIGHT;
        return (int) Math.floor(y / tileHeight);
    }

    private int getFieldColumn(int x) {
        double tileWidth = getWidth() / FIELD_WIDTH;
        return (int) Math.floor(x / tileWidth);
    }

    private void createUIComponents() {
        try {
            this.pnlSurface = new Canvas(); // set the canvas
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
        this.canvas = (Canvas)pnlSurface; // we just need to communicate with pnlSurface as with canvas, so we morph it.
    }
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            workField.setCellType(getFieldColumn(e.getX()), getFieldRow(e.getY()), selectedCellType);
            canvas.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            canvas.getUnderSelection().setLocation(-1, -1);
            canvas.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            canvas.getUnderSelection().setLocation(getFieldColumn(e.getX()), getFieldRow(e.getY()));
            canvas.repaint();
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            mouseMoved(e);
        }
    }
}