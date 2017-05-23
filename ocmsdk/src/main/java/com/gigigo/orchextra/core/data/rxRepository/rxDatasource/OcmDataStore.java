package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import io.reactivex.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface OcmDataStore {

  /**
   * Get an {@link Observable} which will emit a {@link ApiMenuContentDataResponse}.
   */
  Observable<ApiMenuContentDataResponse> getMenuEntity(boolean forceReload);
  /**
   * Get an {@link Observable} which will emit a {@link ApiSectionContentDataResponse}.
   */
  Observable<ApiSectionContentDataResponse> getSectionEntity(String elementUrl);
  /**
   * Get an {@link Observable} which will emit a {@link ApiSectionContentDataResponse}.
   */
  Observable<ApiSectionContentDataResponse> searchByText(String section);
  /**
   * Get an {@link Observable} which will emit a {@link ApiElementDataResponse}.
   */
  Observable<ApiElementDataResponse> getElementById(String section);
}
