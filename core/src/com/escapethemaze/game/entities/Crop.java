package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.AssetManager;

public class Crop {
    private float x, y;
    private int size;
    private int state = 0; // 0-7 growth stages
    private int type = 0;  // Type of crop
    
    public Crop(float x, float y, int tileSize) {
        this.x = x;
        this.y = y;
        this.size = tileSize / 6;
    }
    
    public void render(SpriteBatch batch, AssetManager assetManager, int cameraX, int cameraY) {
        // TODO: Load wheat textures and render based on state
        // Wheat textures are wheat00.png through wheat17.png (2 types x 8 states)
    }
    
    public void update(float delta) {
        // Crops grow over time
    }
    
    public void grow() {
        if (state < 7) {
            state++;
        }
    }
    
    public boolean isFullyGrown() {
        return state >= 7;
    }
    
    public void harvest() {
        state = 0;
    }
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
}
