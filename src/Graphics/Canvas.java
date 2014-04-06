/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

package Graphics;

import AI.Node;
import Board.WorkField;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lux on 05.12.13.
 */
public class Canvas extends JPanel {

    private Font font;
    private Point selection;
    private Color selectionColor;
    private Color solutionColor;
    private Color fontColor;
    private WorkField workField;
    private Dimension tileSize;
    private ArrayList<BufferedImage> tiles;
    private TilesetProcessor tilesetProcessor;
    private ArrayList<Node> solution;
    private ArrayList<Node> opened; // to restore lookup steps.
    private int openedStepsAmount;

    public Canvas() throws IOException {
        super();
        super.setDoubleBuffered(true);
        tileSize = new Dimension();
        font = new Font("Arial", Font.TRUETYPE_FONT, 36);
        selectionColor = new Color(255, 255, 255, 90);
        solutionColor = new Color(87, 36, 255, 100);
        fontColor = new Color(0, 0, 0, 250);
        selection = new Point(-1, -1); // no selection by default.
        openedStepsAmount = 0;
    }

    public void loadTileset(String tilesetPath) {
        tilesetProcessor = new TilesetProcessor(tilesetPath);
        tiles = tilesetProcessor.splitIntoChunks(4, 6, 64, 64);
    }

    @Override
    public void paintComponent(Graphics G) {
        if (workField == null)
            return;

        clear(G);
        setBackground(Color.GRAY);
        Graphics2D g2d = (Graphics2D) G;
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        tileSize.setSize(getWidth() / workField.getCollsAmount(), getHeight() / workField.getRowsAmount());
        for (int i = 0; i < workField.getRowsAmount(); i++) {
            for (int j = 0; j < workField.getCollsAmount(); j++) {
                g2d.drawImage(tiles.get(workField.getCell(i, j).getType().getValue()),
                        i * tileSize.width, j * tileSize.height, tileSize.width, tileSize.height, null);
            }
        }
        if (solution != null && opened != null) {
            for (int i = 0; i < openedStepsAmount; i++) {
                g2d.setPaint(solutionColor);
                g2d.fillRect(opened.get(i).getX() * tileSize.width, opened.get(i).getY() * tileSize.height,
                        tileSize.width, tileSize.height);
            }

            for (int i = solution.size() - 1; i >= solution.size() - openedStepsAmount / 8; i--) { // reverse lookup
                g2d.setPaint(fontColor);
                g2d.drawString(String.valueOf(solution.size() - i),
                        solution.get(i).getX() * tileSize.width + (tileSize.width / 2 - 12),   // 12 is for (font size / 2)
                        solution.get(i).getY() * tileSize.height + (tileSize.height - 12));
            }
        }
        g2d.setPaint(selectionColor);
        if (selection.x >= 0)
            g2d.fillRect(selection.x * tileSize.width, selection.y * tileSize.height, tileSize.width, tileSize.height);
    }

    public Point getSelection() {
        return selection;
    }

    public void setWorkField(WorkField workField) {
        this.workField = workField;
    }

    private void clear(Graphics g) {
        super.paintComponent(g);
    }

    public TilesetProcessor getTilesetProcessor() {
        return tilesetProcessor;
    }

    public void setSolution(ArrayList<Node> solution) {
        this.solution = solution;
    }

    public void setOpened(ArrayList<Node> opened) {
        this.opened = opened;
    }

    public void setOpenedStepsAmount(int openedStepsAmount) {
        this.openedStepsAmount = openedStepsAmount;
    }
}