package com.escapethemaze.game.entities;

public class Item {
    public int type;
    public int usesLeft;
    
    private static final String[] ITEM_NAMES = {
        "Wood", "Stone", "Wheat", "Seeds", "Bread", 
        "Axe", "Pickaxe", "Hoe", "Sword", "Bow",
        "Arrow", "Torch", "Flint", "Lighter", "Chest",
        "Rope", "Map", "Compass", "Shovel", "Trap",
        "Food"
    };
    
    public Item(int type, boolean hasUses) {
        this.type = type;
        this.usesLeft = hasUses ? getDefaultUses(type) : 0;
    }
    
    public String getItemName() {
        if (type >= 0 && type < ITEM_NAMES.length) {
            return ITEM_NAMES[type];
        }
        return "Unknown";
    }
    
    public static String getItemName(int type) {
        if (type >= 0 && type < ITEM_NAMES.length) {
            return ITEM_NAMES[type];
        }
        return "Unknown";
    }
    
    private int getDefaultUses(int type) {
        // Different items have different durability
        switch (type) {
            case 5: // Axe
            case 6: // Pickaxe
            case 7: // Hoe
            case 8: // Sword
                return 100;
            case 9: // Bow
                return 50;
            case 11: // Torch
                return 200;
            case 13: // Lighter
                return 500;
            default:
                return 1;
        }
    }
    
    public void use() {
        if (usesLeft > 0) {
            usesLeft--;
        }
    }
    
    public boolean isUsable() {
        return usesLeft > 0;
    }
}
