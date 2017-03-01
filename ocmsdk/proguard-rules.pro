# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/josebenito/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepparameternames
-renamesourcefileattribute SourceFile

#https://www.guardsquare.com/en/proguard/manual/attributes
#The following setting preserves the optional attributes that are typically necessary when processing code that is intended to be used as a library:
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep public class com.gigigo.orchextra.ocm.** {public protected *;}
-keep public interface com.gigigo.orchextra.ocm.** {*;}
-keep @interface com.gigigo.orchextra.ocm.**

-keepclasseswithmembernames,includedescriptorclasses class * {native <methods>;}

# Application classes that will be serialized/deserialized over Gson
-keep class com.gigigo.orchextra.core.data.dto.** { *; }

#Youtube Android Api
-keep class com.google.android.youtube.player.**
-keep interface com.google.android.youtube.player.**
-keep enum com.google.android.youtube.player.**
-keepclassmembers class com.google.android.youtube.player.** {
    *;
 }

 #Retrofit 2
 # Platform calls Class.forName on types which do not exist on Android to determine platform.
 -dontnote retrofit2.Platform
 # Platform used when running on RoboVM on iOS. Will not be used at runtime.
 -dontnote retrofit2.Platform$IOS$MainThreadExecutor
 # Platform used when running on Java 8 VMs. Will not be used at runtime.
 -dontwarn retrofit2.Platform$Java8
 # Retain generic type information for use by reflection by converters and adapters.
 -keepattributes Signature
 # Retain declared checked exceptions for use by a Proxy instance.
 -keepattributes Exceptions

#Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

#OkHttp 3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**


#Dagger OX 2
-keepattributes *Annotation*

-keepclassmembers,allowobfuscation class * {
    @orchextra.javax.inject.* *;
    @orchextra.dagger.* *;
    <init>();
}

-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection

-keepnames class orchextra.dagger.Lazy

#Leamplum
-keepclassmembers class * {
  @com.leanplum.annotations.* <fields>;
}
-keep class com.leanplum.** { *; }
-dontwarn com.leanplum.**

#Thread Decorated View  ---- Verficar estas reglas por que peta la app
-keep public class me.panavtec.threaddecoratedview.** {public protected *;}
-keep public interface me.panavtec.threaddecoratedview.** {*;}
-keep @interface me.panavtec.threaddecoratedview.**
-keep @me.panavtec.threaddecoratedview.views.qualifiers.ThreadDecoratedView interface * {
    *;
}

-keepclasseswithmembernames interface * { @me.panavtec.threaddecoratedview.views.qualifiers.NotDecorated *; }