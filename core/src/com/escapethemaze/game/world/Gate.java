package com.escapethemaze.game.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.escapethemaze.game.AssetManager;
import java.util.Random;

public class Gate {
    private int angle;
    private boolean hasChest;
    private static final int CHEST_SPAWN_RATE = 80;
    
    public Gate(int angle, Random random) {
        this.angle = angle;
        this.hasChest = random.nextInt(CHEST_SPAWN_RATE) == 1;
    }
    
    public void render(ShapeRenderer renderer, float doorX, float doorY, float doorAngle, int cameraX, int cameraY, int tileSize) {
        float totalAngle = (float) Math.toRadians(angle + doorAngle);
        float nx = doorX + (float) Math.cos(totalAngle) * (tileSize / 2f);
        float ny = doorY + (float) Math.sin(totalAngle) * (tileSize / 2f);
        
        renderer.line(doorX + cameraX, doorY + cameraY, nx + cameraX, ny + cameraY);
    }
    
    public void renderChest(SpriteBatch batch, AssetManager assetManager, float doorX, float doorY, float doorAngle, int cameraX, int cameraY, int tileSize) {
        if (hasChest && assetManager.chestImage != null) {
            float totalAngle = (float) Math.toRadians(angle + doorAngle);
            float chestX = doorX + (float) Math.cos(totalAngle) * (tileSize / 3f);
            float chestY = doorY + (float) Math.sin(totalAngle) * (tileSize / 3f);
            int chestSize = tileSize / 6;
            batch.draw(assetManager.chestImage, chestX + cameraX - chestSize, chestY + cameraY - chestSize, chestSize * 2, chestSize * 2);
        }
    }
    
    public boolean hasChest() {
        return hasChest;
    }
    
    public int getAngle() {
        return angle;
    }
}
