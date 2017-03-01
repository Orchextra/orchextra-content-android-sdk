package com.gigigo.orchextra.core.sdk.di.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by rui.alonso on 7/9/16.
 */
public abstract class BaseInjectionActivity<T> extends BaseActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initDI();
  }

  protected abstract void initDI();
}
