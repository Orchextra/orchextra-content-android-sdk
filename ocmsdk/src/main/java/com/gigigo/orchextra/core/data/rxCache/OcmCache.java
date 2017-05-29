package com.gigigo.orchextra.core.data.rxCache;

import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import io.reactivex.Observable;

public interface OcmCache {

  Observable<ApiMenuContentDataResponse> getMenus();

  void putMenus(ApiMenuContentDataResponse apiMenuContentDataResponse);

  boolean isMenuCached();

  boolean isMenuExpired();

}
