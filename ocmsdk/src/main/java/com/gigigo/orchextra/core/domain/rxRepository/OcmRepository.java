package com.gigigo.orchextra.core.domain.rxRepository;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import io.reactivex.Observable;

/**
 * Created by francisco.hernandez on 22/5/17.
 */

public interface OcmRepository {
  Observable<MenuContentData> getMenu(boolean useCache);

  Observable<ElementCache> getCachedElement(String elementUrl);

  Observable<ElementCache> getElementCacheBySection(String section);

  Observable<String> getContentUrlBySection(String section);

  Observable<ContentItem> getSectionContentById(String section);
}
