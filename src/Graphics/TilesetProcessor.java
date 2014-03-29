package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Lux on 29.03.2014.
 */
public final class TilesetProcessor {
    public static ArrayList<BufferedImage> splitIntoChunks(BufferedImage tileset,
                                                           int rows, int colls, int chunkWidth, int chunkHeight) {
        ArrayList<BufferedImage> chunks = new ArrayList<>();
        BufferedImage chunk;
        Graphics2D g2d;
        int chunkRowPos, chunkCollPos;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colls; j++) {
                chunk = new BufferedImage(chunkWidth, chunkHeight, tileset.getType());
                chunkCollPos = j * chunkWidth + chunkWidth;
                chunkRowPos = i * chunkHeight + chunkHeight;
                g2d = chunk.createGraphics();
                g2d.drawImage(tileset, 0, 0, chunkWidth, chunkHeight,
                                       j * chunkWidth, i * chunkHeight,
                                       chunkCollPos, chunkRowPos, null);
                g2d.dispose();
                chunks.add(chunk);
            }
        }
        return chunks;
    }
}
