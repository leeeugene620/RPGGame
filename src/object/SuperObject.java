package object;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

// Parent Class for ALL objects
public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle hitBox = new Rectangle(0, 0, 48, 48);
    public int hitBoxDefaultX = 0;
    public int hitBoxDefaultY = 0;
    // Method belows helps to optimize the image instantiation before drawing
    UtilityTool utilityTool = new UtilityTool();

    public void draw(Graphics2D graphics2D, GamePanel gamePanel) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX; // WorldX - Player.WorldX gives reference of the tile to the player so if player was at 500, 500 the tile would be -500, -500
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY; // The addition offsets the distance to center the player. If player was at (0, 0) the player would still be in the center

        if (worldX + gamePanel.tileSize > (gamePanel.player.worldX - gamePanel.player.screenX) && // Makes it so the screen only renders what the player can see
                worldX - gamePanel.tileSize < (gamePanel.player.worldX + gamePanel.player.screenX) && // + tilesize so it fits the screen
                worldY + gamePanel.tileSize > (gamePanel.player.worldY - gamePanel.player.screenY) &&
                worldY - gamePanel.tileSize < (gamePanel.player.worldY + gamePanel.player.screenY)) {

            graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }
}
