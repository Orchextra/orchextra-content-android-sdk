package com.gigigo.orchextra.core.sdk.ui.views.edittexts;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.ui.FontCache;
import com.gigigo.orchextra.ocm.OCManager;

public class OcmTitleEditText extends android.support.v7.widget.AppCompatEditText {

  private final Context context;

  public OcmTitleEditText(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public OcmTitleEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public OcmTitleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  private void init() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      OcmStyleUi styleUi = injector.provideOcmStyleUi();
      if (styleUi != null && !TextUtils.isEmpty(styleUi.getTitleFontPath())) {
        this.setTypeface(FontCache.getFont(context, styleUi.getTitleFontPath()));
      }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      setLetterSpacing(0.4f);
    }
  }
}
