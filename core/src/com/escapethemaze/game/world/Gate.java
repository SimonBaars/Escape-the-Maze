package com.escapethemaze.game.world;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        
        // TODO: Render chest if hasChest is true
    }
    
    public boolean hasChest() {
        return hasChest;
    }
    
    public int getAngle() {
        return angle;
    }
}
