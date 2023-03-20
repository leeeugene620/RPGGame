package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gamePanel;
    KeyHandler keyHandler;

    public final int screenX; //Where we draw player on the screen
    public final int screenY;
    public int hasKey = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize/2); // Adjusting centering b/c image drawn from top left corner
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize/2);

        hitBox = new Rectangle(8, 16, 31, 32);
        hitBoxDefaultX = hitBox.x;
        hitBoxDefaultY = hitBox.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        worldX = gamePanel.tileSize * 23; // Player POS in world map
        worldY = gamePanel.tileSize * 21;
        speed = 4;
        direction = "down"; // Doesn't matter which direction
    }

    public void getPlayerImage() {
        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
    }

    // Method belows helps to optimize the image instantiation before drawing
    public BufferedImage setup(String imageName) {

        UtilityTool utilityTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void update() {
        // Makes it so player sprite doesn't change unless key is pressed
        if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if (keyHandler.upPressed) {
                direction = "up";
            } else if (keyHandler.downPressed) {
                direction = "down";
            } else if (keyHandler.leftPressed) {
                direction = "left";
            } else if (keyHandler.rightPressed) {
                direction = "right";
            }

            // Checks Tile Collision
            collisionOn = false;
            gamePanel.collisionChecker.checkTile(this);

            // Checks Object Collision
            int  objIndex = gamePanel.collisionChecker.checkObj(this, true);
            pickUpObj(objIndex);

            // If collision is false then player is able to move
            if (!collisionOn) {

                switch(direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 15) { //Updates sprite every 1/6 of a second
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObj(int ind) {
        if (ind != 999) { // 999 is default if no item was touched/picked up

            String objectName = gamePanel.obj[ind].name;

            switch(objectName) {
                case "Key":
                    gamePanel.playSE(1);
                    hasKey++;
                    gamePanel.obj[ind] = null; // Makes object disappear 10/16/22 I spent like 2 hours on finding its replacing the entire array with null :|
                    gamePanel.ui.showMsg("Picked up a key! ");
                    break;
                case "Door":
                    if (hasKey > 0) {
                        gamePanel.playSE(3);
                        gamePanel.obj[ind] = null;
                        hasKey--;
                        gamePanel.ui.showMsg("Used a key and opened the door!");
                    } else {
                        gamePanel.ui.showMsg("You need a key to open this door!");
                    }
                    break;
                case "Boots":
                    gamePanel.playSE(2);
                    speed += 2;
                    gamePanel.obj[ind] = null;
                    gamePanel.ui.showMsg("You found speed-up boots!");
                    break;
                case "Chest":
                    gamePanel.stopMusic();
                    gamePanel.ui.gameFin = true;
                    gamePanel.playSE(4);
                    break;
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        //graphics2D.setColor(Color.white);
        //graphics2D.fillRect(x, y, gamePanel.tileSize, gamePanel.tileSize);

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) { // Changes sprite depending on the SpriteNum
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        graphics2D.drawImage(image, screenX, screenY, null);
    }
}
