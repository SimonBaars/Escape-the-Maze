package com.escapethemaze.game.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.escapethemaze.game.AssetManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Door {
    private float x, y;
    private int angle;
    private int targetAngle;
    private int tileSize;
    private List<Gate> gates;
    
    public Door(float x, float y, int tileSize, Random random) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.gates = new ArrayList<>();
        
        this.angle = (random.nextInt(4)) * 90;
        this.targetAngle = this.angle;
        
        // Create gates (doors can have 1-3 gates)
        int gateCount = 0;
        for (int a = 0; a < 4; a++) {
            if ((gateCount < 3 && random.nextFloat() < 0.55f) || (gateCount == 0 && a == 3)) {
                gateCount++;
                gates.add(new Gate(a * 90, random));
            }
        }
    }
    
    public void render(ShapeRenderer renderer, int cameraX, int cameraY) {
        for (Gate gate : gates) {
            gate.render(renderer, x, y, angle, cameraX, cameraY, tileSize);
        }
    }
    
    public void renderChests(SpriteBatch batch, AssetManager assetManager, int cameraX, int cameraY) {
        for (Gate gate : gates) {
            gate.renderChest(batch, assetManager, x, y, angle, cameraX, cameraY, tileSize);
        }
    }
    
    public void update(float delta) {
        if (targetAngle != angle) {
            if (angle > targetAngle) {
                angle--;
            } else {
                angle++;
            }
        }
    }
    
    public void rotate(int degrees) {
        targetAngle = (targetAngle + degrees) % 360;
    }
    
    public float getX() { return x; }
    public float getY() { return y; }
}
