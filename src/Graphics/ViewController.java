package Graphics;

import AI.BeamSA;
import AI.Node;
import Board.Cell;
import Board.CellType;
import Board.FieldController;
import Board.WorkField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 * Created by Lux on 29.03.2014.
 */
public class ViewController extends JFrame {
    private JPanel pnlRoot;
    private JPanel pnlSurface;
    private JPanel pnlAlgorithm;
    private JPanel pnlStatisticsBox;
    private JLabel lblMapLabel;
    private JLabel lblStatistics;
    private JSlider sldProgress;
    private JTabbedPane tpnTools;

    private JButton btnEmpty;
    private JButton btnObstacle;
    private JButton btnEmrStart;
    private JButton btnEmrFinish;
    private JButton btnFind;
    private JButton btnClear;
    private JButton btnGenerate;
    private JButton btnSave;
    private JButton btnLoad;
    private JButton btnSettings;

    private ViewController instance;
    private Canvas canvas;
    private ArrayList<Node> solution;
    private FieldController fieldController;
    private CellType selectedCellType;
    private WorkField workField;
    private BeamSA beamSA;

    private int columnsAmount;
    private int rowsAmount;

    public ViewController() {
        setTitle("Beam");
        setSize(992, 910);
        setResizable(false);
        setContentPane(pnlRoot);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sldProgress.setMaximum(0);

        solution = new ArrayList<>();
        columnsAmount = rowsAmount = 16; // default value.
        workField = new WorkField(columnsAmount, rowsAmount);
        fieldController = new FieldController(workField);
        fieldController.initField();
        canvas.setWorkField(workField);
        canvas.loadTileset(formTilesetPath("tileset.png"));
        MouseHandler handler = new MouseHandler();
        canvas.addMouseListener(handler);
        canvas.addMouseMotionListener(handler);
        selectedCellType = CellType.FREE;
        assignButtonIcons();
        addActionListeners();
        instance = this;
        setVisible(true);
    }

    private String formTilesetPath(String tilesetName) {
        return getClass().getClassLoader().getResource("tilesets/" + tilesetName).getPath();
//        return System.getProperty("user.dir") + File.separator + "tilesets" + File.separator + tilesetName;
    }

    private void assignButtonIcons(){
        btnEmpty.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.FREE.getValue())));
        btnObstacle.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.OBSTACLE.getValue())));
        btnEmrStart.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.EMITTER_START.getValue())));
        btnEmrFinish.setIcon(new ImageIcon(canvas.getTilesetProcessor().getTileAt(CellType.EMITTER_FINISH.getValue())));
        btnFind.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/path.png")));
        btnLoad.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/search.png")));
        btnSave.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/save.png")));
        btnClear.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
        btnGenerate.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/setting.png")));
    }

    private void createUIComponents() {
        try {
            pnlSurface = new Canvas(); // set the canvas
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvas = (Canvas)pnlSurface; // we just need to communicate with pnlSurface as with canvas, so we morph it.
    }

    public void resizeField(int rowsAmount, int columnsAmount) {
        this.rowsAmount = rowsAmount;
        this.columnsAmount = columnsAmount;
        fieldController.resizeField(rowsAmount, columnsAmount);
        workField = fieldController.getWorkField(); // refresh the local workfield reference
        canvas.setWorkField(workField);
        resetStatistics();
        sldProgress.setMaximum(0);
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
                resetSolutionSteps();
                resetStatistics();
                fieldController.clearField();
                solution.clear();
                canvas.repaint();
            }
        });

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSolutionSteps();
                resetStatistics();
                fieldController.clearField();
                fieldController.generateObjects(CellType.OBSTACLE);
                canvas.repaint();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog(canvas, "Map name: ", "Input Map Name", QUESTION_MESSAGE);
                if (filename == null)
                    return;
                try {
                    fieldController.morphCells(CellType.PATH, CellType.FREE);
                    resetSolutionSteps();
                    resetStatistics();
                    fieldController.saveToFile("maps/" + filename);
                } catch (IOException e1) {
                    showErrorMsg("Filename is invalid or empty");
                }
            }
        });

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                int choice;
                choice = chooser.showDialog(canvas, "Open");
                if (choice == JFileChooser.APPROVE_OPTION) {
                    resetSolutionSteps();
                    resetStatistics();
                    try {
                        fieldController.loadFromFile(chooser.getSelectedFile().getPath());
                    } catch (IOException | NumberFormatException e1) {
                        showErrorMsg("Can't load file: " + chooser.getSelectedFile());
                    }
                }
            }
        });

        btnSettings.addActionListener(e -> {
            Settings settings = new Settings(instance);
            settings.setVisible(true);
        });

        btnFind.addActionListener(e -> {
            fieldController.morphCells(CellType.PATH, CellType.FREE); // clear previous result.
            Cell start = fieldController.findCell(CellType.EMITTER_START);
            Cell finish = fieldController.findCell(CellType.EMITTER_FINISH);
            if (start == null || finish == null) {
                showErrorMsg("DWF setup is not valid");
                return;
            }
            beamSA = new BeamSA(workField, start, finish);
            if(beamSA.findSolution()) {
                showInfoMsg("Path had been found!");
            } else {
                showErrorMsg("i am stuck!");
            }
            solution = beamSA.getSolution();
            sldProgress.setMaximum(beamSA.getOpened().size());
            updateStatistics();
            canvas.setOpened(beamSA.getOpened());
            canvas.setSolution(solution);
            for (Node node : solution) {
                workField.setCellType(node.getX(), node.getY(), CellType.PATH);
            }
            canvas.repaint();
        });

        sldProgress.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.setOpenedStepsAmount(sldProgress.getValue());
                canvas.repaint();
            }
        });
    }

    private void updateStatistics(){
        lblStatistics.setText("Execution time: " + beamSA.getExecTime() +
                              " ms | Path length: " + solution.size() +
                              " nodes | Opened nodes amount: " + beamSA.getOpened().size());
    }

    private void resetStatistics(){
        lblStatistics.setText("Ready...");
    }

    private void resetSolutionSteps(){
        canvas.setOpenedStepsAmount(0);
        sldProgress.setMaximum(0);
    }

    private void showErrorMsg(String errorText) {
        JOptionPane.showMessageDialog(this, errorText, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfoMsg(String infoText) {
        JOptionPane.showMessageDialog(this, infoText, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private int getFieldRow(int y){
        double tileHeight = canvas.getHeight() / rowsAmount;
        return (int) Math.floor(y / tileHeight);
    }

    private int getFieldColumn(int x) {
        double tileWidth = canvas.getWidth() / columnsAmount;
        return (int) Math.floor(x / tileWidth);
    }

    public int getColumnsAmount() {
        return columnsAmount;
    }

    public int getRowsAmount() {
        return rowsAmount;
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
            workField.setCellType(getFieldColumn(e.getX()), getFieldRow(e.getY()), selectedCellType);
            mouseMoved(e);
        }
    }
}