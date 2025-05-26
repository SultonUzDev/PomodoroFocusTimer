# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

############## Kotlin Standard ##############
-keepclassmembers class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontnote kotlin.**

############## Room ##############
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * {
    @androidx.room.* <methods>;
    @androidx.room.* <fields>;
}
-keep @androidx.room.TypeConverters class *
-keepclassmembers class * {
    @androidx.room.TypeConverter <methods>;
}
############## Dagger / Hilt Core ##############

# Keep all Hilt and Dagger core classes
-keep class dagger.hilt.** { *; }
-keep class dagger.** { *; }
-dontwarn dagger.hilt.**
-dontwarn dagger.**
-dontwarn javax.inject.**

############## Keep Hilt Application & Entry Points ##############

-keep class * {
    @dagger.hilt.android.HiltAndroidApp <methods>;
}
-keep class * {
    @dagger.hilt.android.AndroidEntryPoint <methods>;
}

############## Keep Dagger Modules and Bindings ##############

-keep class * {
    @dagger.Module <methods>;
}
-keep class * {
    @dagger.Provides <methods>;
}
-keep class * {
    @dagger.Binds <methods>;
}
-keep class * {
    @dagger.BindsInstance <methods>;
}
-keep class * {
    @dagger.hilt.InstallIn <methods>;
}

############## Keep Injected Classes ##############

# Keep constructors or fields annotated with @Inject
-keepclassmembers class * {
    @javax.inject.Inject <init>(...);
    @javax.inject.Inject *;
}

############## Keep Singleton-scoped classes (optional but helpful) ##############

-keep class * {
    @javax.inject.Singleton <methods>;
}

############## Optional: Avoid Hilt-related warnings ##############

-dontwarn dagger.internal.**
-dontwarn dagger.hilt.internal.**
-dontwarn androidx.hilt.**

-keep class *ViewModel { *; }


############## Jetpack Navigation ##############
-keepclassmembers class * {
    @androidx.navigation.NavArgs <methods>;
    @androidx.navigation.NavArgs <fields>;
}
-keep class androidx.navigation.NavController { *; }
-keep class androidx.navigation.NavGraph { *; }
-dontwarn androidx.navigation.**

############## Coroutines / Flow ##############
-dontwarn kotlinx.coroutines.**

############## Jetpack Compose (if using minify) ##############
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

############## Optional Debugging Helps ##############
# Keep ViewModels for easier debugging
-keep class *ViewModel { *; }
