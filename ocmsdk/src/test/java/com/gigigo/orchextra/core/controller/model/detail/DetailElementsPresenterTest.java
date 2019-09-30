//package com.gigigo.orchextra.core.controller.model.detail;
//
//import com.gigigo.orchextra.core.controller.OcmViewGenerator;
//import com.gigigo.orchextra.core.domain.OcmController;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.verify;
//
//@RunWith(MockitoJUnitRunner.class) public class DetailElementsPresenterTest {
//
//  private DetailElementsViewPresenter presenter;
//
//  private final String FAKE_ELEMENT_URL = "FAKE_ELEMENT_URL";
//
//  @Mock private DetailElementsView mockDetailView;
//  @Mock private OcmController mockOcmController;
//  @Mock private OcmViewGenerator mockOcmViewGenerator;
//
//  @Rule public ExpectedException expectedException = ExpectedException.none();
//
//  @Before public void setUp() {
//    presenter = new DetailElementsViewPresenter(mockOcmController, mockOcmViewGenerator);
//    presenter.attachView(mockDetailView);
//  }
//
//  @Test public void testLoadSection() {
//    presenter.loadSection(FAKE_ELEMENT_URL);
//
//    verify(mockDetailView).showProgressView(true);
//    verify(mockOcmController).getDetails(anyString(),
//        any(OcmController.GetDetailControllerCallback.class));
//  }
//}
