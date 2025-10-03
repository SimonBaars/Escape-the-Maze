# Escape the Maze - LibGDX Port

This is a LibGDX port of the original Processing-based Android game "Escape the Maze".

## Project Structure

```
Escape-the-Maze/
├── core/                          # Core game logic (platform-independent)
│   └── src/com/escapethemaze/game/
│       ├── EscapeTheMazeGame.java    # Main game class
│       ├── AssetManager.java         # Asset loading and management
│       ├── AudioManager.java         # Audio management
│       ├── GameScreen.java           # Game rendering and logic
│       ├── entities/                 # Game entities
│       │   ├── Player.java
│       │   ├── Pig.java
│       │   ├── Tree.java
│       │   ├── Wood.java
│       │   ├── Trap.java
│       │   └── Item.java
│       └── world/                    # World generation
│           ├── MazeGenerator.java
│           ├── Door.java             # Previously "Poort"
│           ├── Gate.java             # Previously "Klep"
│           └── Line.java             # Previously "Lijn"
├── android/                       # Android launcher
│   └── src/com/escapethemaze/android/
│       └── AndroidLauncher.java
├── assets/                        # All game assets (textures, sounds, music)
└── .github/workflows/             # CI/CD pipeline
    └── android-build.yml

```

## Changes from Original

### Code Improvements
1. **English variable names**: All Dutch variable names have been translated to English:
   - `poort` → `Door`
   - `klep` → `Gate`
   - `lijn` → `Line`
   - `teken()` → `render()`
   - `getal` → `number`

2. **Modern architecture**: 
   - Separated concerns with dedicated managers (AssetManager, AudioManager)
   - Entity-based design for game objects
   - Proper separation of game logic and rendering

3. **LibGDX benefits**:
   - Better performance
   - Cross-platform support
   - Modern graphics rendering
   - Better asset management
   - Improved input handling

### Build System
- Gradle build system
- Automated CI/CD with GitHub Actions
- Generates both debug and release APKs

## Building the Game

### Prerequisites
- JDK 17 or higher
- Android SDK (for Android builds)

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Run on connected device
./gradlew android:run
```

### CI/CD Pipeline

The project includes a GitHub Actions workflow that automatically:
1. Builds the project on every push to master/main
2. Generates debug and release APKs
3. Uploads APKs as build artifacts
4. Creates releases with APKs attached (on tagged commits)

To create a release:
```bash
git tag v1.0.0
git push origin v1.0.0
```

## Game Controls

### Desktop (for testing)
- **WASD / Arrow Keys**: Move player
- **Enter**: Start game / Continue

### Android
- **Touch screen**: Divided into zones for movement
  - Left third: Move left
  - Right third: Move right
  - Top third: Move up
  - Bottom third: Move down

## Development

### Project Setup
1. Clone the repository
2. Import as Gradle project in IntelliJ IDEA or Android Studio
3. Run `./gradlew build` to verify setup

### Running Tests
```bash
./gradlew test
```

### Code Style
- Follow Java naming conventions
- Use English for all identifiers
- Keep methods focused and small
- Document complex logic

## Assets

All original game assets have been preserved in the `assets/` directory:
- Player sprites (4 directions × 4 animation frames)
- Pig sprites (4 directions × 4 animation frames)
- Tree sprites (3 growth stages)
- Wall textures (3 variants)
- UI elements (buttons, text, etc.)
- Audio files (music and sound effects)

## License

See LICENSE file for details. All audio and sprites are from royalty-free packs or created by the original author.

## Credits

- Original game by Simon Baars
- LibGDX port maintaining all original game mechanics and assets
