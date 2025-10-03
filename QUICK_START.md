# Quick Start Guide

## For Developers

### Prerequisites
- JDK 11 or higher
- Android SDK (automatically downloaded by Gradle if needed)

### Build the Game

**1. Debug Build (for testing)**
```bash
./gradlew android:assembleDebug
```
Output: `android/build/outputs/apk/debug/android-debug.apk`

**2. Release Build (for distribution)**
```bash
./gradlew android:assembleRelease
```
Output: `android/build/outputs/apk/release/android-release-unsigned.apk`

**3. Clean Build (start fresh)**
```bash
./gradlew clean android:assembleDebug
```

### Install on Device

```bash
# Using ADB
adb install android/build/outputs/apk/debug/android-debug.apk

# Or drag-and-drop the APK to your device
```

### Run the Build

```bash
./gradlew android:installDebug
./gradlew android:run
```

## For Users

### Download Pre-built APK

1. Go to the GitHub Actions tab
2. Click on the latest successful build
3. Download the `app-debug` or `app-release` artifact
4. Extract the ZIP file
5. Install the APK on your Android device

### Game Controls

**Movement:**
- W/↑ - Move Up
- S/↓ - Move Down
- A/← - Move Left
- D/→ - Move Right
- Shift - Run (when unlocked)

**Actions:**
- E - Use selected item
- ESC - Pause menu

**Gameplay:**
- Survive by gathering food
- Explore the maze during the day
- Find shelter at night
- Collect items and craft tools
- Escape the maze!

## Project Structure Quick Reference

```
📁 Escape-the-Maze/
├── 📁 .github/workflows/        # CI/CD pipeline
├── 📁 android/                  # Android app
│   ├── 📁 assets/              # Game assets (images, sounds)
│   ├── 📁 src/                 # Android launcher
│   ├── AndroidManifest.xml     # App configuration
│   └── build.gradle            # Android build config
├── 📁 core/                     # Game logic
│   └── 📁 src/com/escapethemaze/game/
│       ├── EscapeTheMaze.java  # Main game
│       ├── 📁 entities/        # Game objects
│       ├── 📁 pathfinding/     # AI navigation
│       └── 📁 systems/         # Game systems
├── 📁 gradle/wrapper/           # Gradle wrapper
├── build.gradle                 # Root build config
├── settings.gradle              # Module settings
└── gradlew / gradlew.bat       # Build scripts
```

## Common Commands

```bash
# List all available tasks
./gradlew tasks

# Build everything
./gradlew build

# Clean all builds
./gradlew clean

# Check dependencies
./gradlew dependencies

# Android-specific tasks
./gradlew android:tasks
```

## Troubleshooting

### Build fails with "SDK not found"
Create a `local.properties` file with:
```
sdk.dir=/path/to/your/android/sdk
```

### Out of memory during build
Increase Gradle memory in `gradle.properties`:
```
org.gradle.jvmargs=-Xms512M -Xmx4096M
```

### Clean build takes too long
Gradle caches dependencies. First build takes longer, subsequent builds are faster.

### APK not installing
- Enable "Install from Unknown Sources" on your device
- Make sure you have enough storage space
- Try uninstalling the old version first

## More Information

- **Full details**: See `PROJECT_SUMMARY.md`
- **Migration info**: See `MIGRATION.md`
- **General info**: See `README.md`
- **CI/CD**: `.github/workflows/android-build.yml`

## Support

- Report issues on GitHub
- Check existing issues and pull requests
- Read the documentation files

---
**Built with LibGDX** | **Original by Simon Baars**
