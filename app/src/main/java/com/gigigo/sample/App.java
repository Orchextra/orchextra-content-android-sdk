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
//import com.gigigo.vuforiaimplementation.ImageRecognitionVuforiaImpl;
import com.squareup.leakcanary.LeakCanary;

public class App extends MultiDexApplication {

  //public static String API_KEY = "a2966ba69f4ead1a4f1550bfda450e9fd07e6762";   //Asv project
  //public static String API_SECRET = "f79713d7e9b0fcd69fedfb94f471106cb85d8ca4";

  //WOAH RELEASE
  //public static String API_KEY = "8286702045adf5a3ad816f70ecb80e4c91fbb8de";
  //public static String API_SECRET = "eab37080130215ced60eb9d5ff729049749ec205";

  //WOAH Debug
  public static String API_KEY = "8286702045adf5a3ad816f70ecb80e4c91fbb8de";
  public static String API_SECRET = "eab37080130215ced60eb9d5ff729049749ec205";

  //[UAT][CSE] - WOAH SITC STAGING
  //public static String API_KEY = "adfc8ba4340828a054bf061f692707a197af96cb";
  //public static String API_SECRET = "677cf75a17aeec144ee402c281ad3a732d736a8a";

  //WOAH MARKETS RELEASE
  //public static String API_KEY = "ef08c4dccb7649b9956296a863db002a68240be2";    //Woah project
  //public static String API_SECRET = "6bc18c500546f253699f61c11a62827679178400";

  //WOAH QA RELEASE
  //public static String API_KEY = "ad46332237cbb9fd38ad38470a9bee6d4892d770";    //Woah project
  //public static String API_SECRET = "f5e2dd3ba0de46964775fc1b48c4895c73d263c5";

  //REPSOL RELEASE
  //public static String API_KEY = "7bb9fa0f9b7a02846383fd6284d3c74b8155644c";
  //public static String API_SECRET = "3295dc8de90300e2977e6cec5b28b614fc644934";

  //REPSOL DEBUG
  //public static String API_KEY = "53cf8785f1f02b5a69adbddee58bb33b6094eb5b";    //Woah project
  //public static String API_SECRET = "e1d718aefde8aab04621a7acf1771dfbf5884fef";

  //[UAT][INTERNAL] DEMO APP
  //public static String API_KEY = "338d65a6572be208f25a9a5815861543adaa4abb";
  //public static String API_SECRET = "b29dac01598f9d8e2102aef73ac816c0786843ef";




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
    enableStrictMode();
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      //This process is dedicated to LeakCanary for heap analysis.
      //You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    //// Normal app init code...

    MultiDex.install(this);
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
        .setOrchextraCredentials(API_KEY, API_SECRET)
        .setContentLanguage("EN")
        .setOnEventCallback(onEventCallback);

    Ocm.initialize(ocmBuilder);

    OcmStyleUiBuilder ocmStyleUiBuilder =
        new OcmStyleUiBuilder().setTitleFont("fonts/Gotham-Ultra.ttf")
            .setNormalFont("fonts/Gotham-Book.ttf")
            .setMediumFont("fonts/Gotham-Medium.ttf")
            .setTitleToolbarEnabled(false)
            .setEnabledStatusBar(false);
    Ocm.setStyleUi(ocmStyleUiBuilder);

    Ocm.setBusinessUnit("e2e");
  }

  private void enableStrictMode() {
    StrictMode.setThreadPolicy(
        new StrictMode.ThreadPolicy.Builder().detectAll()   // or .detectAll() for all detectable problems
            .penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
  }
}
