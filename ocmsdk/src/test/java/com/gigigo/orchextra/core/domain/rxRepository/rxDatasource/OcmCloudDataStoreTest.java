package com.gigigo.orchextra.core.domain.rxRepository.rxDatasource;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData;
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementData;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmCloudDataStore;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class OcmCloudDataStoreTest {

  private static final String FAKE_SECTION = "FAKE_SECTION";
  private static final String FAKE_SEACH_TEXT = "FAKE_TEXT";
  private static final String FAKE_ID = "F4k3_ID";

  private OcmCloudDataStore ocmCloudDataStore;

  @Mock private OcmApiService mockRestApi;
  @Mock private OcmCache mockOcmCache;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    ocmCloudDataStore = new OcmCloudDataStore(mockRestApi, mockOcmCache);
  }

  @Test public void testGetMenuEntityFromApi() {
    ocmCloudDataStore.getMenuEntity();
    verify(mockRestApi).getMenuDataRx();
  }

  @Test public void testGetSectionEntityFromApi() {
    ApiSectionContentDataResponse fakeApiSection = new ApiSectionContentDataResponse();
    Observable<ApiSectionContentDataResponse> fakeObservable = Observable.just(fakeApiSection);
    given(mockRestApi.getSectionDataRx(FAKE_SECTION)).willReturn(fakeObservable);

    ocmCloudDataStore.getSectionEntity(FAKE_SECTION);

    verify(mockRestApi).getSectionDataRx(FAKE_SECTION);
  }

  @Test public void testSearchByTextEntityFromApi() {
    ApiSectionContentDataResponse fakeApiSection = new ApiSectionContentDataResponse();
    Observable<ApiSectionContentDataResponse> fakeObservable = Observable.just(fakeApiSection);
    given(mockRestApi.searchRx(FAKE_SEACH_TEXT)).willReturn(fakeObservable);

    ocmCloudDataStore.searchByText(FAKE_SEACH_TEXT);

    verify(mockRestApi).searchRx(FAKE_SEACH_TEXT);
  }

  @Test public void testGetElementByIdEntityFromApi() {
    ApiElementDataResponse fakeApiElement = new ApiElementDataResponse();
    Observable<ApiElementDataResponse> fakeObservable = Observable.just(fakeApiElement);
    given(mockRestApi.getElementByIdRx(FAKE_ID)).willReturn(fakeObservable);

    ocmCloudDataStore.getElementById(FAKE_ID);

    verify(mockRestApi).getElementByIdRx(FAKE_ID);
  }
}
