# Android R8 / ProGuard rules for Devfolio

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses, EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
    @kotlinx.serialization.Serializable <fields>;
}
-keepclassmembers class *$$serializer {
    public static final *$$serializer INSTANCE;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.masum.devfolio.model.** { *; }
-keep class com.masum.devfolio.data.model.** { *; }
-keep class com.masum.devfolio.data.hackathon.** { *; }
-keep class com.masum.devfolio.data.auth.** { *; }

# Ktor & OkHttp
-dontwarn io.ktor.**
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class io.ktor.** { *; }
-keep class okhttp3.** { *; }

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Compose & Coil
-dontwarn coil3.**
-keep class coil3.** { *; }

# Google Tink & Security Crypto annotations
-dontwarn com.google.errorprone.annotations.**

