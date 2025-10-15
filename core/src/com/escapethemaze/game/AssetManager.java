package com.escapethemaze.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

public class AssetManager implements Disposable {
    private Map<String, Texture> textures;
    private Map<String, Music> music;
    private Map<String, Sound> sounds;
    
    public Texture[][] playerImages;
    public Texture[][] pigImages;
    public Texture[] treeImages;
    public Texture[] fireImages;
    public Texture[] wallImages;
    public Texture[] craftingTableImages;
    public Texture[][] wheatImages;
    public Texture[] buttonImages;
    public Texture[] textImages;
    public Texture[] pressButtonImages;
    public Texture[] bugImages;
    
    public Texture grassImage;
    public Texture chestImage;
    public Texture mapBackground;
    public Texture trapImage;
    public Texture mineImage;
    public Texture farmlandImage;
    public Texture cobblestoneImage;
    
    public Music grassMusic;
    public Music nightMusic;
    public Music mazeMusic;
    public Music introMusic;
    
    public Sound clickSound;
    public Sound fireSound;
    
    public AssetManager() {
        textures = new HashMap<>();
        music = new HashMap<>();
        sounds = new HashMap<>();
    }
    
    public void load() {
        // Load player images (4 directions x 4 frames)
        playerImages = new Texture[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                playerImages[i][j] = loadTexture("player" + (i+1) + (j+1) + ".png");
            }
        }
        
        // Load pig images (4 directions x 4 frames)
        pigImages = new Texture[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pigImages[i][j] = loadTexture("pig" + (i+1) + (j+1) + ".png");
            }
        }
        
        // Load tree images
        treeImages = new Texture[3];
        for (int i = 0; i < 3; i++) {
            treeImages[i] = loadTexture("tree" + i + ".png");
        }
        
        // Load fire images
        fireImages = new Texture[5];
        for (int i = 0; i < 5; i++) {
            fireImages[i] = loadTexture("fire" + (i+1) + ".png");
        }
        
        // Load wall images
        wallImages = new Texture[3];
        for (int i = 0; i < 3; i++) {
            wallImages[i] = loadTexture("wall" + i + ".png");
        }
        
        // Load button images
        buttonImages = new Texture[13];
        for (int i = 0; i < 13; i++) {
            buttonImages[i] = loadTexture("button" + (i+1) + ".png");
        }
        
        // Load press button images
        pressButtonImages = new Texture[11];
        for (int i = 0; i < 11; i++) {
            pressButtonImages[i] = loadTexture("pressbutton" + (i+1) + ".png");
        }
        
        // Load text images
        textImages = new Texture[3];
        for (int i = 0; i < 3; i++) {
            textImages[i] = loadTexture("text" + (i+1) + ".png");
        }
        
        // Load bug/bot images
        bugImages = new Texture[4];
        for (int i = 0; i < 4; i++) {
            bugImages[i] = loadTexture("bug" + (i+1) + ".png");
        }
        
        // Load crafting table images
        craftingTableImages = new Texture[4];
        craftingTableImages[0] = loadTexture("craftingtable.png");
        craftingTableImages[1] = loadTexture("craftingtable.png");
        craftingTableImages[2] = loadTexture("arrow.png");
        craftingTableImages[3] = loadTexture("arrow2.png");
        
        // Load wheat images (2 types x 8 growth stages)
        wheatImages = new Texture[2][8];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                wheatImages[i][j] = loadTexture("wheat" + i + j + ".png");
            }
        }
        
        // Load other textures
        grassImage = loadTexture("grass.png");
        chestImage = loadTexture("crate.png");
        mapBackground = loadTexture("map.png");
        trapImage = loadTexture("spike.png");
        mineImage = loadTexture("mine.png");
        farmlandImage = loadTexture("farmland.png");
        cobblestoneImage = loadTexture("cobblestone_mossy.png");
        
        // Load music
        grassMusic = loadMusic("grass.mp3");
        nightMusic = loadMusic("night.mp3");
        mazeMusic = loadMusic("maze.mp3");
        introMusic = loadMusic("intro.mp3");
        
        // Load sounds
        clickSound = loadSound("click.mp3");
        fireSound = loadSound("fire.mp3");
    }
    
    private Texture loadTexture(String filename) {
        if (!textures.containsKey(filename)) {
            try {
                Texture texture = new Texture(Gdx.files.internal(filename));
                textures.put(filename, texture);
                return texture;
            } catch (Exception e) {
                Gdx.app.error("AssetManager", "Failed to load texture: " + filename, e);
                return null;
            }
        }
        return textures.get(filename);
    }
    
    private Music loadMusic(String filename) {
        if (!music.containsKey(filename)) {
            try {
                Music m = Gdx.audio.newMusic(Gdx.files.internal(filename));
                music.put(filename, m);
                return m;
            } catch (Exception e) {
                Gdx.app.error("AssetManager", "Failed to load music: " + filename, e);
                return null;
            }
        }
        return music.get(filename);
    }
    
    private Sound loadSound(String filename) {
        if (!sounds.containsKey(filename)) {
            try {
                Sound s = Gdx.audio.newSound(Gdx.files.internal(filename));
                sounds.put(filename, s);
                return s;
            } catch (Exception e) {
                Gdx.app.error("AssetManager", "Failed to load sound: " + filename, e);
                return null;
            }
        }
        return sounds.get(filename);
    }
    
    @Override
    public void dispose() {
        for (Texture texture : textures.values()) {
            if (texture != null) texture.dispose();
        }
        for (Music m : music.values()) {
            if (m != null) m.dispose();
        }
        for (Sound s : sounds.values()) {
            if (s != null) s.dispose();
        }
        textures.clear();
        music.clear();
        sounds.clear();
    }
}
