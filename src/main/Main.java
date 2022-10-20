package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Allows us to close the window
        window.setResizable(false); //Cannot resize the window
        window.setTitle("2D Game");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); //Causes this window to be sized to fit the preferred size and layouts of its subcomponents

        window.setLocationRelativeTo(null); //Sets window to the center of the screen
        window.setVisible(true); //So we can see this window

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
