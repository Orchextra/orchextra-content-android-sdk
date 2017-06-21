package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.core.controller.OcmControllerImp;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.controller.model.grid.ContentView;
import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.rxInteractor.ClearCache;
import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
import com.gigigo.orchextra.ocm.Ocm;
import io.reactivex.observers.DisposableObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class) public class DetailElementsPresenterTest {

  private DetailElementsViewPresenter presenter;

  private final String FAKE_ELEMENT_URL = "FAKE_ELEMENT_URL";

  @Mock private DetailElementsView mockDetailView;
  @Mock private OcmController mockOcmController;
  @Mock private OcmViewGenerator mockOcmViewGenerator;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    presenter = new DetailElementsViewPresenter(mockOcmController, mockOcmViewGenerator);
    presenter.attachView(mockDetailView);
  }

  @Test public void testLoadSection() {
    presenter.loadSection(FAKE_ELEMENT_URL);

    verify(mockDetailView).showProgressView(true);
    verify(mockOcmController).getDetails(anyBoolean(), anyString(),
        any(OcmController.GetDetailControllerCallback.class));
  }
}
