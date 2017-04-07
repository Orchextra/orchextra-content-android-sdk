package com.gigigo.orchextra.core.data.api.services;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OcmApiService {

  String MENUS = "menus";
  String SECTION = "{section}";
  String SEARCH = "search";
  String ELEMENT = "element/{elementId}";

  @GET(MENUS) Call<ApiMenuContentDataResponse> getMenuData();

  @GET(SECTION) Call<ApiSectionContentDataResponse> getSectionData(
      @Path(value = "section", encoded = true) String section);

  @GET(SEARCH) Call<ApiSectionContentDataResponse> search(@Query("search") String textToSearch);

  @GET(ELEMENT) Call<ApiElementDataResponse> getElementById(@Path(value = "elementId", encoded = true) String elementId);
}
