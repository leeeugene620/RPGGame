package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //This class works as a game screen
    //Screen settings
    final int originalTileSize = 16;  // 16 x 16 tiles, default size for npc, player character, etc
    final int scale = 3; //Scales pixel images for higher resolution screens

    public final int tileSize = originalTileSize * scale; // 48 x 48 tiles displayed on the game screen
    public int maxScreenCol = 16; //16 tiles vertically
    public int maxScreenRow = 12; //12 tiles horizontally
    public int screenWidth = tileSize * maxScreenCol; // 768 Pixels wide
    public int screenHeight = tileSize * maxScreenRow; // 576 Pixels tall

    // World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;

    // System
    TileManager tileManager = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Sound se = new Sound();
    Sound music = new Sound();
    public AssetSetter assetSetter = new AssetSetter(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public UI ui = new UI(this);
    Thread gameThread; //Needs to implement Runnable


    // Entity and Objects
    public Player player = new Player(this, keyHandler);

    public SuperObject obj[] = new SuperObject[10]; // Displays up to 10 obj at the same time, but are replaceable in the array

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Screen dimensions
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // All the drawing from this component will be done offscreen, better rendering performance
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() { // Creating method to add other objects later, call before running game

        assetSetter.setObj();

        playMusic(0);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); //Game clock
    }

    // "Sleep" method Game loop
    /*
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS; // 1 billion b/c we use nanoseconds. 1 sec = 1000000000 nanoseconds || 0.1666 seconds per frame
        double nextDrawTime = System.nanoTime() + drawInterval;

        //Game loop (Core of our game)
        while (gameThread != null) {

            // 1 Update: Update information such as character position
            update();

            // 2 Draw: Draw the screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000; // Changes nanoseconds to milliseconds

                if (remainingTime < 0) {
                    // If the update and repaint took longer than the drawInterval
                    // Unlikely in this 2D game, but still worth just in case
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime); //Pauses game loop until sleep time is over (However, sleep accepts millisec)
                // Apparently isn't too accurate and adds a few milliseconds

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }



        }

    } */

    // "Delta/Accumulator" method
    public void run() {

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval; // Curr - Last == Time Elapsed
            timer += currentTime - lastTime;
            lastTime = currentTime;

            // Draws if delta is greater than 1 and resets after
            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }

    }

    @SuppressWarnings("checkstyle:SimplifyBooleanExpression")
    public void update() {

        player.update();
    }

    public void paintComponent(Graphics graphics) {

        super.paintComponent(graphics); // Must write this line everytime you create the paintComponent method b/c it's already in Java

        Graphics2D graphics2D = (Graphics2D) graphics; // Graphics2D has specific functions we want to use

        // Debug
        long drawStart = 0;
        if(keyHandler.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // Tiles
        tileManager.draw(graphics2D);

        // Objects
        for (SuperObject superObject : obj) {
            if (superObject != null) {
                superObject.draw(graphics2D, this);
            }
        }

        // Player
        player.draw(graphics2D);

        // UI
        ui.draw(graphics2D);

        // Debug
        if (keyHandler.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            graphics2D.setColor(Color.white);
            graphics2D.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed); // Checks how much time has passed
        }


        graphics2D.dispose(); // Dispose of this graphics context and save memory
    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {

        music.stop();
    }

    public void playSE(int i) {

        se.setFile(i);
        se.play();
    }
}
