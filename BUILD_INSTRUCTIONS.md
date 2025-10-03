# Build Instructions

## Prerequisites

1. **Java Development Kit (JDK) 17 or higher**
   ```bash
   java -version
   # Should show version 17 or higher
   ```

2. **Android SDK** (for Android builds)
   - Install Android Studio, or
   - Install command-line tools from https://developer.android.com/studio

3. **Environment Variables**
   ```bash
   export ANDROID_HOME=/path/to/android/sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

## Quick Start

### Build Debug APK
```bash
./gradlew assembleDebug
```
Output: `android/build/outputs/apk/debug/android-debug.apk`

### Build Release APK
```bash
./gradlew assembleRelease
```
Output: `android/build/outputs/apk/release/android-release-unsigned.apk`

### Install on Connected Device
```bash
# Enable USB debugging on your Android device
./gradlew installDebug
```

### Run on Device
```bash
./gradlew android:run
```

## Detailed Build Options

### Clean Build
```bash
./gradlew clean
./gradlew assembleDebug
```

### Build with Stacktrace (for debugging)
```bash
./gradlew assembleDebug --stacktrace
```

### Build with Info Logging
```bash
./gradlew assembleDebug --info
```

### Run Tests
```bash
./gradlew test
./gradlew core:test
```

### Generate Test Reports
```bash
./gradlew test
# Open: build/reports/tests/test/index.html
```

## Signing Release APK

### Create Keystore
```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

### Configure Signing
Create `android/keystore.properties`:
```properties
storeFile=../my-release-key.jks
storePassword=your-store-password
keyAlias=my-key-alias
keyPassword=your-key-password
```

Add to `android/build.gradle`:
```gradle
def keystorePropertiesFile = rootProject.file("android/keystore.properties")
def keystoreProperties = new Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        release {
            if (keystorePropertiesFile.exists()) {
                storeFile file(keystoreProperties['storeFile'])
                storePassword keystoreProperties['storePassword']
                keyAlias keystoreProperties['keyAlias']
                keyPassword keystoreProperties['keyPassword']
            }
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
        }
    }
}
```

### Build Signed APK
```bash
./gradlew assembleRelease
```

## Troubleshooting

### Gradle Version Issues
If you get Gradle version errors:
```bash
./gradlew wrapper --gradle-version 8.4
```

### Android SDK Not Found
```bash
# Create local.properties
echo "sdk.dir=/path/to/android/sdk" > local.properties
```

### Out of Memory
Increase Gradle memory in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xms512M -Xmx4G
```

### Cannot Resolve Dependencies
Check your internet connection and try:
```bash
./gradlew build --refresh-dependencies
```

### Permission Denied on gradlew
```bash
chmod +x gradlew
```

## CI/CD Build (GitHub Actions)

The project automatically builds on GitHub Actions:

1. **Push to master/main**: Triggers build
2. **View workflows**: Go to Actions tab on GitHub
3. **Download APKs**: Click on workflow run → Artifacts

### Manual Trigger
Go to Actions → Android CI/CD → Run workflow

### Create Release
```bash
git tag v1.0.0
git push origin v1.0.0
```
This creates a GitHub release with APKs attached.

## Platform-Specific Notes

### Windows
Use `gradlew.bat` instead of `./gradlew`:
```cmd
gradlew.bat assembleDebug
```

### macOS/Linux
Make sure gradlew is executable:
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### Android Studio
1. Open project in Android Studio
2. Select `android` module
3. Click Run → Run 'android'

## Build Variants

### Debug
- Debuggable
- Not optimized
- Fast build
```bash
./gradlew assembleDebug
```

### Release
- Not debuggable
- ProGuard optimized
- Signed (if configured)
```bash
./gradlew assembleRelease
```

## Testing on Emulator

### Create AVD (Android Virtual Device)
```bash
# List available system images
sdkmanager --list

# Install system image
sdkmanager "system-images;android-33;google_apis;x86_64"

# Create AVD
avdmanager create avd -n Pixel_5_API_33 -k "system-images;android-33;google_apis;x86_64" -d pixel_5
```

### Launch Emulator
```bash
emulator -avd Pixel_5_API_33
```

### Install and Run
```bash
./gradlew installDebug
adb shell am start -n com.escapethemaze.android/com.escapethemaze.android.AndroidLauncher
```

## IDE Setup

### IntelliJ IDEA / Android Studio
1. File → Open → Select `build.gradle`
2. Wait for Gradle sync
3. Select run configuration: `android`
4. Click Run

### Eclipse
```bash
./gradlew eclipse
```
Then import as existing project.

### VSCode
Install extensions:
- Java Extension Pack
- Gradle for Java
- Android IDE

## Common Commands Reference

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew build

# Install
./gradlew installDebug
./gradlew installRelease

# Clean
./gradlew clean

# Test
./gradlew test
./gradlew core:test

# Run
./gradlew android:run

# List tasks
./gradlew tasks

# Dependencies
./gradlew dependencies
./gradlew core:dependencies

# Project info
./gradlew projects
```

## Performance Tips

1. **Enable Gradle daemon**: Already configured in `gradle.properties`
2. **Enable parallel execution**: Add to `gradle.properties`:
   ```properties
   org.gradle.parallel=true
   ```
3. **Use build cache**:
   ```properties
   org.gradle.caching=true
   ```

## Getting Help

- [LibGDX Setup Guide](https://libgdx.com/wiki/start/setup)
- [Gradle Build Tool](https://gradle.org/)
- [Android Developer Docs](https://developer.android.com/)

For project-specific issues, please open an issue on GitHub.
