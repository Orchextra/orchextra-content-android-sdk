package com.gigigo.orchextra.core.controller.model.detail;

import com.gigigo.orchextra.core.controller.OcmControllerImp;
import com.gigigo.orchextra.core.controller.model.grid.ContentView;
import com.gigigo.orchextra.core.controller.model.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.rxInteractor.ClearCache;
import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
import io.reactivex.observers.DisposableObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class) public class ContentViewPresenterTest {

  private ContentViewPresenter presenter;

  private final String FAKE_ID = "FAKE_ID";
  private final String FAKE_FILTER = "FAKE_FILTER";

  @Mock private OcmController mockOcmController;
  @Mock private Authoritation mockAuthoritation;
  @Mock private ContentView mockContentView;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    presenter =
        new ContentViewPresenter(mockOcmController, mockAuthoritation);
    presenter.attachView(mockContentView);
  }

  @Test public void testLoadSectionWithCacheAndAfterNetwork() {
    presenter.loadSectionWithCacheAndAfterNetwork(FAKE_ID, FAKE_FILTER);

    verify(mockContentView).showProgressView(true);
    verify(mockOcmController).getSection(anyBoolean(), anyString(), any(
        OcmController.GetSectionControllerCallback.class));
  }

}
