package Graphics;

import AI.Brain;
import AI.GraphVertex;
import Board.BoardController;
import Board.CellType;
import Board.WorkField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton btnObstacle;
    private JButton btnEmitterStart;
    private JButton btnEmitterFinish;
    private JButton btnEmpty;
    private Canvas canvas;

    private ArrayList<GraphVertex> solution;
    private BoardController boardController;
    private CellType selectedCellType;
    private WorkField workField;
    private Brain brain;

    private int columnsAmount;
    private int rowsAmount;

    public ViewController() {
        setSize(800, 730);
        setResizable(false);
        setTitle("RAY");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(pnlRoot);
        solution = new ArrayList<GraphVertex>();
        columnsAmount = rowsAmount = 10; // default value.
        workField = new WorkField(columnsAmount, rowsAmount);
        boardController = new BoardController(workField);
        boardController.initField();
        canvas.setWorkField(workField);
        canvas.loadTileset("res/tileset.png");
        MouseHandler handler = new MouseHandler();
        canvas.addMouseListener(handler);
        canvas.addMouseMotionListener(handler);
        selectedCellType = CellType.OBSTACLE;
        assignButtonIcons();
        addActionListeners();
        setVisible(true);
    }

    private void resetInitBoard(){
        workField = new WorkField(columnsAmount, rowsAmount);
        boardController.setWorkField(workField);
    }

    private void reset(){
        solution.clear();
        resetInitBoard();
    }

    private int getFieldRow(int y){
        double tileHeight = canvas.getHeight() / rowsAmount;
        return (int) Math.floor(y / tileHeight);
    }

    private int getFieldColumn(int x) {
        double tileWidth = canvas.getWidth() / columnsAmount;
        return (int) Math.floor(x / tileWidth);
    }

    private void assignButtonIcons(){
        btnEmpty.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(TextureIndexes.INDEX_EMPTY)));
        btnObstacle.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(TextureIndexes.INDEX_OBSTACLE)));
        btnEmitterStart.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(TextureIndexes.INDEX_EMITTER_START)));
        btnEmitterFinish.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(TextureIndexes.INDEX_EMITTER_FINISH)));
    }

    private void createUIComponents() {
        try {
            pnlSurface = new Canvas(); // set the canvas
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvas = (Canvas)pnlSurface; // we just need to communicate with pnlSurface as with canvas, so we morph it.
    }

    private void addActionListeners() {
        btnEmitterStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCellType = CellType.EMITTER_START;
            }
        });

        btnEmitterFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCellType = CellType.EMITTER_FINISH;
            }
        });

        btnObstacle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCellType = CellType.OBSTACLE;
            }
        });

        btnEmpty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCellType = CellType.FREE;
            }
        });
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
            canvas.getSelection().setLocation(-1, -1);
            canvas.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            canvas.getSelection().setLocation(getFieldColumn(e.getX()), getFieldRow(e.getY()));
            canvas.repaint();
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            mouseMoved(e);
        }
    }
}