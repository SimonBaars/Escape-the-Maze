package com.escapethemaze.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Mine {
    private EscapeTheMaze game;
    private float x, y;
    private boolean isActive = false;
    
    public Mine(EscapeTheMaze game) {
        this.game = game;
    }
    
    public void place(float x, float y) {
        this.x = x;
        this.y = y;
        this.isActive = true;
    }
    
    public void update(float delta) {
        if (!isActive) return;
        
        // Check if player is near mine
        float distance = (float)Math.sqrt(Math.pow(x - game.player.x + game.camx, 2) + 
                                         Math.pow(y - game.player.y + game.camy, 2));
        if (distance < game.tileSize / 2) {
            // Mine triggered!
            game.player.health -= 10;
            isActive = false;
        }
    }
    
    public void draw(SpriteBatch batch) {
        if (!isActive) return;
        
        if (game.mineImage != null) {
            batch.draw(game.mineImage, x + game.camx, y + game.camy, 
                game.tileSize / 6f, game.tileSize / 6f);
        }
    }
}
