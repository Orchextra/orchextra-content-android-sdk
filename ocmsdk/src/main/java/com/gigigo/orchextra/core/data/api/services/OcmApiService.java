package com.gigigo.orchextra.core.data.api.services;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OcmApiService {

  String MENUS = "menus";
  String SECTION = "{section}";
  String SEARCH = "search";
  String ELEMENT = "element/{elementId}";

  @GET(MENUS) Observable<ApiMenuContentDataResponse> getMenuDataRx();

  @GET(MENUS) Call<ApiMenuContentDataResponse> getMenu();

  @GET(SECTION) Observable<ApiSectionContentDataResponse> getSectionDataRx(
      @Path(value = "section", encoded = true) String section,
      @Query("withThumbnails") Integer withThumbnails);

  @GET(SEARCH) Observable<ApiSectionContentDataResponse> searchRx(
      @Query("search") String textToSearch);

  @GET(ELEMENT) Observable<ApiElementDataResponse> getElementByIdRx(
      @Path(value = "elementId", encoded = true) String elementId,
      @Query("withThumbnails") Integer withThumbnails);
}
