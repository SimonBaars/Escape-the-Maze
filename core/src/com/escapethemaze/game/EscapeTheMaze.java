package com.escapethemaze.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Random;

import com.escapethemaze.game.entities.*;
import com.escapethemaze.game.pathfinding.Pathfinder;
import com.escapethemaze.game.pathfinding.Node;

public class EscapeTheMaze extends ApplicationAdapter {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Viewport viewport;
    public BitmapFont font;
    
    // Game constants
    public int dayLength = 30 * 200;
    public int amountOfBots = 5;
    public int numOfColumns = 50;
    public int tileSize;
    public int gladeSize = 4;
    public float camx = -300, camy = -300;
    public int borderSize = 3;
    public boolean night;
    public int targetNode = 0;
    public int daytime;
    public int vision;
    public int scherm = 10; // screen/menu state
    public int day = 1;
    public boolean canMove = true;
    public boolean notDrawn;
    public byte[] wallTypes;
    public int[] playerNodes = new int[2];
    public int chestSpawnRate = 80;
    public float grieverDamage = 1;
    public int maxFood;
    public int buttonSize;
    public boolean canPress = true;
    public boolean sprintingMode = false;
    public String playerName = "Unknown Name";
    public int isLoading = 0;
    
    // Game entities
    public ArrayList<Node> gladeNodes = new ArrayList<Node>();
    public ArrayList<Tree> trees = new ArrayList<Tree>();
    public ArrayList<Wood> woodBlocks = new ArrayList<Wood>();
    public ArrayList<Pig> pigs = new ArrayList<Pig>();
    public ArrayList<Trap> traps = new ArrayList<Trap>();
    
    public Poort[][] poorten;
    public Node[][] nodeRegister;
    public Lijn[] lijnen = new Lijn[0];
    public Node[] bots;
    public Player player;
    public Farmland farmland;
    public Pathfinder bfs;
    public BotSpawner botSpawner = new BotSpawner();
    
    // Graphics resources
    public Texture[][] pigImage = new Texture[4][4];
    public Texture[][] playerImages = new Texture[4][4];
    public Texture[] treeImages = new Texture[3];
    public Texture[] fireImages = new Texture[5];
    public Texture[] wallImages = new Texture[3];
    public Texture[] craftingTable = new Texture[4];
    public Texture[] buttonImage = new Texture[13];
    public Texture[] textImage = new Texture[3];
    public Texture[] pressButtons = new Texture[11];
    public Texture grassImage;
    public Texture chestImage;
    public Texture mapBg;
    public Texture trapImage;
    public Texture mineImage;
    
    // Audio resources
    public Music grassMusic;
    public Music nightMusic;
    public Music mazeMusic;
    public Music introMusic;
    public Sound clickSound;
    public Sound fireSound;
    
    public Random random = new Random();
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        
        // Calculate sizes based on viewport
        int width = (int)viewport.getWorldWidth();
        int height = (int)viewport.getWorldHeight();
        
        buttonSize = Math.min(width, height) / 8;
        tileSize = getClosestDivisibleBy((width + height) / 7, 12);
        vision = 5;
        maxFood = dayLength * 2;
        
        bots = new Node[amountOfBots];
        poorten = new Poort[numOfColumns][numOfColumns];
        
        // Load all assets
        loadTextures();
        loadAudio();
        
        // Initialize game
        setupGame();
    }
    
    private void loadTextures() {
        try {
            // Load wall textures
            for (int i = 0; i < wallImages.length; i++) {
                wallImages[i] = new Texture(Gdx.files.internal("wall" + i + ".png"));
            }
            
            // Load tree textures
            for (int i = 0; i < treeImages.length; i++) {
                treeImages[i] = new Texture(Gdx.files.internal("tree" + i + ".png"));
            }
            
            // Load fire textures
            for (int i = 0; i < fireImages.length; i++) {
                fireImages[i] = new Texture(Gdx.files.internal("fire" + (i + 1) + ".png"));
            }
            
            // Load button textures
            for (int i = 0; i < buttonImage.length; i++) {
                buttonImage[i] = new Texture(Gdx.files.internal("button" + (i + 1) + ".png"));
            }
            
            // Load press button textures
            for (int i = 0; i < pressButtons.length; i++) {
                pressButtons[i] = new Texture(Gdx.files.internal("pressbutton" + (i + 1) + ".png"));
            }
            
            // Load text images
            for (int i = 0; i < textImage.length; i++) {
                textImage[i] = new Texture(Gdx.files.internal("text" + (i + 1) + ".png"));
            }
            
            // Load pig textures
            for (int i = 0; i < pigImage.length; i++) {
                for (int j = 0; j < pigImage[0].length; j++) {
                    pigImage[i][j] = new Texture(Gdx.files.internal("pig" + (i + 1) + (j + 1) + ".png"));
                }
            }
            
            // Load player textures
            for (int i = 0; i < playerImages.length; i++) {
                for (int j = 0; j < playerImages[0].length; j++) {
                    playerImages[i][j] = new Texture(Gdx.files.internal("player" + (i + 1) + (j + 1) + ".png"));
                }
            }
            
            // Load single textures
            grassImage = new Texture(Gdx.files.internal("grass.png"));
            chestImage = new Texture(Gdx.files.internal("crate.png"));
            mapBg = new Texture(Gdx.files.internal("map.png"));
            trapImage = new Texture(Gdx.files.internal("spike.png"));
            mineImage = new Texture(Gdx.files.internal("mine.png"));
            
            // Load crafting table textures
            craftingTable[0] = new Texture(Gdx.files.internal("craftingtable.png"));
            craftingTable[1] = new Texture(Gdx.files.internal("craftingtable.png"));
            craftingTable[2] = new Texture(Gdx.files.internal("arrow.png"));
            craftingTable[3] = new Texture(Gdx.files.internal("arrow2.png"));
            
        } catch (Exception e) {
            Gdx.app.error("EscapeTheMaze", "Error loading textures", e);
        }
    }
    
    private void loadAudio() {
        try {
            grassMusic = Gdx.audio.newMusic(Gdx.files.internal("grass.mp3"));
            nightMusic = Gdx.audio.newMusic(Gdx.files.internal("night.mp3"));
            mazeMusic = Gdx.audio.newMusic(Gdx.files.internal("maze.mp3"));
            introMusic = Gdx.audio.newMusic(Gdx.files.internal("intro.mp3"));
            clickSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
            fireSound = Gdx.audio.newSound(Gdx.files.internal("fire.mp3"));
            
            grassMusic.setLooping(true);
            nightMusic.setLooping(true);
            mazeMusic.setLooping(true);
            introMusic.setLooping(true);
        } catch (Exception e) {
            Gdx.app.error("EscapeTheMaze", "Error loading audio", e);
        }
    }
    
    private void setupGame() {
        day = 1;
        canMove = true;
        camx = -numOfColumns * tileSize / 2f;
        camy = -numOfColumns * tileSize / 2f;
        
        int width = (int)viewport.getWorldWidth();
        int height = (int)viewport.getWorldHeight();
        
        player = new Player(width / 2, height / 2, tileSize / 6, this);
        
        initMap();
        wallTypes = new byte[bfs.nodes.size()];
        for (int i = 0; i < wallTypes.length; i++) {
            wallTypes[i] = (byte) random.nextInt(wallImages.length);
        }
        
        for (int i = 0; i < amountOfBots; i++) {
            bots[i] = null;
        }
        
        setNight(false);
        
        // Add initial pigs
        if (gladeNodes.size() > 300) {
            pigs.add(new Pig((int)gladeNodes.get(300).x, (int)gladeNodes.get(290).y, this));
            pigs.add(new Pig((int)gladeNodes.get(301).x, (int)gladeNodes.get(291).y, this));
        }
        
        farmland = new Farmland(this);
    }
    
    private void initMap() {
        bfs = new Pathfinder();
        bfs.offsetX = tileSize / 2;
        bfs.offsetY = tileSize / 2;
        bfs.setCuboidNodes(numOfColumns * 3 - 3, numOfColumns * 3 - 3, tileSize / 3);
        nodeRegister = new Node[numOfColumns * 3 - 3][numOfColumns * 3 - 3];
        fillNodeRegister();
        
        // Initialize glade nodes (safe starting area)
        for (int x = 0; x < numOfColumns * 3 - 3; x++) {
            for (int y = 0; y < numOfColumns * 3 - 3; y++) {
                if (isCloseTo(x, gladeSize, numOfColumns * 3 / 2 - 1) && 
                    isCloseTo(y, gladeSize, numOfColumns * 3 / 2 - 1)) {
                    if (nodeRegister[x][y] != null) {
                        gladeNodes.add(nodeRegister[x][y]);
                    }
                }
            }
        }
    }
    
    private void fillNodeRegister() {
        for (int i = 0; i < bfs.nodes.size(); i++) {
            Node node = bfs.nodes.get(i);
            int x = (int)((node.x - bfs.offsetX) / (tileSize / 3));
            int y = (int)((node.y - bfs.offsetY) / (tileSize / 3));
            if (x >= 0 && x < nodeRegister.length && y >= 0 && y < nodeRegister[0].length) {
                nodeRegister[x][y] = node;
            }
        }
    }
    
    private void setNight(boolean isNight) {
        this.night = isNight;
        if (isNight) {
            if (!nightMusic.isPlaying()) {
                stopAllMusic();
                nightMusic.play();
            }
        } else {
            if (!grassMusic.isPlaying()) {
                stopAllMusic();
                grassMusic.play();
            }
        }
    }
    
    private void stopAllMusic() {
        grassMusic.stop();
        nightMusic.stop();
        mazeMusic.stop();
        introMusic.stop();
    }
    
    private boolean isCloseTo(int number, int howCloseTo, int anotherNumber) {
        return Math.abs(number - anotherNumber) <= howCloseTo;
    }
    
    private int getClosestDivisibleBy(int number, int divisor) {
        int remainder = number % divisor;
        if (remainder < divisor / 2) {
            return number - remainder;
        } else {
            return number + (divisor - remainder);
        }
    }
    
    @Override
    public void render() {
        handleInput();
        update(Gdx.graphics.getDeltaTime());
        draw();
    }
    
    private void handleInput() {
        if (scherm == 1 && canMove) { // In game
            int moveX = 0;
            int moveY = 0;
            
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                moveY = -1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                moveY = 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                moveX = -1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                moveX = 1;
            }
            
            if (moveX != 0 || moveY != 0) {
                int speed = player.movingSpeed;
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && player.canRun) {
                    speed = player.runningSpeed;
                }
                player.moveTo(moveX * speed, moveY * speed);
            }
            
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                // Use item
                player.useSelectedItem();
            }
            
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                scherm = 8; // Pause menu
            }
        }
    }
    
    private void update(float delta) {
        if (scherm == 1) { // In game
            daytime++;
            if (daytime > dayLength) {
                daytime = 0;
                night = !night;
                setNight(night);
                if (!night) {
                    day++;
                }
            }
            
            // Update player
            playerNodes[0] = (int)(((player.x - camx) - (tileSize / 2f)) / tileSize);
            playerNodes[1] = (int)(((player.y - camy) - (tileSize / 2f)) / tileSize);
            player.update(delta);
            
            // Update entities
            for (Pig pig : pigs) {
                pig.update(delta);
            }
            
            for (Tree tree : trees) {
                tree.update(delta);
            }
            
            // Check player health
            if (player.currentFood <= 0) {
                player.health -= 0.1f;
            }
            if (player.health <= 0) {
                scherm = 2; // Game over
            }
        }
    }
    
    private void draw() {
        Gdx.gl.glClearColor(0.48f, 0.48f, 0.48f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        
        if (scherm == 1) {
            drawGame();
        } else if (scherm == 2) {
            drawGameOver();
        } else if (scherm == 10) {
            drawMainMenu();
        }
        
        batch.end();
    }
    
    private void drawGame() {
        // Draw ground
        int startX = Math.max(0, playerNodes[0] - vision);
        int startY = Math.max(0, playerNodes[1] - vision);
        int endX = Math.min(numOfColumns, playerNodes[0] + vision);
        int endY = Math.min(numOfColumns, playerNodes[1] + vision);
        
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                float drawX = x * tileSize + camx;
                float drawY = y * tileSize + camy;
                batch.draw(grassImage, drawX, drawY, tileSize / 3f, tileSize / 3f);
            }
        }
        
        // Draw trees
        for (Tree tree : trees) {
            tree.draw(batch, camx, camy);
        }
        
        // Draw wood blocks
        for (Wood wood : woodBlocks) {
            wood.draw(batch, camx, camy);
        }
        
        // Draw pigs
        for (Pig pig : pigs) {
            pig.draw(batch, camx, camy);
        }
        
        // Draw player
        player.draw(batch, camx, camy);
        
        // Draw UI
        drawUI();
    }
    
    private void drawUI() {
        int width = (int)viewport.getWorldWidth();
        int height = (int)viewport.getWorldHeight();
        
        // Draw health bar
        font.draw(batch, "Health: " + (int)player.health, 10, height - 10);
        font.draw(batch, "Food: " + player.currentFood, 10, height - 30);
        font.draw(batch, "Day: " + day, 10, height - 50);
    }
    
    private void drawGameOver() {
        int width = (int)viewport.getWorldWidth();
        int height = (int)viewport.getWorldHeight();
        font.draw(batch, "GAME OVER", width / 2 - 50, height / 2);
        font.draw(batch, "Press R to restart", width / 2 - 80, height / 2 - 30);
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            setupGame();
            scherm = 1;
        }
    }
    
    private void drawMainMenu() {
        int width = (int)viewport.getWorldWidth();
        int height = (int)viewport.getWorldHeight();
        font.draw(batch, "ESCAPE THE MAZE", width / 2 - 100, height / 2 + 50);
        font.draw(batch, "Press SPACE to start", width / 2 - 100, height / 2);
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            scherm = 1;
        }
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        
        // Dispose textures
        for (Texture[] textures : wallImages) {
            if (textures != null) textures.dispose();
        }
        for (Texture[] textures : treeImages) {
            if (textures != null) textures.dispose();
        }
        for (Texture[] textures : fireImages) {
            if (textures != null) textures.dispose();
        }
        for (Texture[][] textures : pigImage) {
            for (Texture[] row : textures) {
                for (Texture tex : row) {
                    if (tex != null) tex.dispose();
                }
            }
        }
        for (Texture[][] textures : playerImages) {
            for (Texture[] row : textures) {
                for (Texture tex : row) {
                    if (tex != null) tex.dispose();
                }
            }
        }
        
        if (grassImage != null) grassImage.dispose();
        if (chestImage != null) chestImage.dispose();
        if (mapBg != null) mapBg.dispose();
        if (trapImage != null) trapImage.dispose();
        if (mineImage != null) mineImage.dispose();
        
        // Dispose audio
        if (grassMusic != null) grassMusic.dispose();
        if (nightMusic != null) nightMusic.dispose();
        if (mazeMusic != null) mazeMusic.dispose();
        if (introMusic != null) introMusic.dispose();
        if (clickSound != null) clickSound.dispose();
        if (fireSound != null) fireSound.dispose();
    }
}
