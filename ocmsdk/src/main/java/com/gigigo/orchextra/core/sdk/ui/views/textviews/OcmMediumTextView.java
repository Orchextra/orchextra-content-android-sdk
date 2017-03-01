package com.gigigo.orchextra.core.sdk.ui.views.textviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;

public class OcmMediumTextView extends TextView {

  private final Context context;

  public OcmMediumTextView(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public OcmMediumTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public OcmMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public OcmMediumTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.context = context;

    init();
  }

  private void init() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      OcmStyleUi styleUi = injector.provideOcmStyleUi();
      if (styleUi != null && !TextUtils.isEmpty(styleUi.getMediumFontPath())) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), styleUi.getMediumFontPath()));
      }
    }
  }
}
