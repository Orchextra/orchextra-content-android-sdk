package com.gigigo.orchextra.core.sdk.di.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

public abstract class BaseInjectionFragment<T> extends BaseFragment {

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    initDI();
  }

  protected abstract void initDI();
}
