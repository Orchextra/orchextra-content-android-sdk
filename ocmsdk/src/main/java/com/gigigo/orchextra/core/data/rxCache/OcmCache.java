package com.gigigo.orchextra.core.data.rxCache;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import io.reactivex.Observable;
import java.util.List;

public interface OcmCache {

  Observable<ApiMenuContentData> getMenus();

  void putMenus(ApiMenuContentData apiMenuContentData);

  boolean isMenuCached();

  boolean isMenuExpired();

  Observable<ApiSectionContentData> getSection(String elementUrl);

  void putSection(ApiSectionContentData apiSectionContentData);

  boolean isSectionCached(String elementUrl);

  boolean isSectionExpired(String elementUrl);

  Observable<ApiElementData> getDetail(String slug);

  void putDetail(ApiElementData apiElementData);

  boolean isDetailCached(String slug);

  boolean isDetailExpired(String slug);

  void evictAll();
}
