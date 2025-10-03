package com.escapethemaze.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Tutorial {
    private EscapeTheMaze game;
    private boolean[] shown;
    private int currentStage = 0;
    private String[] messages;
    
    public Tutorial(EscapeTheMaze game, int totalStages) {
        this.game = game;
        this.shown = new boolean[totalStages];
        this.messages = new String[totalStages];
        
        // Initialize tutorial messages
        messages[0] = "Welcome to Escape the Maze!";
        messages[1] = "Use arrow keys or WASD to move";
        messages[2] = "Collect food to survive";
        messages[3] = "Avoid the maze at night!";
        messages[4] = "Find the exit to escape!";
    }
    
    public void update() {
        // Tutorial logic
        if (currentStage < shown.length && !shown[currentStage]) {
            // Show current tutorial message
        }
    }
    
    public void draw(SpriteBatch batch) {
        if (currentStage < shown.length && !shown[currentStage]) {
            // Draw tutorial message
            if (game.font != null && messages[currentStage] != null) {
                int width = (int)game.viewport.getWorldWidth();
                int height = (int)game.viewport.getWorldHeight();
                game.font.draw(batch, messages[currentStage], width / 2 - 100, height - 50);
            }
        }
    }
    
    public void nextStage() {
        if (currentStage < shown.length) {
            shown[currentStage] = true;
            currentStage++;
        }
    }
    
    public void reset() {
        currentStage = 0;
        for (int i = 0; i < shown.length; i++) {
            shown[i] = false;
        }
    }
}
