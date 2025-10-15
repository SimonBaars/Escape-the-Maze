# Gradle 8.4 Upgrade Guide

## Overview

This document describes the upgrade to Gradle 8.4 and the associated changes made to ensure build compatibility.

## What Was Updated

### 1. Gradle Version
- **Current**: 8.4
- **Configuration**: `gradle/wrapper/gradle-wrapper.properties`
- **Benefits**: Latest features, performance improvements, better Java 21 support

### 2. Android Gradle Plugin (AGP)
- **Previous**: 8.1.1
- **Current**: 8.2.2
- **Reason**: AGP 8.2.2 is specifically tested and optimized for Gradle 8.4+

### 3. Build Configuration
- **Java Version**: 17 (required for AGP 8.x)
- **LibGDX Version**: 1.12.1 (compatible with Gradle 8.4)
- **Compile SDK**: 33
- **Min SDK**: 21
- **Target SDK**: 33

## Compatibility Matrix

| Component | Version | Status |
|-----------|---------|--------|
| Gradle | 8.4 | ✅ |
| Android Gradle Plugin | 8.2.2 | ✅ |
| Java/JDK | 17 | ✅ |
| LibGDX | 1.12.1 | ✅ |
| Android SDK | 33 | ✅ |

## Previous Issues and Solutions

### Issue: Compilation Errors with Gradle 8.4

When first attempting to build with Gradle 8.4, the build failed with errors like:
```
error: cannot find symbol: class PApplet
error: cannot find symbol: class Node
error: cannot find symbol: class PImage
error: package processing.core does not exist
```

**Root Cause**: Legacy Processing code in `android/src/processing/test/escapethemazeandroid/EscapeTheMazeAndroid.java` was being compiled.

**Solution**: The legacy Processing files were removed (see `COMPILE_FIX_NOTES.md` for details):
- Deleted `android/src/processing/test/escapethemazeandroid/EscapeTheMazeAndroid.java`
- Deleted Processing library JARs from `android/libs/`
- The project now only uses the LibGDX launcher at `android/src/com/escapethemaze/android/AndroidLauncher.java`

### Issue: AGP Compatibility

**Previous**: AGP 8.1.1 was tested with Gradle up to 8.3
**Solution**: Updated to AGP 8.2.2 which is specifically tested with Gradle 8.4+

## Verification Steps

To verify the build works with Gradle 8.4:

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test
```

## Troubleshooting

### If you get "Unsupported class file major version" errors
- Ensure you're using JDK 17 or higher
- Check: `java -version`

### If you get Gradle version errors
- The project uses Gradle wrapper, so you don't need to install Gradle separately
- The correct version is automatically downloaded when you run `./gradlew`

### If you get AGP version errors
- Verify `build.gradle` has: `classpath 'com.android.tools.build:gradle:8.2.2'`

### If you get dependency resolution errors
- Check your internet connection
- Try: `./gradlew build --refresh-dependencies`

## CI/CD

The GitHub Actions workflow (`.github/workflows/android-build.yml`) is configured to:
1. Use JDK 17
2. Build with Gradle 8.4
3. Generate both debug and release APKs
4. Upload artifacts

## Further Information

- [Gradle 8.4 Release Notes](https://docs.gradle.org/8.4/release-notes.html)
- [Android Gradle Plugin 8.2 Release Notes](https://developer.android.com/build/releases/gradle-plugin#8-2-0)
- [LibGDX Documentation](https://libgdx.com/wiki/)

## Summary

The project is now fully compatible with Gradle 8.4. The upgrade required:
1. Removing legacy Processing code (completed in PR #6)
2. Updating AGP to 8.2.2 (completed in this PR)
3. Verifying all dependencies and configurations

The build system is now modern, performant, and ready for future updates.
