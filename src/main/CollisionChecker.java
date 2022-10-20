package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {

        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.hitBox.x; // The hitbox/collision target respective to the world the left of the collision box
        int entityRightWorldX = entity.worldX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.worldY + entity.hitBox.y;
        int entityBotWorldY = entity.worldY + entity.hitBox.y + entity.hitBox.height;

        int entityLeftCol = entityLeftWorldX / gamePanel.tileSize; // The column # corresponding to the X of the left of the box
        int entityRightCol = entityRightWorldX / gamePanel.tileSize;
        int entityTopRow = entityTopWorldY / gamePanel.tileSize;
        int entityBotRow = entityBotWorldY / gamePanel.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gamePanel.tileSize; // Predicts where player will be after he moves to do collision check and figure out what tile the player is trying to get to
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBotRow = (entityBotWorldY + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBotRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBotRow];
                if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBotRow];
                if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBotRow];
                if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObj(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < gamePanel.obj.length; i++) {
            if (gamePanel.obj[i] != null) {
                // Get entity's hitbox position/location
                entity.hitBox.x = entity.worldX + entity.hitBox.x;
                entity.hitBox.y = entity.worldY + entity.hitBox.y;

                // Get object's hitbox position/location
                gamePanel.obj[i].hitBox.x = gamePanel.obj[i].worldX + gamePanel.obj[i].hitBox.x;
                gamePanel.obj[i].hitBox.y = gamePanel.obj[i].worldY + gamePanel.obj[i].hitBox.y;

                switch(entity.direction) {
                    case "up":
                        entity.hitBox.y -= entity.speed;
                        // Automatically checks if these two objects are touching
                        if (entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                            if (gamePanel.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i; // Only players can pick up items
                            }
                        }
                        break;
                    case "down":
                        entity.hitBox.y += entity.speed;
                        if (entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                            if (gamePanel.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.hitBox.x -= entity.speed;
                        if (entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                            if (gamePanel.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.hitBox.x += entity.speed;
                        if (entity.hitBox.intersects(gamePanel.obj[i].hitBox)) {
                            if (gamePanel.obj[i].collision) {
                                entity.collisionOn = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                        break;
                }
                entity.hitBox.x = entity.hitBoxDefaultX;
                entity.hitBox.y = entity.hitBoxDefaultY;
                gamePanel.obj[i].hitBox.x = gamePanel.obj[i].hitBoxDefaultX;
                gamePanel.obj[i].hitBox.y = gamePanel.obj[i].hitBoxDefaultY;
            }
        }

        return index;
    }
}
