package com.escapethemaze.game.world;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Line {
    private int x, y;
    private int sizeX, sizeY;
    private int targetSizeX, targetSizeY;
    private boolean type; // true = horizontal, false = vertical
    private boolean isDoor;
    private boolean isOutsideWall;
    
    public Line(int x, int y, int sizeX, int sizeY, boolean type, boolean isDoor, boolean isOutsideWall) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.targetSizeX = sizeX;
        this.targetSizeY = sizeY;
        this.type = type;
        this.isDoor = isDoor;
        this.isOutsideWall = isOutsideWall;
    }
    
    public void render(ShapeRenderer renderer, int cameraX, int cameraY) {
        renderer.line(x + cameraX, y + cameraY, x + sizeX + cameraX, y + sizeY + cameraY);
    }
    
    public void update() {
        if (targetSizeX != sizeX) {
            sizeX += (targetSizeX > sizeX) ? 1 : -1;
        }
        if (targetSizeY != sizeY) {
            sizeY += (targetSizeY > sizeY) ? 1 : -1;
        }
    }
    
    public boolean checkCollision(float px, float py, float size) {
        // Simple line collision detection
        // TODO: Implement proper collision
        return false;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }
    public boolean isType() { return type; }
    public boolean isDoor() { return isDoor; }
    public boolean isOutsideWall() { return isOutsideWall; }
}
