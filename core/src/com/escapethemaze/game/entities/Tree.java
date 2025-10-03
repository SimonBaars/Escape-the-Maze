package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Tree {
    public float x, y;
    int type;
    int wood = 3;
    
    private EscapeTheMaze game;
    
    public Tree(float x, float y, int type, EscapeTheMaze game) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.game = game;
    }
    
    public void draw(SpriteBatch batch, float camx, float camy) {
        if (type >= 0 && type < game.treeImages.length) {
            Texture texture = game.treeImages[type];
            batch.draw(texture, x + camx, y + camy, game.tileSize / 6f, game.tileSize / 6f);
        }
    }
    
    public void update(float delta) {
        // Trees can be updated for growth, seasons, etc.
    }
    
    public boolean harvest() {
        float distance = (float)Math.sqrt(Math.pow(x - game.player.x + game.camx, 2) + 
                                         Math.pow(y - game.player.y + game.camy, 2));
        if (distance < game.tileSize / 2) {
            wood--;
            if (wood <= 0) {
                return true; // Tree should be removed
            }
        }
        return false;
    }
}
