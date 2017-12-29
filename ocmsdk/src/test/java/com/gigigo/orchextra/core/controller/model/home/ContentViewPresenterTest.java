package com.gigigo.orchextra.core.controller.model.home;

import com.gigigo.orchextra.core.controller.model.home.grid.ContentView;
import com.gigigo.orchextra.core.controller.model.home.grid.ContentViewPresenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class) public class ContentViewPresenterTest {

  private ContentViewPresenter presenter;

  private final String FAKE_FILTER = "FAKE_FILTER";

  @Mock private OcmController mockOcmController;
  @Mock private Authoritation mockAuthoritation;
  @Mock private ContentView mockContentView;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    presenter = new ContentViewPresenter(mockOcmController, mockAuthoritation);
    presenter.attachView(mockContentView);
  }

  @Test public void testLoadSectionWithCacheAndAfterNetwork() {
    presenter.loadSection(new UiMenu(), FAKE_FILTER);

    verify(mockOcmController).getSection(any(DataRequest.class), anyString(), anyInt(),
        any(OcmController.GetSectionControllerCallback.class));
  }
}
