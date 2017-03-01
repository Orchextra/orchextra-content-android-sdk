package com.gigigo.orchextra.core.sdk.di.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by rui.alonso on 7/9/16.
 */
public abstract class BaseInjectionFragment<T> extends BaseFragment {

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    initDI();
  }

  protected abstract void initDI();
}
