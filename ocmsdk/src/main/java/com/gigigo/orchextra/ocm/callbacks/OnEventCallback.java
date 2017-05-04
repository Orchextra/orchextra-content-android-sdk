package com.gigigo.orchextra.ocm.callbacks;

import com.gigigo.orchextra.ocm.OcmEvent;

public interface OnEventCallback {
  void doEvent(OcmEvent event, Object data);
  void doEvent(OcmEvent event);
}
