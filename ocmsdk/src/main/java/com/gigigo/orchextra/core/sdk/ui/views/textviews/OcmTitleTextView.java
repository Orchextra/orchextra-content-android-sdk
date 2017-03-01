package com.gigigo.orchextra.core.sdk.ui.views.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import com.gigigo.orchextra.core.sdk.OcmStyleUi;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.ocm.OCManager;
import me.grantland.widget.AutofitTextView;

public class OcmTitleTextView extends AutofitTextView {

  private final Context context;

  public OcmTitleTextView(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public OcmTitleTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public OcmTitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  private void init() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      OcmStyleUi styleUi = injector.provideOcmStyleUi();
      if (styleUi != null && !TextUtils.isEmpty(styleUi.getTitleFontPath())) {
        //setFont(styleUi.getTitleFontPath());
        setTypeface(Typeface.createFromAsset(context.getAssets(), styleUi.getTitleFontPath()));
      }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      setLetterSpacing(0.4f);
    }

    addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
          removeTextChangedListener(this);
          if (!s.toString().isEmpty()) {
            StringBuilder newText = new StringBuilder();
            int length = s.length();
            for (int i = 0; i < length; i++) {
              newText.append(s.charAt(i)).append(" ");
            }

            setText(newText);
          }
          addTextChangedListener(this);
        }
      }
    });
  }
}
