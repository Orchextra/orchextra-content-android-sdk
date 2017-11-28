package com.gigigo.orchextra.core.sdk.ui.views.textviews;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.ui.FontCache;
import com.gigigo.orchextra.ocm.OCManager;
import views.gigigo.com.textviewautofit.TextFitTextView;

public class OcmNormalTextView extends TextFitTextView {

  private final Context context;

  public OcmNormalTextView(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public OcmNormalTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public OcmNormalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  private void init() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      OcmStyleUi styleUi = injector.provideOcmStyleUi();
      if (styleUi != null && !TextUtils.isEmpty(styleUi.getNormalFonPath())) {
        this.setTypeface(FontCache.getFont(context, styleUi.getNormalFonPath()));
      }
    }
  }
}
