package com.escapethemaze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EscapeTheMazeGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GameScreen gameScreen;
    private AssetManager assetManager;
    
    public static final int VIRTUAL_WIDTH = 1280;
    public static final int VIRTUAL_HEIGHT = 720;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        
        assetManager = new AssetManager();
        assetManager.load();
        
        gameScreen = new GameScreen(this);
        gameScreen.create();
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(0.48f, 0.48f, 0.48f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        gameScreen.render(Gdx.graphics.getDeltaTime());
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        if (gameScreen != null) {
            gameScreen.dispose();
        }
    }
    
    public SpriteBatch getBatch() {
        return batch;
    }
    
    public OrthographicCamera getCamera() {
        return camera;
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }
}
