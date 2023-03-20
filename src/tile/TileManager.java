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

        tile = new Tile[50]; // Each array has 50 kinds of tiles
        mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow]; //Will store numbers from map.txt

        getTileImage();
        loadMap("/maps/worldV2.txt");
    }

    public void getTileImage() {

        //Placeholders
        setup(0, "grass00", false); // We keep 0-9 indices empty and keep the placeholder image to prevent NullPointer exception
        setup(1,"grass00", false); // when we scan the array.
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4,"grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);

        //Used Tiles
        setup(10, "grass00", false);
        setup(11, "grass01", false);
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);
        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);
        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41, "tree", true);

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
