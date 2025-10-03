package com.escapethemaze.game.entities;

import com.badlogic.gdx.graphics.Texture;

public class BotSpawner {
    public Texture[] botImage = new Texture[3];
    public byte[] calculatedSpots = new byte[100];
    
    public BotSpawner() {
    }
    
    public void update() {
        // Bot spawning logic
    }
}
