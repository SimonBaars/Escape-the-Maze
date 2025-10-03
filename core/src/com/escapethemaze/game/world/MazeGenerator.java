package com.escapethemaze.game.world;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private int numOfColumns;
    private int tileSize;
    private int gladeSize;
    private List<Line> lines;
    private List<Door> doors;
    private Random random;
    
    public MazeGenerator(int numOfColumns, int tileSize, int gladeSize) {
        this.numOfColumns = numOfColumns;
        this.tileSize = tileSize;
        this.gladeSize = gladeSize;
        this.lines = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.random = new Random();
    }
    
    public void generate() {
        lines.clear();
        doors.clear();
        
        // Generate outer walls
        for (int a = 1; a < numOfColumns; a++) {
            lines.add(new Line(a * tileSize, tileSize, tileSize, 0, true, false, true));
            lines.add(new Line(a * tileSize, numOfColumns * tileSize, tileSize, 0, true, false, true));
            lines.add(new Line(tileSize, a * tileSize, 0, tileSize, false, false, true));
            lines.add(new Line(numOfColumns * tileSize, a * tileSize, 0, tileSize, false, false, true));
            
            // Generate interior maze
            for (int b = 1; b < numOfColumns; b++) {
                if (isInGlade(a, b)) {
                    if (b == (numOfColumns / 2) - gladeSize) {
                        lines.add(new Line(a * tileSize, b * tileSize, tileSize, 0, true, a == numOfColumns / 2, false));
                        lines.add(new Line(a * tileSize, tileSize + (b + (2 * gladeSize)) * tileSize, tileSize, 0, true, a == numOfColumns / 2, false));
                        lines.add(new Line(b * tileSize, a * tileSize, 0, tileSize, false, a == numOfColumns / 2, false));
                        lines.add(new Line(tileSize + (b + (2 * gladeSize)) * tileSize, a * tileSize, 0, tileSize, false, a == numOfColumns / 2, false));
                    }
                } else {
                    doors.add(new Door(a * tileSize + (tileSize / 2), b * tileSize + (tileSize / 2), tileSize, random));
                }
            }
        }
    }
    
    private boolean isInGlade(int a, int b) {
        int center = numOfColumns / 2;
        return Math.abs(a - center) <= gladeSize && Math.abs(b - center) <= gladeSize;
    }
    
    public void render(ShapeRenderer renderer, int cameraX, int cameraY) {
        for (Line line : lines) {
            line.render(renderer, cameraX, cameraY);
        }
        
        for (Door door : doors) {
            door.render(renderer, cameraX, cameraY);
        }
    }
    
    public void update(float delta) {
        for (Door door : doors) {
            door.update(delta);
        }
    }
    
    public List<Line> getLines() {
        return lines;
    }
    
    public List<Door> getDoors() {
        return doors;
    }
}
