package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.AssetManager;
import java.util.Random;

public class Pig {
    private float x, y;
    private int size;
    private int[] animationFrames = new int[3];
    private Random random;
    
    private float wanderTimer = 0;
    private float wanderDirectionX = 0;
    private float wanderDirectionY = 0;
    private static final float WANDER_SPEED = 1.5f;
    private static final float WANDER_CHANGE_INTERVAL = 2.0f;
    
    public Pig(float x, float y, int tileSize) {
        this.x = x;
        this.y = y;
        this.size = tileSize / 6;
        this.random = new Random();
        
        animationFrames[0] = 2; // Direction
        animationFrames[1] = 0; // Frame
        animationFrames[2] = 0; // Counter
    }
    
    public void render(SpriteBatch batch, AssetManager assetManager, int cameraX, int cameraY) {
        if (assetManager.pigImages != null &&
            animationFrames[0] >= 0 && animationFrames[0] < assetManager.pigImages.length &&
            animationFrames[1] >= 0 && animationFrames[1] < assetManager.pigImages[0].length) {
            
            Texture texture = assetManager.pigImages[animationFrames[0]][animationFrames[1]];
            if (texture != null) {
                batch.draw(texture, x + cameraX - size, y + cameraY - size, size * 2, size * 2);
            }
        }
    }
    
    public void update(float delta) {
        wanderTimer += delta;
        
        if (wanderTimer >= WANDER_CHANGE_INTERVAL) {
            wanderTimer = 0;
            wanderDirectionX = (random.nextFloat() - 0.5f) * 2;
            wanderDirectionY = (random.nextFloat() - 0.5f) * 2;
            
            // Normalize
            float length = (float) Math.sqrt(wanderDirectionX * wanderDirectionX + wanderDirectionY * wanderDirectionY);
            if (length > 0) {
                wanderDirectionX /= length;
                wanderDirectionY /= length;
            }
        }
        
        x += wanderDirectionX * WANDER_SPEED;
        y += wanderDirectionY * WANDER_SPEED;
        
        // Update animation
        if (wanderDirectionX != 0 || wanderDirectionY != 0) {
            if (Math.abs(wanderDirectionX) > Math.abs(wanderDirectionY)) {
                animationFrames[0] = wanderDirectionX > 0 ? 1 : 3;
            } else {
                animationFrames[0] = wanderDirectionY > 0 ? 0 : 2;
            }
            
            animationFrames[2]++;
            if (animationFrames[2] >= 5) {
                animationFrames[2] = 0;
                animationFrames[1] = (animationFrames[1] + 1) % 4;
            }
        }
    }
}
