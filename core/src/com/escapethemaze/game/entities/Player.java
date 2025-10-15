package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.AssetManager;
import java.util.ArrayList;
import java.util.List;

public class Player {
    public float x, y;
    public int size;
    public float health = 100;
    public float maxHealth = 100;
    public float currentFood;
    public int maxFood = 12000; // dayLength * 2
    
    private int movingSpeed;
    private int runningSpeed;
    private int[] animationFrames = new int[3]; // direction, frame, counter
    private int animationSpeed = 5;
    private boolean canRun = false;
    
    private List<Item> inventory;
    private int selectedItem = 0;
    private String[] inventoryStrings;
    
    private static final int WALL_COLOR = 0x525252;
    private static final int AMOUNT_OF_ITEMS = 21;
    
    public Player(float x, float y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        
        this.inventory = new ArrayList<>();
        this.inventoryStrings = new String[AMOUNT_OF_ITEMS];
        this.currentFood = maxFood;
        
        // Initialize animation
        animationFrames[0] = 2; // Direction (0-3)
        animationFrames[1] = 0; // Frame
        animationFrames[2] = 0; // Counter
        
        // Calculate speeds based on tile size (assuming tileSize ~180)
        movingSpeed = size / 3; // Roughly tileSize/40 when size = tileSize/6
        runningSpeed = movingSpeed * 2;
    }
    
    public void render(SpriteBatch batch, AssetManager assetManager, int cameraX, int cameraY) {
        if (assetManager.playerImages != null && 
            animationFrames[0] >= 0 && animationFrames[0] < assetManager.playerImages.length &&
            animationFrames[1] >= 0 && animationFrames[1] < assetManager.playerImages[0].length) {
            
            Texture texture = assetManager.playerImages[animationFrames[0]][animationFrames[1]];
            if (texture != null) {
                batch.draw(texture, x + cameraX - size, y + cameraY - size, size * 2, size * 2);
            }
        }
    }
    
    public void move(float dirX, float dirY) {
        if (dirX == 0 && dirY == 0) {
            return;
        }
        
        // Normalize diagonal movement
        float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (length > 0) {
            dirX /= length;
            dirY /= length;
        }
        
        int speed = canRun ? runningSpeed : movingSpeed;
        
        x += dirX * speed;
        y += dirY * speed;
        
        // Update animation direction
        if (Math.abs(dirX) > Math.abs(dirY)) {
            if (dirX > 0) {
                animationFrames[0] = 1; // Right
            } else {
                animationFrames[0] = 3; // Left
            }
        } else {
            if (dirY > 0) {
                animationFrames[0] = 0; // Up
            } else {
                animationFrames[0] = 2; // Down
            }
        }
        
        // Update animation frame
        animationFrames[2]++;
        if (animationFrames[2] >= animationSpeed) {
            animationFrames[2] = 0;
            animationFrames[1] = (animationFrames[1] + 1) % 4;
        }
    }
    
    public void update(float delta) {
        // Food consumption is handled in GameScreen
    }
    
    public void feed(int amount) {
        currentFood += amount;
        if (currentFood > maxFood) {
            currentFood = maxFood;
        }
    }
    
    public void addToInventory(Item item) {
        inventory.add(item);
    }
    
    public void removeFromInventory(int index) {
        if (index >= 0 && index < inventory.size()) {
            inventory.remove(index);
        }
    }
    
    public Item getSelectedItem() {
        if (selectedItem >= 0 && selectedItem < inventory.size()) {
            return inventory.get(selectedItem);
        }
        return null;
    }
    
    public boolean isInGlade(int numOfColumns, int gladeSize, int tileSize, int cameraX, int cameraY) {
        int tileX = (int)((x - cameraX) / tileSize);
        int tileY = (int)((y - cameraY) / tileSize);
        int center = numOfColumns / 2;
        
        return Math.abs(tileX - center) <= gladeSize && Math.abs(tileY - center) <= gladeSize;
    }
}
