package com.gigigo.orchextra.ocm;

import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;

import java.util.List;

/**
 * Created by francisco.hernandez on 26/5/17.
 */

public interface OCManagerCallbacks {
  interface Menus {
    void onMenusLoaded(List<UiMenu> menus);
    void onMenusFails(Throwable e);
  }

  interface Section {
    void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData);
    void onSectionFails(Exception e);
  }

  interface Clear {
    void onDataClearedSuccessfull();
    void onDataClearFails(Exception e);
  }
}
