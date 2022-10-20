package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gamePanel;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        tile = new Tile[10]; // Each array has 10 kinds of tiles
        mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow]; //Will store numbers from map.txt

        getTileImage();
        loadMap("/maps/world01.txt");
    }

    public void getTileImage() {

        setup(0, "grass", false);
        setup(1, "wall", true);
        setup(2, "water", true);
        setup(3, "earth", false);
        setup(4, "tree", true);
        setup(5, "sand", false);
    }

    public void setup(int index, String imageName, boolean collision) {

        UtilityTool utilTool = new UtilityTool();
        // Easier set up of tile creations
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = utilTool.scaleImage(tile[index].image, gamePanel.tileSize, gamePanel.tileSize);
            tile[index].collision = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath){

        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {

                String line = bufferedReader.readLine();

                while (col < gamePanel.maxWorldCol) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gamePanel.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D graphics2D) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow]; // Gets the int (0, 1, 2) depending on what it is for mapTileNum

            int worldX = worldCol * gamePanel.tileSize; // Each tile is 48 pixels so it must increment by 48
            int worldY = worldRow * gamePanel.tileSize;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX; // WorldX - Player.WorldX gives reference of the tile to the player so if player was at 500, 500 the tile would be -500, -500
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY; // The addition offsets the distance to center the player. If player was at (0, 0) the player would still be in the center

            if (worldX + gamePanel.tileSize > (gamePanel.player.worldX - gamePanel.player.screenX) && // Makes it so the screen only renders what the player can see
                worldX - gamePanel.tileSize < (gamePanel.player.worldX + gamePanel.player.screenX) && // + tilesize so it fits the screen
                worldY + gamePanel.tileSize > (gamePanel.player.worldY - gamePanel.player.screenY) &&
                worldY - gamePanel.tileSize < (gamePanel.player.worldY + gamePanel.player.screenY)) {

                graphics2D.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            worldCol++;
            if (worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
