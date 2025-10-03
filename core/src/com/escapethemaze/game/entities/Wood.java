package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.AssetManager;

public class Wood {
    private float x, y;
    private int size;
    private boolean isBurning = false;
    private boolean isCraftingTable = false;
    private int currentCycle = 0;
    private int currentImage = 0;
    
    public Wood(float x, float y, int tileSize) {
        this.x = x;
        this.y = y;
        this.size = tileSize / 6;
    }
    
    public void render(SpriteBatch batch, AssetManager assetManager, int cameraX, int cameraY) {
        if (isCraftingTable && assetManager.craftingTableImages != null && assetManager.craftingTableImages[0] != null) {
            batch.draw(assetManager.craftingTableImages[0], x + cameraX - size, y + cameraY - size, size * 2, size * 2);
        } else if (isBurning && assetManager.fireImages != null) {
            int frame = currentImage % assetManager.fireImages.length;
            if (assetManager.fireImages[frame] != null) {
                batch.draw(assetManager.fireImages[frame], x + cameraX - size, y + cameraY - size, size * 2, size * 2);
            }
        }
        // Draw wood block texture if not burning/crafting table
        // TODO: Add wood block texture
    }
    
    public void update(float delta) {
        if (isBurning) {
            currentCycle++;
            if (currentCycle % 5 == 0) {
                currentImage = (currentImage + 1) % 5;
            }
        }
    }
    
    public void lightOnFire() {
        isBurning = true;
    }
    
    public void makeCraftingTable() {
        isCraftingTable = true;
    }
    
    public boolean harvest() {
        // Return wood to player
        return true;
    }
}
