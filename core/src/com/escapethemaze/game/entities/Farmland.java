package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Farmland {
    private EscapeTheMaze game;
    
    public Farmland(EscapeTheMaze game) {
        this.game = game;
    }
    
    public void update(float delta) {
        // Update farmland crops, growth, etc.
    }
    
    public void draw(SpriteBatch batch, float camx, float camy) {
        // Draw farmland and crops
    }
}
