Orchextra Content	
===================
A library that gives you access to Orchextra Content platform from your Android device.


Getting started
-------------
Start by creating a project in [Orchextra Dashboard](https://dashboard.orchextra.io/start/login), if you haven't done it yet. Go to "Setting" > "SDK Configuration" to get the api key and api secret, you will need these values to start Orchextra SDK.

Installation
-------------
You can check how SDK works with the [:app module](https://github.com/Orchextra/orchextra-content-android-sdk/tree/master/app/src/main/java/com/gigigo/sample) of this repository.

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
        maven { url "https://repo.leanplum.com/" }
    }
}
```

Then we have to add the following dependency into the build.gradle module:
```java
   compile 'com.github.Orchextra:orchextra-content-android-sdk:LAST_VERSION'
```

<img src="https://github.com/Orchextra/orchextra-android-sdk/blob/master/resources/appGradleScreenshot.png" width="300">

and we must sync gradle project.

**NOTE**: You can check the last version of this library in the 'Releases' tab and substitute the LAST_VERSION reference with the last library version. 


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
        
Ocm.initialize(ocmBuilder);
```
**IMPORTANT** you must make this call in **public void onCreate()** of your Application class, if you do not call initialize in this method, the SDK will not initialize properly. You can check that using the logLevel.

**IMPORTANT** if you are using Android Studio 2.1 or higher, and have "Instant Run" enabled, the first time you install the APK is installed in new device, the initialize() spends too much time, maybe a minute on older devices. The second time the problem disappears. To avoid this problem in Android Studio, disables the " Instant Run" from settings-> Build , Execution , Deployment- > Instant Run

Then, in any part of our application we should start the library sdk.

```java
Ocm.start();
```

Change project/authCredentials SDK
-------------
If we doesn't set the new credentials when we initialize the sdk, or change them with new ones. We can set them in any moment during the application life.
```java
Ocm.startWithCredentials(NEW_API_KEY,NEW_API_SECRET, callback);
```
The callback returns the new access token credential. If the credentials have no change, the method do nothing.

Authorization
-------------
Some content of the app is blocked and the user, who use the app, needs to be logged. The setOnDoRequiredLoginCallback method is a listener that is executed when the library needs that the user is logged in the app. The integrative application must execute the login flow and communicate to the sdk that the user is logged in with the following method. If user is log out from the app, we have to execute this method as well.
```java
Ocm.setUserIsAuthorizated(true);
```

Analytics
-------------
The setOnEventCallback method receive some values only for analytics usage. This callback is executed when the user do some actions with the app.

 - *SHARE*: When a content is shared.
 - *CONTENT_START*: When a content is showed.

Custom Schemes
-------------
The setOnCustomSchemeReceiver setOnCustomSchemeReceiver execute the custom schemes that the library receives. 

Language
-------------
Set the content language of the sdk with setContentLanguage method.

Notification  Main Activity
-------------
You can set the main activity to execute the sdk notification.

Styling Sdk
-------------
You can set the style sdk with the following class.
```java
OcmStyleUiBuilder ocmStyleUiBuilder =
        new OcmStyleUiBuilder().setTitleFontAssetsPath("fonts/Gotham-Ultra.ttf")
            .setNormalFont("fonts/Gotham-Book.ttf")
            .setMediumFont("fonts/Gotham-Medium.ttf")
            .setLightFont("fonts/Gotham-Light.ttf");
    Ocm.setStyleUi(ocmStyleUiBuilder);
```
Setting Business Unit
-------------
```java
Ocm.setBusinessUnit("it");
```

####Menu
You can retrieve the menu of your content project with the following method
```java
Ocm.getMenus(new OnRetrieveUiMenuListener());
```

#####Content Grid
When you retrieve the menu info, you can load the data from the each menu with the following method. The UiGridBaseContentData class is a fragment, which you can add to some activity.
```java
UiGridBaseContentData uiGridBaseContentData =
        Ocm.generateGridView(uiMenu.get(tab.getPosition()).getElementUrl(), filter);
```
