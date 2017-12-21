package com.gigigo.orchextra.ocm;

import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.dto.UiVersionData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;

/**
 * Created by francisco.hernandez on 26/5/17.
 */

public interface OcmCallbacks {
  interface Version {
    void onVersionLoaded(UiVersionData version);
    void onVersionFails(Throwable e);
  }

  interface Menus {
    void onMenusLoaded(UiMenuData menus);
    void onMenusFails(Throwable e);
  }

  interface Section {
    void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData);
    void onSectionFails(Exception e);
  }

  interface ClearData {
    void onDataClearSuccess();
    void onDataClearFails(Exception e);
  }
}
