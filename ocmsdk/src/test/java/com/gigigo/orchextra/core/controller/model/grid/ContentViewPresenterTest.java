package com.gigigo.orchextra.core.controller.model.grid;

import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
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

  private final String FAKE_ID = "FAKE_ID";
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
    presenter.loadSection(FAKE_ID, FAKE_FILTER);

    verify(mockOcmController).getSection(forceReload, anyString(), anyInt(),
        any(OcmController.GetSectionControllerCallback.class));
  }
}
