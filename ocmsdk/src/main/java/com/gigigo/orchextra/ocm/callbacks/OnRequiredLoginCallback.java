package com.gigigo.orchextra.ocm.callbacks;

import android.widget.ImageView;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import java.lang.ref.WeakReference;

public interface OnRequiredLoginCallback {
  void doRequiredLogin();
  void doLoggedActionRequired(ElementCache elementCache, String elementUrl, String urlImageToExpand,
      int widthScreen, int heightScreen, WeakReference<ImageView> imageViewWeakReference);
}
