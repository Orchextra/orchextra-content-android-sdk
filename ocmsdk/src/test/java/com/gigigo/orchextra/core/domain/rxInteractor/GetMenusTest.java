package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class GetMenusTest {

  private static final boolean FORCE_RELOAD = false;

  private GetMenus getMenus;

  @Mock private OcmRepository mockOcmRepository;
  @Mock private ThreadExecutor mockThreadExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    getMenus = new GetMenus(mockOcmRepository, mockThreadExecutor,
        mockPostExecutionThread);
  }

  @Test
  public void testGetUserDetailsUseCaseObservableHappyCase() {
    getMenus.buildUseCaseObservable(GetMenus.Params.forForceReload(FORCE_RELOAD));

    verify(mockOcmRepository).getMenu(FORCE_RELOAD);
    verifyNoMoreInteractions(mockOcmRepository);
    verifyZeroInteractions(mockPostExecutionThread);
    verifyZeroInteractions(mockThreadExecutor);
  }

  @Test
  public void testShouldFailWhenNoOrEmptyParameters() {
    expectedException.expect(NullPointerException.class);
    getMenus.buildUseCaseObservable(null);
  }
}
