/*
 * Copyright (c) 2013. Created by Alexander Voevodin [Alvo]
 */

package Graphics;

import Board.WorkField;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lux on 05.12.13.
 */
public class Canvas extends JPanel {

    private WorkField workField;
    private Dimension tileSize;
    private Font font;
    private ArrayList<BufferedImage> tiles;
    private Point underSelection;
    private Color selectionColor;

    public Canvas() throws IOException {
        super();
        super.setDoubleBuffered(true);
        tileSize = new Dimension();
        font = new Font("Arial", Font.TRUETYPE_FONT, 36);
        selectionColor = new Color(0, 0, 0, 100);
        underSelection = new Point(-1, -1);
    }

    public void loadTileTextures(String tilesetPath) {
        try {
            BufferedImage tileset = loadImage(new File(tilesetPath));
            tiles = TilesetProcessor.splitIntoChunks(tileset, workField.getCollsAmount(), workField.getRowsAmount(), 64, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage loadImage(File source) throws IOException {
        BufferedImage tmp = ImageIO.read(source);
        int width = tmp.getWidth();
        int height = tmp.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(tmp, 0, 0, null);
        return img;
    }

    @Override
    public void paintComponent(Graphics G) {
        if (workField == null)
            return;

        clear(G);
        setBackground(Color.GRAY);
        Graphics2D g2d = (Graphics2D) G;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setPaint(selectionColor);
        tileSize.setSize(getWidth() / workField.getCollsAmount(), getHeight() / workField.getRowsAmount());

        int emptyTileTextureIndex = 1;
        int obstacleTileTextureIndex = 11;
        int emitterTileTextureIndex = 12;

        for (int i = 0; i < workField.getRowsAmount(); i++) {
            for (int j = 0; j < workField.getCollsAmount(); j++) {
                switch (workField.getCell(i, j).getType()){
                    case FREE:{
                        g2d.drawImage(tiles.get(emptyTileTextureIndex),
                                i * tileSize.width, j * tileSize.height, tileSize.width, tileSize.height, null);
                        break;
                    }
                    case OBSTACLE:{
                        g2d.drawImage(tiles.get(obstacleTileTextureIndex),
                                i * tileSize.width, j * tileSize.height, tileSize.width, tileSize.height, null);
                        break;
                    }
                    case EMITTER:{
                        g2d.drawImage(tiles.get(emitterTileTextureIndex),
                                i * tileSize.width, j * tileSize.height, tileSize.width, tileSize.height, null);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        }
        if (underSelection.x >=0)
            g2d.fillRect(underSelection.x * tileSize.width, underSelection.y * tileSize.height, tileSize.width, tileSize.height);
    }

    public Point getUnderSelection() {
        return underSelection;
    }

    public void setWorkField(WorkField workField) {
        this.workField = workField;
    }

    private void clear(Graphics g) {
        super.paintComponent(g);
    }
}