package Graphics;

import AI.Brain;
import AI.Node;
import Board.FieldController;
import Board.Cell;
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
    private JTabbedPane tpnTools;
    private JPanel pnlSurface;
    private JPanel pnlRoot;

    private JButton btnEmpty;
    private JButton btnObstacle;
    private JButton btnEmrStart;
    private JButton btnEmrFinish;
    private JButton btnFind;

    private Canvas canvas;
    private ArrayList<Node> solution;
    private FieldController fieldController;
    private CellType selectedCellType;
    private WorkField workField;
    private Brain brain;

    private int columnsAmount;
    private int rowsAmount;

    public ViewController() {
        setSize(890, 730);
        setResizable(false);
        setTitle("RAY");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(pnlRoot);
        solution = new ArrayList<Node>();
        columnsAmount = rowsAmount = 10; // default value.
        workField = new WorkField(columnsAmount, rowsAmount);
        fieldController = new FieldController(workField);
        fieldController.initField();
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
        fieldController.setWorkField(workField);
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
        btnEmpty.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.FREE.getValue())));
        btnObstacle.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.OBSTACLE.getValue())));
        btnEmrStart.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.EMITTER_START.getValue())));
        btnEmrFinish.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.EMITTER_FINISH.getValue())));
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
        btnEmrStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCellType = CellType.EMITTER_START;
            }
        });

        btnEmrFinish.addActionListener(new ActionListener() {
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

        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cell start = fieldController.findCell(CellType.EMITTER_START);
                Cell finish = fieldController.findCell(CellType.EMITTER_FINISH);
                assert start != null && finish != null;
                brain = new Brain(workField, start, finish);
                if(brain.findSolution()){
                    for (Node node : brain.getSolution()) {
                        workField.setCellType(node.getX(), node.getY(), CellType.PATH);
                    }
                    canvas.repaint();
                }
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