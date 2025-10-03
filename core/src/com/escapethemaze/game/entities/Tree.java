package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.AssetManager;

public class Tree {
    private float x, y;
    private int size;
    private int state = 0; // 0 = full tree, 1 = damaged, 2 = stump
    private int createdTrees = 0;
    
    public Tree(float x, float y, int tileSize) {
        this.x = x;
        this.y = y;
        this.size = tileSize / 6;
        this.state = 0;
    }
    
    public void render(SpriteBatch batch, AssetManager assetManager, int cameraX, int cameraY) {
        if (assetManager.treeImages != null && state >= 0 && state < assetManager.treeImages.length) {
            Texture texture = assetManager.treeImages[state];
            if (texture != null) {
                batch.draw(texture, x + cameraX - size, y + cameraY - size, size * 2, size * 2);
            }
        }
    }
    
    public void update(float delta) {
        // Trees can regrow over time
        // TODO: Implement tree growth logic
    }
    
    public void chop() {
        if (state < 2) {
            state++;
        }
    }
    
    public boolean isChopped() {
        return state >= 2;
    }
}
