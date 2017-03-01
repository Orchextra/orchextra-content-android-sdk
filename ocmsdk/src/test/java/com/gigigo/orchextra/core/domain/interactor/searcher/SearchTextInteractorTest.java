package com.gigigo.orchextra.core.domain.interactor.searcher;

import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.SearcherNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.interactor.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactor.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchTextInteractorTest {

  @Mock ConnectionUtils connectionUtils;

  @Mock SearcherNetworkDataSource networkDataSource;

  private SearchTextInteractor searchTextInteractor;

  @Before public void setUp() throws Exception {
    searchTextInteractor = new SearchTextInteractor(connectionUtils, networkDataSource);
  }

  @Test public void shouldRetrieveNoNetworkConnectionErrorWhenNoConnection() throws Exception {

    when(connectionUtils.hasConnection()).thenReturn(false);

    InteractorResponse<ContentData> response = searchTextInteractor.call();

    assertThat(response.hasError()).isTrue();
    assertThat(response.getError()).isExactlyInstanceOf(NoNetworkConnectionError.class);
  }

  @Test public void shouldRetrieveSomeItemsWhenSearchingWithARightText() throws Exception {
    BusinessObject<ContentData> fakeBoContentData = getFakeSuccessfulBoContentData();

    when(connectionUtils.hasConnection()).thenReturn(true);

    when(networkDataSource.doSearch(any(String.class))).thenReturn(fakeBoContentData);

    InteractorResponse<ContentData> response = searchTextInteractor.call();

    assertThat(response.hasError()).isFalse();
    assertThat(response.getResult()).isEqualTo(fakeBoContentData.getData());
  }

  @Test public void shouldRetrieveGenericErrorWhenRequestFailed() throws Exception {
    BusinessObject<ContentData> fakeBoContentData = getFakeFailedBoContentData();

    when(connectionUtils.hasConnection()).thenReturn(true);

    when(networkDataSource.doSearch(any(String.class))).thenReturn(fakeBoContentData);

    InteractorResponse<ContentData> response = searchTextInteractor.call();

    assertThat(response.hasError()).isTrue();
    assertThat(response.getError()).isExactlyInstanceOf(GenericResponseDataError.class);
  }

  private BusinessObject<ContentData> getFakeSuccessfulBoContentData() {
    return new BusinessObject<>(new ContentData(), BusinessError.createOKInstance());
  }

  private BusinessObject<ContentData> getFakeFailedBoContentData() {
    return new BusinessObject<>(null, BusinessError.createKOInstance(""));
  }
}