package com.gigigo.orchextra.ocm;

import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.List;

/**
 * Created by francisco.hernandez on 26/5/17.
 */

public interface OcmCallbacks {
  interface Menus {
    void onMenusLoaded(List<UiMenu> menus);
    void onMenusFails(Throwable e);
  }
}
