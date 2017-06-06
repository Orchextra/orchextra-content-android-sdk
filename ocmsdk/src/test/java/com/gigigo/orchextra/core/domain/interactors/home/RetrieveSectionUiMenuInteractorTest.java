package com.gigigo.orchextra.core.domain.interactors.home;

import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.data.MenuNetworkDataSource;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.services.MenuNetworkDomainService;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class RetrieveSectionUiMenuInteractorTest {

  @Mock ConnectionUtils connectionUtils;

  @Mock MenuNetworkDataSource menuNetworkDataSource;

  @Mock DataBaseDataSource dataBaseDataSource;

  private GetMenuDataInteractor interactor;

  @Before public void setUp() throws Exception {
    MenuNetworkDomainService menuNetworkDomainService =
        new MenuNetworkDomainService(connectionUtils, menuNetworkDataSource);
    interactor = new GetMenuDataInteractor(dataBaseDataSource, menuNetworkDomainService);
  }

  @Ignore @Test public void shouldReturnNoNetworkConnectionErrorWhenDeviceNoHasInternetConnection()
      throws Exception {

    when(connectionUtils.hasConnection()).thenReturn(false);

    InteractorResponse<MenuContentData> response = interactor.call();

    assertTrue(response.hasError());
    assertThat(response.getError(), is(instanceOf(NoNetworkConnectionError.class)));
  }

  @Ignore @Test public void shouldReturnValueListWhenHasConnectionAndRequestWasSuccessful()
      throws Exception {
    BusinessObject<MenuContentData> fakeSuccessfulBoMenuContentData =
        getFakeSuccessfulMenuContentData();

    when(connectionUtils.hasConnection()).thenReturn(true);
    when(menuNetworkDataSource.getMenuContentData()).thenReturn(fakeSuccessfulBoMenuContentData);

    InteractorResponse<MenuContentData> response = interactor.call();

    assertFalse(response.hasError());
    assertThat(response.getResult(), is(fakeSuccessfulBoMenuContentData.getData()));
  }

  @Ignore @Test public void shouldReturnGenericInteractorErrorWhenRequestFailed() throws Exception {
    BusinessObject<MenuContentData> fakeFailedBoMenuContentData = getFakeFailedMenuContentData();

    when(connectionUtils.hasConnection()).thenReturn(true);
    when(menuNetworkDataSource.getMenuContentData()).thenReturn(fakeFailedBoMenuContentData);

    InteractorResponse<MenuContentData> response = interactor.call();

    assertTrue(response.hasError());
    assertThat(response.getError(), is(instanceOf(GenericResponseDataError.class)));
  }

  private BusinessObject<MenuContentData> getFakeSuccessfulMenuContentData() {
    MenuContentData menuContentData = new MenuContentData();

    return new BusinessObject<>(menuContentData, BusinessError.createOKInstance());
  }

  private BusinessObject<MenuContentData> getFakeFailedMenuContentData() {
    return new BusinessObject<>(null, BusinessError.createKOInstance(""));
  }
}