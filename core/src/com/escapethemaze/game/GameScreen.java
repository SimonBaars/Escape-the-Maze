package com.escapethemaze.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.escapethemaze.game.entities.*;
import com.escapethemaze.game.world.MazeGenerator;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Disposable {
    private EscapeTheMazeGame game;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private GlyphLayout menuTitleLayout;
    private GlyphLayout menuInstructionsLayout;
    private GlyphLayout gameOverLayout;
    private GlyphLayout gameOverInstructionsLayout;
    
    // Game state
    private int screen = 10; // 10 = menu, 1 = game, 2 = game over, etc.
    private int day = 1;
    private int dayTime = 0;
    private boolean night = false;
    private boolean canMove = true;
    
    // World settings
    private int numOfColumns = 50;
    private int tileSize = 180;
    private int gladeSize = 4;
    private int cameraX = -300;
    private int cameraY = -300;
    private int vision = 4;
    
    // Game objects
    private Player player;
    private List<Pig> pigs;
    private List<Tree> trees;
    private List<Wood> woodBlocks;
    private List<Trap> traps;
    private MazeGenerator mazeGenerator;
    private AudioManager audioManager;
    
    // Constants
    private static final int DAY_LENGTH = 30 * 200;
    private static final int BORDER_SIZE = 3;
    
    public GameScreen(EscapeTheMazeGame game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.font.getData().setScale(2.0f);
        
        // Pre-calculate text layouts for menu and game over screens
        this.menuTitleLayout = new GlyphLayout(font, "ESCAPE THE MAZE");
        this.menuInstructionsLayout = new GlyphLayout(font, "Press ENTER or TOUCH to start");
        this.gameOverLayout = new GlyphLayout(font, "GAME OVER");
        this.gameOverInstructionsLayout = new GlyphLayout(font, "Press ENTER or TOUCH to return to menu");
        
        this.pigs = new ArrayList<>();
        this.trees = new ArrayList<>();
        this.woodBlocks = new ArrayList<>();
        this.traps = new ArrayList<>();
    }
    
    public void create() {
        audioManager = new AudioManager(game.getAssetManager());
        audioManager.playMusic(AudioManager.MusicType.INTRO);
        
        // Show menu initially
        screen = 10;
    }
    
    public void setupGame() {
        day = 1;
        canMove = true;
        
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        
        cameraX = -numOfColumns * tileSize / 2;
        cameraY = -numOfColumns * tileSize / 2;
        
        player = new Player(screenWidth / 2, screenHeight / 2, tileSize / 6);
        
        // Initialize maze
        mazeGenerator = new MazeGenerator(numOfColumns, tileSize, gladeSize);
        mazeGenerator.generate();
        
        // Add some pigs
        pigs.clear();
        pigs.add(new Pig(300, 300, tileSize));
        pigs.add(new Pig(350, 350, tileSize));
        
        setNight(false);
        
        screen = 1; // Start game
    }
    
    public void render(float delta) {
        SpriteBatch batch = game.getBatch();
        
        if (screen == 10) {
            // Main menu
            renderMenu(batch);
            handleMenuInput();
        } else if (screen == 1) {
            // Game
            renderGame(batch, delta);
            handleGameInput(delta);
            updateGame(delta);
        } else if (screen == 2) {
            // Game over
            renderGameOver(batch);
            handleGameOverInput();
        }
    }
    
    private void renderMenu(SpriteBatch batch) {
        batch.begin();
        
        // Draw title
        float titleX = (Gdx.graphics.getWidth() - menuTitleLayout.width) / 2;
        float titleY = Gdx.graphics.getHeight() / 2 + 100;
        font.draw(batch, "ESCAPE THE MAZE", titleX, titleY);
        
        // Draw instructions
        float instructionsX = (Gdx.graphics.getWidth() - menuInstructionsLayout.width) / 2;
        float instructionsY = Gdx.graphics.getHeight() / 2 - 50;
        font.draw(batch, "Press ENTER or TOUCH to start", instructionsX, instructionsY);
        
        batch.end();
    }
    
    private void handleMenuInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            setupGame();
        }
    }
    
    private void renderGame(SpriteBatch batch, float delta) {
        batch.begin();
        
        // Draw background
        // TODO: Draw maze walls and paths
        
        // Draw entities
        if (player != null) {
            player.render(batch, game.getAssetManager(), cameraX, cameraY);
        }
        
        for (Pig pig : pigs) {
            pig.render(batch, game.getAssetManager(), cameraX, cameraY);
        }
        
        for (Tree tree : trees) {
            tree.render(batch, game.getAssetManager(), cameraX, cameraY);
        }
        
        for (Wood wood : woodBlocks) {
            wood.render(batch, game.getAssetManager(), cameraX, cameraY);
        }
        
        // Draw UI
        // TODO: Add health bar, inventory, etc.
        
        batch.end();
        
        // Draw maze lines with ShapeRenderer
        shapeRenderer.setProjectionMatrix(game.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GRAY);
        if (mazeGenerator != null) {
            mazeGenerator.render(shapeRenderer, cameraX, cameraY);
        }
        shapeRenderer.end();
    }
    
    private void handleGameInput(float delta) {
        if (player != null && canMove) {
            float moveX = 0;
            float moveY = 0;
            
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                moveY = 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                moveY = -1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveX = -1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveX = 1;
            }
            
            // Handle touch/mouse movement for mobile
            if (Gdx.input.isTouched()) {
                int touchX = Gdx.input.getX();
                int touchY = Gdx.input.getY();
                int screenWidth = Gdx.graphics.getWidth();
                int screenHeight = Gdx.graphics.getHeight();
                
                // Divide screen into zones for movement
                if (touchX < screenWidth / 3) {
                    moveX = -1;
                } else if (touchX > screenWidth * 2 / 3) {
                    moveX = 1;
                }
                
                if (touchY < screenHeight / 3) {
                    moveY = 1;
                } else if (touchY > screenHeight * 2 / 3) {
                    moveY = -1;
                }
            }
            
            player.move(moveX, moveY);
        }
    }
    
    private void updateGame(float delta) {
        dayTime++;
        
        if (dayTime >= DAY_LENGTH) {
            dayTime = 0;
            night = !night;
            setNight(night);
            if (night) {
                day++;
            }
        }
        
        // Update player
        if (player != null) {
            player.update(delta);
            
            // Check boundaries
            checkPlayerBoundaries();
            
            // Update food and health
            player.currentFood -= 0.1f * delta;
            if (player.currentFood <= 0) {
                player.health -= 0.1f;
            }
            
            if (player.health <= 0) {
                screen = 2; // Game over
            }
        }
        
        // Update pigs
        for (Pig pig : pigs) {
            pig.update(delta);
        }
        
        // Update trees
        for (Tree tree : trees) {
            tree.update(delta);
        }
        
        // Update audio
        if (audioManager != null) {
            audioManager.update();
        }
    }
    
    private void checkPlayerBoundaries() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        
        if (player.x < screenWidth / BORDER_SIZE) {
            cameraX -= player.x - (screenWidth / BORDER_SIZE);
            player.x = screenWidth / BORDER_SIZE;
        } else if (player.x > screenWidth - (screenWidth / BORDER_SIZE)) {
            cameraX -= player.x - (screenWidth - (screenWidth / BORDER_SIZE));
            player.x = screenWidth - (screenWidth / BORDER_SIZE);
        }
        
        if (player.y < screenHeight / BORDER_SIZE) {
            cameraY -= player.y - (screenHeight / BORDER_SIZE);
            player.y = screenHeight / BORDER_SIZE;
        } else if (player.y > screenHeight - (screenHeight / BORDER_SIZE)) {
            cameraY -= player.y - (screenHeight - (screenHeight / BORDER_SIZE));
            player.y = screenHeight - (screenHeight / BORDER_SIZE);
        }
    }
    
    private void setNight(boolean isNight) {
        this.night = isNight;
        if (audioManager != null) {
            if (isNight) {
                audioManager.playMusic(AudioManager.MusicType.NIGHT);
            } else {
                audioManager.playMusic(AudioManager.MusicType.MAZE);
            }
        }
    }
    
    private void renderGameOver(SpriteBatch batch) {
        batch.begin();
        
        // Draw game over text
        float gameOverX = (Gdx.graphics.getWidth() - gameOverLayout.width) / 2;
        float gameOverY = Gdx.graphics.getHeight() / 2 + 100;
        font.draw(batch, "GAME OVER", gameOverX, gameOverY);
        
        // Draw instructions
        float instructionsX = (Gdx.graphics.getWidth() - gameOverInstructionsLayout.width) / 2;
        float instructionsY = Gdx.graphics.getHeight() / 2 - 50;
        font.draw(batch, "Press ENTER or TOUCH to return to menu", instructionsX, instructionsY);
        
        batch.end();
    }
    
    private void handleGameOverInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            screen = 10; // Back to menu
        }
    }
    
    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        if (audioManager != null) {
            audioManager.dispose();
        }
    }
    
    public int getCameraX() {
        return cameraX;
    }
    
    public int getCameraY() {
        return cameraY;
    }
    
    public int getTileSize() {
        return tileSize;
    }
}
