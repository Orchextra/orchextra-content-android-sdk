package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class CardViewElement extends FrameLayout {

  public CardViewElement(@NonNull Context context) {
    super(context);
  }

  public CardViewElement(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CardViewElement(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public abstract void initialize();
}
