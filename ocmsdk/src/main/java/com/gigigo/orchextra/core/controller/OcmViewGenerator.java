package com.gigigo.orchextra.core.controller;

import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import java.util.List;
import java.util.List;

public interface OcmViewGenerator {

  List<UiMenu> getMenu();

  UiGridBaseContentData generateGridView(String viewId, String filter);

  UiDetailBaseContentData generateDetailView(String elementUrl);

  UiBaseContentData generatePreview(ElementCachePreview preview, ElementCacheShare share);

  UiBaseContentData generateDetailView(ElementCacheType type, ElementCacheRender elements);

  String getImageUrl(String elementUrl);

  UiSearchBaseContentData generateSearchView();

  UiBaseContentData generateCardDetailView(ElementCache cachedElement);

  UiBaseContentData generateCardPreview(ElementCachePreview preview, ElementCacheShare share);
}

