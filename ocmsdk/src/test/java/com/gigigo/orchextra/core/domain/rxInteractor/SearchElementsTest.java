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

@RunWith(MockitoJUnitRunner.class) public class SearchElementsTest {

  private static final String FAKE_TEXT = "FAKE:TEST";


  private SearchElements searchElements;

  @Mock private OcmRepository mockOcmRepository;
  @Mock private PriorityScheduler mockThreadExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    searchElements = new SearchElements(mockOcmRepository, mockThreadExecutor, mockPostExecutionThread);
  }

  @Test public void testSearchElementsUseCaseObservableHappyCase() {
    searchElements.buildUseCaseObservable(SearchElements.Params.forTextToSearch(FAKE_TEXT));

    verify(mockOcmRepository).doSearch(FAKE_TEXT);
    verifyNoMoreInteractions(mockOcmRepository);
    verifyZeroInteractions(mockPostExecutionThread);
    verifyZeroInteractions(mockThreadExecutor);
  }

  @Test public void testShouldFailWhenNoOrEmptyParameters() {
    expectedException.expect(NullPointerException.class);
    searchElements.buildUseCaseObservable(null);
  }
}
