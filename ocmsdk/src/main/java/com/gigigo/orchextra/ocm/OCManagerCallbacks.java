package com.gigigo.orchextra.ocm;

import android.util.Pair;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import java.util.List;

/**
 * Created by francisco.hernandez on 26/5/17.
 */

public interface OCManagerCallbacks {
  interface Menus {
    void onMenusLoaded(UiMenuData menus);
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

  interface QueryParams {
    void onQueryParamsCreated(List<Pair<String, String>> queryString);
  }
}
