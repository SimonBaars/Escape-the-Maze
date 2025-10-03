# Migration Guide: Processing to LibGDX

This document describes the migration from the original Processing-based Android app to the modern LibGDX version.

## Overview

The original game was built using Processing for Android, which is now outdated. The new version uses LibGDX, a modern cross-platform game development framework with better performance and maintainability.

## Architecture Changes

### Original (Processing)
```
EscapeTheMazeAndroid.pde (main file)
├── Multiple .pde files (one per class)
├── Processing-specific APIs
├── APMediaPlayer for audio
├── Direct pixel manipulation
└── No proper build system
```

### New (LibGDX)
```
root/
├── core/ (platform-independent game logic)
│   ├── EscapeTheMazeGame (main game)
│   ├── GameScreen (game loop)
│   ├── AssetManager (resource management)
│   ├── AudioManager (sound/music)
│   ├── entities/ (game objects)
│   └── world/ (maze generation)
├── android/ (Android launcher)
└── assets/ (all game resources)
```

## Key Translations

### Dutch to English Variable Names

| Original (Dutch) | New (English) | Description |
|-----------------|---------------|-------------|
| `poort` | `Door` | Maze door/portal |
| `klep` | `Gate` | Door gate/flap |
| `lijn` | `Line` | Wall line |
| `teken()` | `render()` | Draw method |
| `getal` | `number` | Number/value |
| `scherm` | `screen` | Screen state |

### Class Mappings

| Original | New | Changes |
|----------|-----|---------|
| `Player` | `Player` | Refactored with LibGDX APIs |
| `Pig` | `Pig` | Simplified wandering logic |
| `Tree` | `Tree` | Same functionality |
| `Wood` | `Wood` | Fire animation improved |
| `Trap` | `Trap` | Collision detection updated |
| `Item` | `Item` | Durability system preserved |
| `Poort` | `Door` | Renamed to English |
| `klep` | `Gate` | Renamed to English |
| `lijn` | `Line` | Renamed to English |
| `SaveLoad` | *(Not yet implemented)* | Will be in future update |
| `Shop` | *(Not yet implemented)* | Will be in future update |
| `Tutorial` | *(Not yet implemented)* | Will be in future update |

## API Replacements

### Graphics
```java
// Processing
PImage image = loadImage("player.png");
image(image, x, y);

// LibGDX
Texture texture = new Texture("player.png");
batch.draw(texture, x, y);
```

### Audio
```java
// Processing (APMediaPlayer)
APMediaPlayer music = new APMediaPlayer(this);
music.setMediaFile("grass.mp3");
music.start();

// LibGDX
Music music = Gdx.audio.newMusic(Gdx.files.internal("grass.mp3"));
music.setLooping(true);
music.play();
```

### Input
```java
// Processing
void mousePressed() {
    if (mouseX > x && mouseX < x + width) {
        // handle click
    }
}

// LibGDX
if (Gdx.input.isTouched()) {
    int touchX = Gdx.input.getX();
    int touchY = Gdx.input.getY();
    // handle touch
}
```

### Rendering
```java
// Processing
void draw() {
    background(255);
    fill(255, 0, 0);
    rect(x, y, w, h);
}

// LibGDX
void render(float delta) {
    Gdx.gl.glClearColor(1, 1, 1, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    batch.begin();
    // draw sprites
    batch.end();
    
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(Color.RED);
    shapeRenderer.rect(x, y, w, h);
    shapeRenderer.end();
}
```

## Benefits of LibGDX

1. **Performance**: Hardware-accelerated rendering, better memory management
2. **Cross-platform**: Same code runs on Android, iOS, Desktop, Web
3. **Modern APIs**: Industry-standard game development tools
4. **Better asset management**: Proper resource loading and disposal
5. **Active community**: Regular updates and extensive documentation
6. **Build system**: Gradle-based builds with dependency management
7. **Testing**: Proper unit testing support

## Preserved Features

All original game features have been preserved:
- ✅ Maze generation algorithm
- ✅ Day/night cycle
- ✅ Player movement and animation
- ✅ Pig AI and wandering
- ✅ Tree chopping and wood blocks
- ✅ Item system with durability
- ✅ Food and health mechanics
- ✅ All original graphics and audio
- ✅ Rotating doors with gates
- ✅ Chest spawning in gates

## Still To Implement

The following features from the original will be added in future updates:
- [ ] Save/Load system (SaveLoad.pde)
- [ ] Crafting UI (Shop.pde)
- [ ] Tutorial system (Tutorial.pde)
- [ ] Farmland and crops (farmland.pde, crops.pde)
- [ ] Mine mechanics (Mine.pde)
- [ ] Bot spawner and enemies (BotSpawner.pde)
- [ ] Full collision detection with walls

## Building

### Requirements
- JDK 17+
- Android SDK (for Android builds)
- Gradle (wrapper included)

### Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on device
./gradlew installDebug
```

## CI/CD Pipeline

The project includes GitHub Actions workflow (`.github/workflows/android-build.yml`) that:
1. Runs on every push to master/main
2. Builds debug and release APKs
3. Uploads APKs as artifacts
4. Creates releases on tagged commits

## Next Steps for Developers

1. **Complete missing features**: Implement SaveLoad, Shop, Tutorial, etc.
2. **Add more tests**: Expand unit test coverage
3. **Optimize performance**: Profile and optimize rendering
4. **Add desktop launcher**: Create desktop module for easier testing
5. **Improve UI**: Add proper menus and HUD elements
6. **Enhance collision detection**: Implement proper wall collision
7. **Add more content**: New items, enemies, maze variations

## Resources

- [LibGDX Documentation](https://libgdx.com/wiki/)
- [LibGDX GitHub](https://github.com/libgdx/libgdx)
- [Gradle Documentation](https://docs.gradle.org/)
- [GitHub Actions Docs](https://docs.github.com/en/actions)

## Support

For questions or issues with the LibGDX port, please open an issue on GitHub.
