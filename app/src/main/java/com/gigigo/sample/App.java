package com.gigigo.sample;

import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.gigigo.orchextra.core.controller.model.grid.ImageTransformReadArticle;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmBuilder;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder;
import com.gigigo.orchextra.ocm.callbacks.OnEventCallback;
import com.gigigo.orchextra.ocm.callbacks.OnRequiredLoginCallback;
import com.squareup.leakcanary.LeakCanary;

//import com.gigigo.vuforiaimplementation.ImageRecognitionVuforiaImpl;

public class App extends MultiDexApplication {

  /**
  / Project credentials are moved to variants.gradle file
   */

  private OnRequiredLoginCallback onDoRequiredLoginCallback =  new OnRequiredLoginCallback() {
    @Override public void doRequiredLogin() {

    }

    @Override public void doRequiredLogin(String elementUrl) {

    }
  };
  private OnEventCallback onEventCallback = new OnEventCallback() {
    @Override public void doEvent(OcmEvent event, Object data) {
    }

    @Override public void doEvent(OcmEvent event) {
    }
  };

  @Override public void onCreate() {
    System.out.println("appOn1");
    enableStrictMode();
    System.out.println("appOn2");
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      //This process is dedicated to LeakCanary for heap analysis.
      //You should not init your app in this process.
      return;
    }
    System.out.println("appOn3");
    LeakCanary.install(this);
    //// Normal app init code...
    System.out.println("appOn4");
    MultiDex.install(this);
    System.out.println("appOn5");
    //region normal Filters., perfect 4 documentation
/*
    switch (transform) {
      case 0:
        return new GrayscaleTransformation(mApplication);
      case 1:
        return new BlurTransformation(mApplication);
      case 2:
        return new ColorFilterTransformation(mApplication, Color.RED);
      case 3:
        return new CropCircleTransformation(mApplication);
      case 4:
        return new CropSquareTransformation(mApplication);
      case 5:
        return new RoundedCornersTransformation(mApplication, 25,
            25);//return new MaskTransformation(mApplication, 50); //fails
      case 6:
        return new RoundedCornersTransformation(mApplication, 25, 25);
      case 7:
        return new BrightnessFilterTransformation(mApplication);
      case 8:
        return new ContrastFilterTransformation(mApplication);
      case 9:
        return new InvertFilterTransformation(mApplication);
      case 10:
        return new KuwaharaFilterTransformation(mApplication);
      case 11:
        return new PixelationFilterTransformation(mApplication);
      case 12:
        return new SepiaFilterTransformation(mApplication);
      case 13:
        return new SketchFilterTransformation(mApplication);
      case 14:
        return new SwirlFilterTransformation(mApplication);
      case 15:
        return new ToonFilterTransformation(mApplication);
      default:
        transform = -1;
        return new VignetteFilterTransformation(mApplication);
    }
     */
    //endregion

    //asv READ ARTICLES DOCU
    // x defecto el transform es overlay si no se setea el mode
    //x defecto es grayscale si se pone mode bitmap xo no se alimenta un bitmaptransform
    //x defecto se guardarán hasta 100? articulos, y pueden ser un maximo de 200

    OcmBuilder ocmBuilder = new OcmBuilder(this).setNotificationActivityClass(MainActivity.class)
        .setShowReadArticles(true)
        //.setVuforiaImpl(new ImageRecognitionVuforiaImpl())
        .setTransformReadArticleMode(ImageTransformReadArticle.BITMAP_TRANSFORM)
       // .setVuforiaImpl(new ImageRecognitionVuforiaImpl())
        .setMaxReadArticles(100)
        .setOrchextraCredentials(BuildConfig.API_KEY, BuildConfig.API_SECRET)
        .setContentLanguage("EN")
        .setOnEventCallback(onEventCallback);
    System.out.println("appOn6");
    Ocm.initialize(ocmBuilder);
    System.out.println("appOn7");
    OcmStyleUiBuilder ocmStyleUiBuilder =
        new OcmStyleUiBuilder().setTitleFont("fonts/Gotham-Ultra.ttf")
            .setNormalFont("fonts/Gotham-Book.ttf")
            .setMediumFont("fonts/Gotham-Medium.ttf")
            .setTitleToolbarEnabled(false)
            //.disableThumbnailImages()
            .setEnabledStatusBar(false);
    Ocm.setStyleUi(ocmStyleUiBuilder);
    System.out.println("appOn8");
    Ocm.setBusinessUnit(BuildConfig.BUSSINES_UNIT);
    System.out.println("appOn9");
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }
}
