package com.escapethemaze.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class AudioManager implements Disposable {
    private AssetManager assetManager;
    private Music currentMusic;
    private MusicType currentMusicType;
    
    public enum MusicType {
        INTRO,
        GRASS,
        MAZE,
        NIGHT
    }
    
    public AudioManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public void playMusic(MusicType type) {
        if (currentMusicType == type && currentMusic != null && currentMusic.isPlaying()) {
            return; // Already playing this music
        }
        
        stopAllMusic();
        
        currentMusicType = type;
        switch (type) {
            case INTRO:
                currentMusic = assetManager.introMusic;
                break;
            case GRASS:
                currentMusic = assetManager.grassMusic;
                break;
            case MAZE:
                currentMusic = assetManager.mazeMusic;
                break;
            case NIGHT:
                currentMusic = assetManager.nightMusic;
                break;
        }
        
        if (currentMusic != null) {
            currentMusic.setLooping(true);
            currentMusic.setVolume(0.5f);
            currentMusic.play();
        }
    }
    
    public void stopAllMusic() {
        if (assetManager.introMusic != null) assetManager.introMusic.stop();
        if (assetManager.grassMusic != null) assetManager.grassMusic.stop();
        if (assetManager.mazeMusic != null) assetManager.mazeMusic.stop();
        if (assetManager.nightMusic != null) assetManager.nightMusic.stop();
    }
    
    public void playSound(Sound sound) {
        if (sound != null) {
            sound.play(0.7f);
        }
    }
    
    public void playClickSound() {
        playSound(assetManager.clickSound);
    }
    
    public void playFireSound() {
        playSound(assetManager.fireSound);
    }
    
    public void update() {
        // Handle any audio updates if needed
    }
    
    @Override
    public void dispose() {
        stopAllMusic();
    }
}
