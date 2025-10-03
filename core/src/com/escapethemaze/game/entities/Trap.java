package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.AssetManager;

public class Trap {
    private float x, y;
    private int size;
    private boolean active = true;
    
    public Trap(float x, float y, int tileSize) {
        this.x = x;
        this.y = y;
        this.size = tileSize / 6;
    }
    
    public void render(SpriteBatch batch, AssetManager assetManager, int cameraX, int cameraY) {
        if (active && assetManager.trapImage != null) {
            batch.draw(assetManager.trapImage, x + cameraX - size, y + cameraY - size, size * 2, size * 2);
        }
    }
    
    public boolean checkCollision(float px, float py, float psize) {
        float dist = (float) Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
        return active && dist < (size + psize);
    }
    
    public void trigger() {
        active = false;
    }
}
