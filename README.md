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
   compile 'com.github.Orchextra:orchextra-content-android-sdk:LAST_VERSION'
```

<img src="https://github.com/Orchextra/orchextra-android-sdk/blob/master/resources/appGradleScreenshot.png" width="300">

and we must sync gradle project.

You can check the last version of this library in the 'Releases' tab and substitute the LAST_VERSION reference with the last library version. 


Integrating SDK
-------------
We have to created a class which **extends from Application** (if we didn't do yet) and add the Orchextra Content init method. 

```java
OcmBuilder ocmBuilder =
        new OcmBuilder(this)
        .setOrchextraCredentials(API_KEY, API_SECRET)
        .setOnDoRequiredLoginCallback(doRequiredLoginCallback)
        .setOnEventCallback(eventCallback)
        .setOnCustomSchemeReceiver(onCustomSchemeReceiver)
        .setContentLanguage(getContentLanguage())
        .setNotificationActivityClass(MainActivity.class);
```
**IMPORTANT** you must make this call in **public void onCreate()** of your Application class, if you do not call initialize in this method, the SDK will not initialize properly. You can check that using the logLevel.

**IMPORTANT** if you are using Android Studio 2.1 or higher, and have "Instant Run" enabled, the first time you install the APK is installed in new device, the initialize() spends too much time, maybe a minute on older devices. The second time the problem disappears. To avoid this problem in Android Studio, disables the " Instant Run" from settings-> Build , Execution , Deployment- > Instant Run

Then, in any part of our application we should start the library sdk.

```java
Ocm.start();
```
Change project/authCredentials SDK
-------------
In the new version we set the new project authCredentials when we initialize the sdk, if we want to change the Ox Project, we can call it in any moment.
```java
Ocm.startWithCredentials(NEW_API_KEY,NEW_API_SECRET, callback);
```
The callback returns the new access token credential. If the credentials have no change, the method do nothing.
