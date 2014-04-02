/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

package Graphics;

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
    private WorkField workField;
    private Dimension tileSize;
    private ArrayList<BufferedImage> tiles;
    private TilesetProcessor tilesetProcessor;

    public Canvas() throws IOException {
        super();
        super.setDoubleBuffered(true);
        tileSize = new Dimension();
        font = new Font("Arial", Font.TRUETYPE_FONT, 36);
        selectionColor = new Color(255, 255, 255, 90);
        selection = new Point(-1, -1); // no selection by default.
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(selectionColor);
        tileSize.setSize(getWidth() / workField.getCollsAmount(), getHeight() / workField.getRowsAmount());
        for (int i = 0; i < workField.getRowsAmount(); i++) {
            for (int j = 0; j < workField.getCollsAmount(); j++) {
                g2d.drawImage(tiles.get(workField.getCell(i, j).getType().getValue()),
                        i * tileSize.width, j * tileSize.height, tileSize.width, tileSize.height, null);
            }
        }
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
}