package com.gigigo.orchextra.core.sdk.ui;

import android.content.Context;
import android.graphics.Typeface;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nubor on 22/11/2017.
 */

public class FontCache {
  private static Map<String, Typeface> fontMap = new HashMap<String, Typeface>();

  public static Typeface getFont(Context context, String fontName) {
    boolean isFontCacheEnabled =
        true; //hack asv add this bool for check if typefont cache improve performance
    if (fontMap.containsKey(fontName) && isFontCacheEnabled) {
      return fontMap.get(fontName);
    } else {
      Typeface tf = Typeface.createFromAsset(context.getAssets(), fontName);
      fontMap.put(fontName, tf);
      return tf;
    }
  }
}