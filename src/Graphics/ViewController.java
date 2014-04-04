package Graphics;

import AI.Brain;
import AI.Node;
import Board.Cell;
import Board.CellType;
import Board.FieldController;
import Board.WorkField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 * Created by Lux on 29.03.2014.
 */
public class ViewController extends JFrame {
    private JTabbedPane tpnTools;
    private JPanel pnlSurface;
    private JPanel pnlRoot;
    private JPanel pnlAlgorithm;
    private JLabel lblMapLabel;

    private JButton btnEmpty;
    private JButton btnObstacle;
    private JButton btnEmrStart;
    private JButton btnEmrFinish;
    private JButton btnFind;
    private JButton btnClear;
    private JButton btnGenerate;
    private JButton btnSave;
    private JButton btnLoad;

    private Canvas canvas;
    private ArrayList<Node> solution;
    private FieldController fieldController;
    private CellType selectedCellType;
    private WorkField workField;
    private Brain brain;

    private int columnsAmount;
    private int rowsAmount;

    public ViewController() {
        setSize(850, 710);
        setTitle("RAY");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(pnlRoot);
        solution = new ArrayList<>();
        columnsAmount = rowsAmount = 16; // default value.
        workField = new WorkField(columnsAmount, rowsAmount);
        fieldController = new FieldController(workField);
        fieldController.initField();
        canvas.setWorkField(workField);
        canvas.loadTileset("tilesets/tileset.png");
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
        btnFind.setIcon(new ImageIcon("icons/path.png"));
        btnLoad.setIcon(new ImageIcon("icons/search.png"));
        btnSave.setIcon(new ImageIcon("icons/save.png"));
        btnClear.setIcon(new ImageIcon("icons/cancel.png"));
        btnGenerate.setIcon(new ImageIcon("icons/setting.png"));
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

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldController.clearField();
                solution.clear();
                canvas.repaint();
            }
        });

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldController.generateObjects(CellType.OBSTACLE);
                canvas.repaint();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog(canvas, "Map name: ", "Input Map Name", QUESTION_MESSAGE);
                fieldController.saveToFile("maps/" + filename);
            }
        });

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int choice;
                choice = chooser.showDialog(canvas, "Open");
                if (choice == JFileChooser.APPROVE_OPTION) {
                    fieldController.loadFromFile(chooser.getSelectedFile().getPath());
                }
            }
        });

        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldController.morphCells(CellType.PATH, CellType.FREE); // clear previous result.
                Cell start = fieldController.findCell(CellType.EMITTER_START);
                Cell finish = fieldController.findCell(CellType.EMITTER_FINISH);
                assert start != null && finish != null;
                brain = new Brain(workField, start, finish);
                if(brain.findSolution()) {
                    solution = brain.getSolution();
                    for (Node node : solution) {
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
            if (e.getButton() == MouseEvent.BUTTON1)
                workField.setCellType(getFieldColumn(e.getX()), getFieldRow(e.getY()), selectedCellType);
            else
                workField.setCellType(getFieldColumn(e.getX()), getFieldRow(e.getY()), CellType.FREE);
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
            workField.setCellType(getFieldColumn(e.getX()), getFieldRow(e.getY()), selectedCellType);
        }
    }
}