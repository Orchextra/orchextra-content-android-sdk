package com.gigigo.orchextra.core.domain.outputs;

import com.gigigo.threaddecoratedview.views.ThreadSpec;

public class BackThreadSpec implements ThreadSpec {
  @Override public void execute(Runnable action) {
    new Thread(action).start();
  }
}
