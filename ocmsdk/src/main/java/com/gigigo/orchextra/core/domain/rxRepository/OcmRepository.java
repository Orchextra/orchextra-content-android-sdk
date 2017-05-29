package com.gigigo.orchextra.core.domain.rxRepository;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import io.reactivex.Observable;

/**
 * Created by francisco.hernandez on 22/5/17.
 */

public interface OcmRepository {
  Observable<MenuContentData> getMenu(boolean forceReload);

  Observable<ContentData> getSectionElements(boolean forceReload, String elementUrl);

  Observable<ElementCache> getDetail(String section);

  Observable<ContentItem> doSearch(String textToSearch);
}
