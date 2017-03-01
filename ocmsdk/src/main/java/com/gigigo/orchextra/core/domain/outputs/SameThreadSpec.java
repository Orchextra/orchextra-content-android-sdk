package com.gigigo.orchextra.core.domain.outputs;

import com.gigigo.threaddecoratedview.views.ThreadSpec;

public class SameThreadSpec implements ThreadSpec {
  @Override public void execute(Runnable action) {
    action.run();
  }
}
