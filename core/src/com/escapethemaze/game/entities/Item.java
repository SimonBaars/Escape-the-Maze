package com.escapethemaze.game.entities;

import com.escapethemaze.game.EscapeTheMaze;

public class Item {
    public int id;
    public boolean hasItem;
    private EscapeTheMaze game;
    
    public Item(int id, boolean hasItem, EscapeTheMaze game) {
        this.id = id;
        this.hasItem = hasItem;
        this.game = game;
    }
    
    public String getItemName(int id) {
        switch(id) {
            case 0: return "Wood";
            case 1: return "Wheat";
            case 2: return "Bread";
            case 3: return "Teleporter";
            case 4: return "Map";
            case 5: return "Compass";
            case 6: return "Torch";
            case 7: return "Sword";
            case 8: return "Bow";
            case 9: return "Arrow";
            case 10: return "Key";
            case 11: return "Potion";
            case 12: return "Torch";
            default: return "Unknown";
        }
    }
    
    public void use() {
        switch(id) {
            case 0: // Wood - place wood block
                placeWood();
                break;
            case 1: // Wheat - feed pigs
                feedPigs();
                break;
            case 2: // Bread - eat
                game.player.feed(5000);
                removeFromInventory();
                break;
            case 3: // Teleporter
                // Teleport logic would go here
                break;
            default:
                break;
        }
    }
    
    private void placeWood() {
        // Check if in glade area
        if (isInGlade((int)(game.player.x - game.camx), (int)(game.player.y - game.camy))) {
            new Wood(game.player.x - game.camx - (game.tileSize / 12f), 
                    game.player.y - game.camy - (game.tileSize / 3f), game);
            removeFromInventory();
        }
    }
    
    private void feedPigs() {
        for (Pig pig : game.pigs) {
            if (pig.feed()) {
                removeFromInventory();
                break;
            }
        }
    }
    
    private void removeFromInventory() {
        game.player.inventory.remove(this);
    }
    
    private boolean isInGlade(int x, int y) {
        int tileX = x / game.tileSize;
        int tileY = y / game.tileSize;
        return isCloseTo(tileX, game.gladeSize - 1, game.numOfColumns / 2) && 
               isCloseTo(tileY, game.gladeSize - 1, game.numOfColumns / 2);
    }
    
    private boolean isCloseTo(int number, int howCloseTo, int anotherNumber) {
        return Math.abs(number - anotherNumber) <= howCloseTo;
    }
}
