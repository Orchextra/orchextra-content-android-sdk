package com.gigigo.orchextra.core.controller;

import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;

import java.util.List;

public interface OcmViewGenerator {

  void generateSectionView(String viewId, String filter, int imagesToDownload,
      GetSectionViewGeneratorCallback getSectionViewGeneratorCallback);

  UiDetailBaseContentData generateDetailView(String elementUrl);

  UiBaseContentData generatePreview(ElementCachePreview preview, ElementCacheShare share);

  UiBaseContentData generateDetailView(ElementCacheType type, ElementCacheRender elements);

  void getImageUrl(String elementUrl,
      GetDetailImageViewGeneratorCallback getDetailImageViewGeneratorCallback);

  UiSearchBaseContentData generateSearchView();

  UiBaseContentData generateCardDetailView(ElementCache cachedElement);

  UiBaseContentData generateCardPreview(ElementCachePreview preview, ElementCacheShare share);

  void getMenu(boolean forceReload, GetMenusViewGeneratorCallback getMenusViewGeneratorCallback);

  // Callbacks
  interface GetMenusViewGeneratorCallback {
    void onGetMenusLoaded(UiMenuData menus);

    void onGetMenusFails(Throwable e);
  }

  interface GetDetailImageViewGeneratorCallback {
    void onGetImageLoaded(String imagePath);

    void onGetImageError(Exception e);
  }

  interface GetSectionViewGeneratorCallback {
    void onSectionViewLoaded(UiGridBaseContentData uiGridBaseContentData);

    void onSectionViewFails(Exception e);
  }
}

