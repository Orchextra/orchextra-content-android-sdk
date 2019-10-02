package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class CardDataView extends FrameLayout {

  public CardDataView(@NonNull Context context) {
    super(context);
  }

  public CardDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CardDataView(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public abstract void initialize();
}
