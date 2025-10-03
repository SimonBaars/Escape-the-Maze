package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

import java.util.ArrayList;

public class Player {
    public float x, y;
    public int size;
    public float health = 100;
    public int movingSpeed;
    public int runningSpeed;
    public final int wallColor = 0x525252;
    public final int amountOfItems = 21;
    public ArrayList<Item> inventory = new ArrayList<Item>();
    public int currentFood;
    public int selectedItem = 0;
    public int[] animationFrames = new int[3];
    public final int animationSpeed = 5;
    public boolean canRun = false;
    public String[] inventoryStrings = new String[amountOfItems];
    
    private EscapeTheMaze game;
    
    public Player(int x, int y, int size, EscapeTheMaze game) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.game = game;
        
        this.movingSpeed = game.tileSize / 40;
        this.runningSpeed = movingSpeed * 2;
        this.currentFood = game.maxFood;
        
        animationFrames[0] = 2;
        animationFrames[1] = 0;
        animationFrames[2] = 0;
        
        // Initialize inventory
        Item testItem = new Item(amountOfItems, false, game);
        for (int i = 0; i < inventoryStrings.length; i++) {
            inventoryStrings[i] = "0x " + testItem.getItemName(i);
        }
        inventory.add(new Item(12, true, game));
    }
    
    public void draw(SpriteBatch batch, float camx, float camy) {
        if (animationFrames[0] >= 0 && animationFrames[0] < game.playerImages.length &&
            animationFrames[1] >= 0 && animationFrames[1] < game.playerImages[0].length) {
            Texture texture = game.playerImages[animationFrames[0]][animationFrames[1]];
            batch.draw(texture, 
                x - (game.tileSize / 12f), 
                y - (game.tileSize / 12f),
                game.tileSize / 6f, 
                game.tileSize / 6f);
        }
    }
    
    public void update(float delta) {
        checkOutOfScreen();
        currentFood--;
    }
    
    public void moveTo(int moveX, int moveY) {
        float moveToX = x + moveX;
        float moveToY = y + moveY;
        
        int movementAngle = (int)getAngle(x, y, moveToX, moveToY);
        
        // Update animation based on movement direction
        if (movementAngle == 0) {
            animationFrames[0] = 3; // Up
        } else if (movementAngle == 90) {
            animationFrames[0] = 1; // Right
        } else if (movementAngle == 180) {
            animationFrames[0] = 2; // Down
        } else if (movementAngle == 270) {
            animationFrames[0] = 0; // Left
        }
        
        // Update animation frame
        int movementDistance = (int)Math.sqrt(moveX * moveX + moveY * moveY);
        animationFrames[2] += (movementDistance / movingSpeed);
        if (animationFrames[2] >= animationSpeed) {
            animationFrames[2] = 0;
            animationFrames[1]++;
            if (animationFrames[1] >= 4) {
                animationFrames[1] = 0;
            }
        }
        
        // Move player
        x += moveX;
        y += moveY;
        
        // Update camera
        checkOutOfScreen();
    }
    
    private void checkOutOfScreen() {
        int width = (int)game.viewport.getWorldWidth();
        int height = (int)game.viewport.getWorldHeight();
        
        if (x < width / game.borderSize) {
            game.camx -= x - (width / game.borderSize);
            x = width / game.borderSize;
        }
        if (x > width - width / game.borderSize) {
            game.camx += x - (width - width / game.borderSize);
            x = width - width / game.borderSize;
        }
        if (y < height / game.borderSize) {
            game.camy -= y - (height / game.borderSize);
            y = height / game.borderSize;
        }
        if (y > height - height / game.borderSize) {
            game.camy += y - (height - height / game.borderSize);
            y = height - height / game.borderSize;
        }
        
        // Check if out of maze bounds
        if (x - game.camx > game.numOfColumns * game.tileSize || 
            y - game.camy > game.numOfColumns * game.tileSize || 
            x - game.camx < game.tileSize || 
            y - game.camy < game.tileSize) {
            game.notDrawn = true;
            game.scherm = 3; // Out of bounds screen
        }
    }
    
    private float getAngle(float x1, float y1, float x2, float y2) {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        angle = (angle + 360 + 90) % 360;
        
        // Snap to 90 degree angles
        if (angle < 45 || angle >= 315) return 0;
        else if (angle < 135) return 90;
        else if (angle < 225) return 180;
        else return 270;
    }
    
    public void useSelectedItem() {
        for (Item item : inventory) {
            if (item.id == selectedItem) {
                item.use();
                break;
            }
        }
    }
    
    public void feed(int amount) {
        currentFood += amount;
        if (currentFood > game.maxFood) {
            currentFood = game.maxFood;
        }
    }
}
