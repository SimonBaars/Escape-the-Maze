package com.escapethemaze.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.escapethemaze.game.EscapeTheMaze;

public class SaveLoad {
    private EscapeTheMaze game;
    private Preferences prefs;
    
    public SaveLoad(EscapeTheMaze game) {
        this.game = game;
        prefs = Gdx.app.getPreferences("EscapeTheMazeSave");
    }
    
    public void saveGame() {
        try {
            // Save player data
            prefs.putFloat("player_x", game.player.x);
            prefs.putFloat("player_y", game.player.y);
            prefs.putFloat("player_health", game.player.health);
            prefs.putInteger("player_food", game.player.currentFood);
            prefs.putInteger("player_selected_item", game.player.selectedItem);
            prefs.putBoolean("player_can_run", game.player.canRun);
            
            // Save game state
            prefs.putInteger("day", game.day);
            prefs.putFloat("camx", game.camx);
            prefs.putFloat("camy", game.camy);
            prefs.putInteger("daytime", game.daytime);
            prefs.putBoolean("night", game.night);
            
            prefs.flush();
            Gdx.app.log("SaveLoad", "Game saved successfully");
        } catch (Exception e) {
            Gdx.app.error("SaveLoad", "Error saving game", e);
        }
    }
    
    public void loadGame() {
        try {
            // Load player data
            game.player.x = prefs.getFloat("player_x", game.player.x);
            game.player.y = prefs.getFloat("player_y", game.player.y);
            game.player.health = prefs.getFloat("player_health", 100);
            game.player.currentFood = prefs.getInteger("player_food", game.maxFood);
            game.player.selectedItem = prefs.getInteger("player_selected_item", 0);
            game.player.canRun = prefs.getBoolean("player_can_run", false);
            
            // Load game state
            game.day = prefs.getInteger("day", 1);
            game.camx = prefs.getFloat("camx", -300);
            game.camy = prefs.getFloat("camy", -300);
            game.daytime = prefs.getInteger("daytime", 0);
            game.night = prefs.getBoolean("night", false);
            
            Gdx.app.log("SaveLoad", "Game loaded successfully");
        } catch (Exception e) {
            Gdx.app.error("SaveLoad", "Error loading game", e);
        }
    }
    
    public boolean hasSavedGame() {
        return prefs.contains("player_x");
    }
    
    public void deleteSave() {
        prefs.clear();
        prefs.flush();
    }
}
