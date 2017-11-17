package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
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

@RunWith(MockitoJUnitRunner.class) public class GetVersionTest {

  private static final boolean FORCE_RELOAD = false;

  private GetVersion getVersion;

  @Mock private OcmRepository mockOcmRepository;
  @Mock private PriorityScheduler mockThreadExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    getVersion = new GetVersion(mockOcmRepository, mockThreadExecutor, mockPostExecutionThread);
  }

  @Test public void testGetSectionUseCaseObservableHappyCase() {
    getVersion.buildUseCaseObservable(GetVersion.Params.forVersion());

    verify(mockOcmRepository).getVersion();
    verifyNoMoreInteractions(mockOcmRepository);
    verifyZeroInteractions(mockPostExecutionThread);
    verifyZeroInteractions(mockThreadExecutor);
  }

}
