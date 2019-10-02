package com.gigigo.orchextra.core.sdk.di.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

public abstract class BaseInjectionActivity<T> extends BaseActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initDI();
  }

  protected abstract void initDI();
}
