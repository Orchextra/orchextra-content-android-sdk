package com.gigigo.orchextra.core.sdk.ui.views.textviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;

public class OcmLightTextView extends TextView {

  private final Context context;

  public OcmLightTextView(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public OcmLightTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public OcmLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public OcmLightTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.context = context;

    init();
  }

  private void init() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      OcmStyleUi styleUi = injector.provideOcmStyleUi();
      if (styleUi != null && !TextUtils.isEmpty(styleUi.getLightFontPath())) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), styleUi.getLightFontPath()));
      }
    }
  }
}
