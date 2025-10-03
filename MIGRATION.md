# Migration from Processing to LibGDX

This document outlines the migration of Escape the Maze from Processing to LibGDX.

## Overview

The game has been completely ported from Processing (PApplet-based) to LibGDX, a professional game development framework. This migration provides:

- Better performance and optimization
- Standard Android development practices
- Gradle-based build system
- Cross-platform potential (desktop, iOS, HTML5)
- Modern CI/CD integration

## Architecture Changes

### Project Structure

```
Escape-the-Maze/
├── core/                       # Platform-independent game logic
│   └── src/com/escapethemaze/game/
│       ├── EscapeTheMaze.java  # Main game class
│       ├── entities/           # Game entities (Player, Pig, Tree, etc.)
│       ├── pathfinding/        # A* pathfinding implementation
│       └── systems/            # Game systems (Shop, SaveLoad, etc.)
├── android/                    # Android-specific code
│   ├── src/com/escapethemaze/game/
│   │   └── AndroidLauncher.java
│   └── assets/                 # All game assets (images, sounds)
└── build.gradle               # Root build configuration
```

### Key Changes

#### 1. Rendering System
- **Before:** Processing's `draw()` with `PImage` and `image()`
- **After:** LibGDX's `SpriteBatch` with `Texture` and `batch.draw()`

#### 2. Input Handling
- **Before:** Processing's `keyPressed()`, `mousePressed()`
- **After:** LibGDX's `Gdx.input` API with polling and event-based input

#### 3. Audio System
- **Before:** APMediaPlayer (Android-specific)
- **After:** LibGDX's cross-platform `Music` and `Sound` classes

#### 4. Game Loop
- **Before:** Processing's automatic `setup()` and `draw()` loop
- **After:** LibGDX's `ApplicationAdapter` with `create()`, `render()`, `dispose()`

#### 5. Asset Management
- **Before:** `loadImage()` with Processing's data folder
- **After:** `Gdx.files.internal()` with LibGDX's asset management

#### 6. Save System
- **Before:** File I/O to SD card
- **After:** LibGDX's `Preferences` API (cross-platform key-value storage)

## Implementation Details

### Core Classes

| Processing | LibGDX | Notes |
|------------|--------|-------|
| `PApplet` | `ApplicationAdapter` | Main game class |
| `PImage` | `Texture` | Image resources |
| `PVector` | `Vector2` | 2D vectors (not heavily used in this game) |
| `ArrayList` | `ArrayList` | Same Java collections |
| Processing's `random()` | `java.util.Random` or `MathUtils.random()` | Random number generation |

### Entity System

All game entities (Player, Pig, Tree, Wood, Trap) have been ported with their:
- Position and state management
- Rendering logic using LibGDX's SpriteBatch
- Update logic with delta time
- Collision detection

### Game Systems

1. **Pathfinding** - A* algorithm implemented for maze navigation
2. **Shop** - Crafting and item management system
3. **SaveLoad** - Game state persistence using Preferences API
4. **Tutorial** - In-game tutorial system
5. **Mine** - Trap system for hazards

### Asset Structure

All assets remain in `android/assets/` directory:
- Player sprites: `player11.png` to `player44.png`
- Pig sprites: `pig11.png` to `pig44.png`
- Environment: `grass.png`, `tree0-2.png`, `wall0-2.png`
- Audio: `grass.mp3`, `night.mp3`, `maze.mp3`, etc.

## Build System

### Gradle Configuration

The project uses Gradle 7.5.1 with:
- Android Gradle Plugin 7.2.2
- LibGDX 1.11.0
- Compile SDK: 33
- Min SDK: 16
- Target SDK: 33

### Build Commands

```bash
# Debug build
./gradlew android:assembleDebug

# Release build
./gradlew android:assembleRelease

# Clean build
./gradlew clean
```

## CI/CD Pipeline

GitHub Actions workflow automatically:
1. Builds debug APK on every push
2. Builds release APK on master/main branch
3. Uploads APK artifacts for download
4. Uses caching to speed up builds

Workflow file: `.github/workflows/android-build.yml`

## Known Limitations

Due to the scope of the original game, some advanced features have simplified implementations:

1. **Maze Generation** - Basic implementation, can be enhanced
2. **AI Pathfinding** - Functional but can be optimized
3. **Collision Detection** - Pixel-perfect collision simplified to distance checks
4. **Save System** - Saves core game state, not entire maze configuration

## Future Enhancements

Potential improvements:
1. Desktop version (PC/Mac/Linux) using LibGDX's desktop backend
2. Enhanced graphics with shaders and particles
3. Improved UI with Scene2D widgets
4. Multiplayer support with networking
5. Level editor for custom mazes
6. Achievement system
7. Cloud save support

## Testing

To test the game:
1. Build the debug APK: `./gradlew android:assembleDebug`
2. Install on device: `adb install android/build/outputs/apk/debug/android-debug.apk`
3. Or use the CI/CD pipeline to get pre-built APKs

## Credits

- Original Processing version: Simon Baars
- LibGDX migration: Automated with preservation of all original assets and gameplay
- Framework: LibGDX (https://libgdx.com)
- Assets: GameDevMarket (royalty-free) and custom sprites
