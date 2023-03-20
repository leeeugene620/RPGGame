package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class UI {

    GamePanel gamePanel;
    Font arial40, arial80B;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int msgCounter = 0;
    public boolean gameFin = false;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        arial40 = new Font("Arial", Font.PLAIN, 40);
        arial80B = new Font("Arial", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key(gamePanel);
        keyImage = key.image;
    }

    public void showMsg(String text) {
        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D graphics2D) {

        if (gameFin) {

            graphics2D.setFont(arial40);
            graphics2D.setColor(Color.white);

            String text;
            int textLen;
            int x;
            int y;

            text = "You found the treasure!";
            textLen = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth(); // Returns the len of text

            x = gamePanel.screenWidth / 2 - (textLen / 2); // To adjust centering it bc it starts from left side
            y = (gamePanel.screenHeight / 2) - (gamePanel.tileSize * 3);
            graphics2D.drawString(text, x, y);

            text = "Your Time is: " + dFormat.format(playTime) + "!";
            textLen = (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth(); // Returns the len of text
            x = (gamePanel.screenWidth / 2) - (textLen / 2); // To adjust centering it bc it starts from left side
            y = (gamePanel.screenHeight / 2) + (gamePanel.tileSize * 4);
            graphics2D.drawString(text, x, y);

            graphics2D.setFont(arial80B);
            graphics2D.setColor(Color.yellow);
            text = "Congratulations!";
            textLen = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
            x = gamePanel.screenWidth / 2 - (textLen / 2);
            y = gamePanel.screenHeight / 2 + (gamePanel.tileSize * 2);
            graphics2D.drawString(text, x, y);

            gamePanel.gameThread = null;
        } else {
            // Don't instantiate objects here because it goes in the game loop and makes the program lag
            graphics2D.setFont(arial40);
            graphics2D.setColor(Color.white);
            graphics2D.drawImage(keyImage, gamePanel.tileSize / 2, gamePanel.tileSize / 2, gamePanel.tileSize, gamePanel.tileSize, null);
            graphics2D.drawString("= " + gamePanel.player.hasKey, 74, 65); // Draw string indicates bottom of image not top like drawImage

            // Time
            playTime += (double) 1/60;
            graphics2D.drawString("Time: " + dFormat.format(playTime), gamePanel.tileSize * 11, 65);

            // Message
            if (messageOn) {
                graphics2D.setFont(graphics2D.getFont().deriveFont(30F));
                graphics2D.drawString(message, gamePanel.tileSize / 2, gamePanel.tileSize * 5);
                msgCounter++;

                if (msgCounter > 120) { // 60 FPS so 2 seconds
                    msgCounter = 0;
                    messageOn = false;
                }
            }
        }


    }
}
