package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    // For convenient functions
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType()); //Blank Canvas to draw on
        Graphics2D graphics2D = scaledImage.createGraphics(); // Creates a G2D which can be used to draw into this BI
        graphics2D.drawImage(original, 0, 0, width, height, null);
        graphics2D.dispose();

        return scaledImage;
    }
}
