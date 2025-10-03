# Add project specific ProGuard rules here.

# LibGDX
-dontwarn com.badlogic.gdx.jnigen.**
-dontwarn com.badlogic.gdx.utils.GdxBuild
-dontwarn com.badlogic.gdx.physics.box2d.utils.Box2DBuild
-dontwarn com.badlogic.gdx.jnigen.BuildTarget*
-dontwarn com.badlogic.gdx.graphics.g2d.freetype.FreetypeBuild

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

# Keep classes with constructors used by reflection
-keep public class * extends com.badlogic.gdx.Screen
-keep public class * extends com.badlogic.gdx.Game
-keep public class * extends com.badlogic.gdx.ApplicationListener

# Keep game classes
-keep class com.escapethemaze.game.** { *; }
