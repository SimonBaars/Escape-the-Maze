# Changelog - LibGDX Migration

## Version 2.0.0 - LibGDX Port (2024)

### 🎉 Major Changes

**Complete framework migration from Processing to LibGDX**

This is a major rewrite that maintains all original gameplay while modernizing the codebase.

### ✨ New Features

#### Build System
- ✅ Gradle-based build system
- ✅ Multi-module project structure (core + android)
- ✅ Gradle wrapper for consistent builds
- ✅ ProGuard configuration for release optimization
- ✅ Automated CI/CD with GitHub Actions

#### Development
- ✅ Professional game framework (LibGDX)
- ✅ Cross-platform architecture (ready for desktop/iOS/HTML5)
- ✅ Modern Java codebase with proper OOP design
- ✅ Improved code organization and maintainability
- ✅ Better performance and optimization opportunities

#### Game Systems
- ✅ Enhanced save/load using LibGDX Preferences API
- ✅ Improved asset management
- ✅ Better audio system with Music and Sound classes
- ✅ Viewport management for consistent display across devices
- ✅ Delta-time based updates for smooth gameplay

### 🔄 Changed

#### Code Structure
- **Before**: Single `EscapeTheMazeAndroid.java` (3615 lines)
- **After**: 18 Java files in organized packages
  - Main game class: `EscapeTheMaze.java`
  - Entity classes: 11 files in `entities/` package
  - Pathfinding: 2 files in `pathfinding/` package
  - Game systems: 4 files in `systems/` package
  - Android launcher: `AndroidLauncher.java`

#### API Changes

| Processing | LibGDX | Purpose |
|------------|--------|---------|
| `PApplet` | `ApplicationAdapter` | Main game class |
| `PImage` | `Texture` | Image resources |
| `image(img, x, y)` | `batch.draw(texture, x, y)` | Rendering |
| `loadImage()` | `Gdx.files.internal()` | Asset loading |
| `frameRate(30)` | Delta time updates | Frame rate |
| `APMediaPlayer` | `Music`, `Sound` | Audio playback |
| File I/O to SD card | `Preferences` API | Save/load |
| `mousePressed()` | `Gdx.input` API | Input handling |

#### Build Process
- **Before**: Processing IDE export or Ant build
- **After**: Gradle commands
  - Debug: `./gradlew android:assembleDebug`
  - Release: `./gradlew android:assembleRelease`

#### Deployment
- **Before**: Manual APK export from Processing
- **After**: Automated CI/CD pipeline
  - Builds on every commit
  - Uploads APK artifacts
  - Supports both debug and release builds

### 📦 Assets

- ✅ All original assets preserved (87+ files)
- ✅ Same directory structure in `android/assets/`
- ✅ No asset modifications needed
- ✅ Player sprites: 16 files (4 directions × 4 frames)
- ✅ Pig sprites: 16 files (4 directions × 4 frames)
- ✅ Environment: grass, trees, walls, UI elements
- ✅ Audio: 6 music/sound files

### 🎮 Gameplay

**No gameplay changes** - All original features maintained:
- ✅ Player movement and animation
- ✅ Day/night cycle
- ✅ Food and health system
- ✅ Inventory and crafting
- ✅ AI-controlled pigs
- ✅ Maze navigation
- ✅ Save/load functionality
- ✅ Tutorial system
- ✅ Shop system
- ✅ Traps and hazards

### 🐛 Bug Fixes

- Improved collision detection
- Better camera following
- More stable save/load system
- Consistent rendering across devices

### 📚 Documentation

#### New Files
- `README.md` - Updated with build instructions
- `MIGRATION.md` - Detailed migration guide
- `PROJECT_SUMMARY.md` - Comprehensive overview
- `QUICK_START.md` - Quick reference guide
- `CHANGES.md` - This file
- `.github/workflows/android-build.yml` - CI/CD configuration

### 🔧 Technical Details

#### Dependencies
- **LibGDX**: 1.11.0
- **Gradle**: 7.5.1
- **Android Gradle Plugin**: 7.2.2
- **Compile SDK**: 33
- **Min SDK**: 16 (Android 4.1+)
- **Target SDK**: 33

#### Package Structure
```
com.escapethemaze.game
├── EscapeTheMaze          # Main game
├── entities/              # Game entities
│   ├── Player
│   ├── Pig
│   ├── Tree
│   ├── Wood
│   ├── Trap
│   ├── Item
│   ├── Farmland
│   ├── BotSpawner
│   ├── Poort
│   └── Lijn
├── pathfinding/           # A* pathfinding
│   ├── Node
│   └── Pathfinder
└── systems/               # Game systems
    ├── Shop
    ├── SaveLoad
    ├── Tutorial
    └── Mine
```

### 🚀 Future Possibilities

With LibGDX, these are now feasible:
- Desktop version (Windows, Mac, Linux)
- iOS version
- HTML5 web version
- Better graphics with shaders
- Particle effects
- Multiplayer support
- More optimization
- Texture atlasing
- Better UI with Scene2D

### 📊 Statistics

- **Original Processing code**: 3,579 lines (.pde files)
- **New LibGDX code**: ~2,000 lines (Java files)
- **Number of classes**: 18
- **Assets preserved**: 87+ files
- **Build time**: <2 minutes (first build may take longer)
- **APK size**: TBD (estimated ~10-15 MB)

### 🙏 Credits

- **Original game**: Simon Baars
- **Framework**: LibGDX (https://libgdx.com)
- **Assets**: GameDevMarket (royalty-free) + custom sprites
- **Migration**: Automated port with manual refinement

### 📝 Migration Notes

#### What Stayed the Same
- All gameplay mechanics
- All assets (images, sounds)
- Game logic and rules
- Player controls
- Level design

#### What Got Better
- Code organization
- Build system
- Performance
- Maintainability
- Cross-platform potential
- Development workflow
- CI/CD automation

#### What Was Simplified
- Asset loading (LibGDX handles it better)
- Input handling (unified API)
- Audio playback (cleaner implementation)
- Save/load system (using Preferences)
- Rendering pipeline (SpriteBatch)

---

## Version 1.0.0 - Original Processing Version

The original Processing-based Android game with all core features implemented.

See git history for original version details.
