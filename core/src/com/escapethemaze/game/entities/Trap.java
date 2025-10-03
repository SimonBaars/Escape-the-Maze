package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Trap {
    public float x, y;
    float damage = 2;
    
    private EscapeTheMaze game;
    
    public Trap(float x, float y, EscapeTheMaze game) {
        this.x = x;
        this.y = y;
        this.game = game;
    }
    
    public void draw(SpriteBatch batch, float camx, float camy) {
        if (game.trapImage != null) {
            batch.draw(game.trapImage, x + camx, y + camy, 
                game.tileSize / 6f, game.tileSize / 6f);
        }
    }
    
    public void checkPlayerCollision() {
        float distance = (float)Math.sqrt(Math.pow(x - game.player.x + game.camx, 2) + 
                                         Math.pow(y - game.player.y + game.camy, 2));
        if (distance < game.tileSize / 4) {
            game.player.health -= damage;
        }
    }
}
