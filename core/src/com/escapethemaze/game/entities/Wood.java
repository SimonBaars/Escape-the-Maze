package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Wood {
    public float x, y;
    
    private EscapeTheMaze game;
    
    public Wood(float x, float y, EscapeTheMaze game) {
        this.x = x;
        this.y = y;
        this.game = game;
        game.woodBlocks.add(this);
    }
    
    public void draw(SpriteBatch batch, float camx, float camy) {
        if (game.chestImage != null) {
            batch.draw(game.chestImage, x + camx, y + camy, 
                game.tileSize / 6f, game.tileSize / 6f);
        }
    }
    
    public boolean harvest() {
        float distance = (float)Math.sqrt(Math.pow(x - game.player.x + game.camx, 2) + 
                                         Math.pow(y - game.player.y + game.camy, 2));
        return distance < game.tileSize / 2;
    }
}
