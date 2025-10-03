package com.escapethemaze.game.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.escapethemaze.game.EscapeTheMaze;

public class Shop {
    private EscapeTheMaze game;
    private boolean isOpen = false;
    
    public Shop(EscapeTheMaze game) {
        this.game = game;
    }
    
    public void open() {
        isOpen = true;
    }
    
    public void close() {
        isOpen = false;
    }
    
    public void update(float delta) {
        if (!isOpen) return;
        
        // Shop logic
    }
    
    public void draw(SpriteBatch batch) {
        if (!isOpen) return;
        
        // Draw shop UI
        if (game.craftingTable != null && game.craftingTable.length > 1 && game.craftingTable[1] != null) {
            int width = (int)game.viewport.getWorldWidth();
            int height = (int)game.viewport.getWorldHeight();
            batch.draw(game.craftingTable[1], width / 4, height / 4, width / 2, height / 2);
        }
    }
    
    public boolean isOpen() {
        return isOpen;
    }
}
