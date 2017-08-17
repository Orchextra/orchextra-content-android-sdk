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

@RunWith(MockitoJUnitRunner.class) public class GetDetailTest {

  private static final boolean FORCE_RELOAD = false;
  private static final String FAKE_DETAIL = "FAKE_DETAIL";

  private GetDetail getDetail;

  @Mock private OcmRepository mockOcmRepository;
  @Mock private PriorityScheduler mockThreadExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    getDetail = new GetDetail(mockOcmRepository, mockThreadExecutor, mockPostExecutionThread);
  }

  @Test public void testGetDetailUseCaseObservableHappyCase() {
    getDetail.buildUseCaseObservable(GetDetail.Params.forDetail(FORCE_RELOAD, FAKE_DETAIL));

    verify(mockOcmRepository).getDetail(FORCE_RELOAD, FAKE_DETAIL);
    verifyNoMoreInteractions(mockOcmRepository);
    verifyZeroInteractions(mockPostExecutionThread);
    verifyZeroInteractions(mockThreadExecutor);
  }

  @Test public void testShouldFailWhenNoOrEmptyParameters() {
    expectedException.expect(NullPointerException.class);
    getDetail.buildUseCaseObservable(null);
  }
}
