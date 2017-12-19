package com.gigigo.orchextra.core.domain.rxRepository.rxDatasource;

import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementDataResponse;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentDataResponse;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionResponse;
import com.gigigo.orchextra.core.data.api.services.OcmApiService;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.data.rxCache.imageCache.OcmImageCache;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmCloudDataStore;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class OcmCloudDataStoreTest {

  private static final String FAKE_SECTION = "FAKE_SECTION";
  private static final int FAKE_THUMBNAILS = 32;
  private static final String FAKE_SEACH_TEXT = "FAKE_TEXT";
  private static final String FAKE_ID = "F4k3_ID";

  private OcmCloudDataStore ocmCloudDataStore;

  @Mock private OcmApiService mockRestApi;
  @Mock private OcmCache mockOcmCache;
  @Mock private OcmImageCache mockImageCache;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    ocmCloudDataStore = new OcmCloudDataStore(mockRestApi, mockOcmCache, mockImageCache);
  }

  @Test public void testGetMenuEntityFromApi() {
    ApiMenuContentDataResponse fakeApiMenuContentDataResponse = new ApiMenuContentDataResponse();
    Observable<ApiMenuContentDataResponse> fakeObservable =
        Observable.just(fakeApiMenuContentDataResponse);
    given(mockRestApi.getMenuDataRx()).willReturn(fakeObservable);

    ocmCloudDataStore.getMenuEntity();

    verify(mockRestApi).getMenuDataRx();
  }

  @Test public void testGetSectionEntityFromApi() {
    ApiSectionContentDataResponse fakeApiSection = new ApiSectionContentDataResponse();
    Observable<ApiSectionContentDataResponse> fakeObservable = Observable.just(fakeApiSection);
    given(mockRestApi.getSectionDataRx(FAKE_SECTION, FAKE_THUMBNAILS)).willReturn(fakeObservable);

    ocmCloudDataStore.getSectionEntity(FAKE_SECTION, anyInt());

    verify(mockRestApi).getSectionDataRx(FAKE_SECTION, FAKE_THUMBNAILS);
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
    given(mockRestApi.getElementByIdRx(FAKE_ID, FAKE_THUMBNAILS)).willReturn(fakeObservable);

    ocmCloudDataStore.getElementById(FAKE_ID);

    verify(mockRestApi).getElementByIdRx(FAKE_ID, FAKE_THUMBNAILS);
  }

  @Test public void testGetVersion(){
    ApiVersionResponse fakeVersion = new ApiVersionResponse();

    Observable<ApiVersionResponse> fakeObservable =  Observable.just(fakeVersion);
    given(mockRestApi.getVersionDataRx()).willReturn(fakeObservable);

    ocmCloudDataStore.getVersion();

    verify(mockRestApi).getVersionDataRx();

  }
}
