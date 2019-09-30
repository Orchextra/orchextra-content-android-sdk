//package com.gigigo.orchextra.core.domain.rxInteractor;
//
//import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
//import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoMoreInteractions;
//import static org.mockito.Mockito.verifyZeroInteractions;
//
//@RunWith(MockitoJUnitRunner.class) public class GetSectionTest {
//
//  private static final boolean FORCE_RELOAD = false;
//  private static final String FAKE_SECTION = "FAKE_SECTION";
//
//  private GetSection getSection;
//
//  @Mock private OcmRepository mockOcmRepository;
//  @Mock private PriorityScheduler mockThreadExecutor;
//  @Mock private PostExecutionThread mockPostExecutionThread;
//
//  @Rule public ExpectedException expectedException = ExpectedException.none();
//
//  @Before public void setUp() {
//    getSection = new GetSection(mockOcmRepository, mockThreadExecutor, mockPostExecutionThread);
//  }
//
//  @Test public void testGetSectionUseCaseObservableHappyCase() {
//    getSection.buildUseCaseObservable(GetSection.Params.forSection(FORCE_RELOAD, FAKE_SECTION, 12));
//
//    verify(mockOcmRepository).getSectionElements(FORCE_RELOAD, FAKE_SECTION, 12);
//    verifyNoMoreInteractions(mockOcmRepository);
//    verifyZeroInteractions(mockPostExecutionThread);
//    verifyZeroInteractions(mockThreadExecutor);
//  }
//
//  @Test public void testShouldFailWhenNoOrEmptyParameters() {
//    expectedException.expect(NullPointerException.class);
//    getSection.buildUseCaseObservable(null);
//  }
//}
