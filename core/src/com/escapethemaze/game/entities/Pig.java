package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Pig {
    public float x, y;
    int walkingProgress = 0;
    int speed = 30;
    int[] animationFrames = new int[2];
    boolean followPlayer = false;
    int wheatEaten = 0;
    
    private EscapeTheMaze game;
    
    public Pig(int x, int y, EscapeTheMaze game) {
        this.x = x;
        this.y = y;
        this.game = game;
        animationFrames[0] = 0;
        animationFrames[1] = (int)(Math.random() * 4);
    }
    
    public void draw(SpriteBatch batch, float camx, float camy) {
        if (animationFrames[0] >= 0 && animationFrames[0] < game.pigImage.length &&
            animationFrames[1] >= 0 && animationFrames[1] < game.pigImage[0].length) {
            Texture texture = game.pigImage[animationFrames[0]][animationFrames[1]];
            float drawX = x + camx;
            float drawY = y + camy;
            if (animationFrames[0] == 0 || animationFrames[0] == 1) {
                batch.draw(texture, drawX, drawY, game.tileSize / 3f, game.tileSize / 6f);
            } else {
                batch.draw(texture, drawX, drawY, game.tileSize / 6f, game.tileSize / 3f);
            }
        }
    }
    
    public void update(float delta) {
        wander();
    }
    
    private void wander() {
        if (walkingProgress == 0) {
            animationFrames[0] = (int)(Math.random() * 4);
        }
        
        boolean canMove = true;
        if (animationFrames[0] == 0) {
            if (isInGlade((int)(x - (game.tileSize / 3f) / speed), (int)y)) {
                x -= (game.tileSize / 3f) / speed;
            } else {
                walkingProgress = 0;
                canMove = false;
            }
        } else if (animationFrames[0] == 1) {
            if (isInGlade((int)(x + (game.tileSize / 3f) / speed), (int)y)) {
                x += (game.tileSize / 3f) / speed;
            } else {
                walkingProgress = 0;
                canMove = false;
            }
        } else if (animationFrames[0] == 2) {
            if (isInGlade((int)x, (int)(y - (game.tileSize / 3f) / speed))) {
                y -= (game.tileSize / 3f) / speed;
            } else {
                walkingProgress = 0;
                canMove = false;
            }
        } else if (animationFrames[0] == 3) {
            if (isInGlade((int)x, (int)(y + (game.tileSize / 3f) / speed))) {
                y += (game.tileSize / 3f) / speed;
            } else {
                walkingProgress = 0;
                canMove = false;
            }
        }
        
        if (canMove) {
            walkingProgress++;
            if (walkingProgress >= speed) {
                walkingProgress = 0;
                animationFrames[1]++;
                if (animationFrames[1] >= 4) {
                    animationFrames[1] = 0;
                }
            }
        }
    }
    
    private boolean isInGlade(int checkX, int checkY) {
        int tileX = checkX / game.tileSize;
        int tileY = checkY / game.tileSize;
        return isCloseTo(tileX, game.gladeSize - 1, game.numOfColumns / 2) && 
               isCloseTo(tileY, game.gladeSize - 1, game.numOfColumns / 2);
    }
    
    private boolean isCloseTo(int number, int howCloseTo, int anotherNumber) {
        return Math.abs(number - anotherNumber) <= howCloseTo;
    }
    
    public boolean feed() {
        float distance = (float)Math.sqrt(Math.pow(x + game.camx - game.player.x, 2) + 
                                         Math.pow(y + game.camy - game.player.y, 2));
        if (distance < game.tileSize) {
            wheatEaten++;
            followPlayer = true;
            return true;
        }
        return false;
    }
}
