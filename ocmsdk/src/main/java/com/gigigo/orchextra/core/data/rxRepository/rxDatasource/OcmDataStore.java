package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import io.reactivex.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface OcmDataStore {

  /**
   * Get an {@link Observable} which will emit a {@link ApiMenuContentDataResponse}.
   */
  Observable<ApiMenuContentData> getMenuEntity();

  /**
   * Get an {@link Observable} which will emit a {@link ApiSectionContentDataResponse}.
   */
  Observable<ApiSectionContentData> getSectionEntity(String elementUrl);

  /**
   * Get an {@link Observable} which will emit a {@link ApiSectionContentDataResponse}.
   */
  Observable<ApiSectionContentData> searchByText(String section);

  /**
   * Get an {@link Observable} which will emit a {@link ApiElementDataResponse}.
   */
  Observable<ApiElementData> getElementById(String section);
}
