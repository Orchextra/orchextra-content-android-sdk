package com.gigigo.orchextra.core.sdk.ui.views.textviews;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.ui.FontCache;
import views.gigigo.com.textviewautofit.TextFitTextView;
import com.gigigo.orchextra.ocm.OCManager;

public class OcmLightTextView extends TextFitTextView {

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

  private void init() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      OcmStyleUi styleUi = injector.provideOcmStyleUi();
      if (styleUi != null && !TextUtils.isEmpty(styleUi.getLightFontPath())) {
        this.setTypeface(FontCache.getFont(context, styleUi.getLightFontPath()));
      }
    }
  }
}
