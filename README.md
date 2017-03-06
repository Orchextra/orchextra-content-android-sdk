Orchextra Content	
===================
A library that gives you access to Orchextra Content platform from your Android device.


Getting started
-------------
Start by creating a project in Orchextra Dashboard, if you haven't done it yet. Go to "Setting" > "SDK Configuration" to get the api key and api secret, you will need these values to start Orchextra SDK.

Installation
-------------
You can check how SDK works with the :app module of this repository.

Requirements
-------------
Orchextra can be integrated in Android Gingerbread (v. 10) or later.

Adding the dependency
-------------
We have to add the gradle dependencies. In our rootproject **build.gradle** file, we add the following maven dependency. This is required in order to advice gradle that it has to look for Orchextra sdk inside **jitpack.io** maven repository. Gradle file is this one:

<img src="https://github.com/Orchextra/orchextra-android-sdk/blob/master/resources/rootGradleScreenshot.png" width="300">

```java
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Then we have to add the following dependency into this file:
```java
   compile 'com.github.Orchextra:orchextra-content-android-sdk:1.0.1'
```

<img src="https://github.com/Orchextra/orchextra-android-sdk/blob/master/resources/appGradleScreenshot.png" width="300">

and we must sync gradle project.
