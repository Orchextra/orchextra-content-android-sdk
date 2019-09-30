//package com.gigigo.orchextra.core.controller;
//
//import com.gigigo.orchextra.core.domain.OcmController;
//import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
//import com.gigigo.orchextra.core.domain.rxInteractor.ClearCache;
//import com.gigigo.orchextra.core.domain.rxInteractor.GetDetail;
//import com.gigigo.orchextra.core.domain.rxInteractor.GetMenus;
//import com.gigigo.orchextra.core.domain.rxInteractor.GetSection;
//import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
//import com.gigigo.orchextra.core.domain.rxInteractor.SearchElements;
//import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import io.reactivex.observers.DisposableObserver;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoMoreInteractions;
//
//@RunWith(MockitoJUnitRunner.class)
//public class OcmControllerTest {
//
//    private OcmController ocmController;
//
//    private final String FAKE_SECTION = "FAKE_SECTION";
//    private final String FAKE_DETAIL = "FAKE_DETAIL";
//    private final String FAKE_TEXT = "FAKE_TEXT";
//    private final int FAKE_IMAGES = 5;
//
//    @Mock
//    private ClearCache mockClearCache;
//    @Mock
//    private OcmController.GetMenusControllerCallback mockMenusControllerCallback;
//    @Mock
//    private GetDetail mockGetDetail;
//    @Mock
//    private GetMenus mockGetMenus;
//    @Mock
//    private GetSection mockGetSection;
//    @Mock
//    private SearchElements mockSearchElements;
//    @Mock
//    private ConnectionUtils mockConnectionUtils;
//
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//
//    @Before
//    public void setUp() {
//        ocmController =
//                new OcmControllerImp(mockGetMenus, mockGetSection, mockGetDetail, mockSearchElements,
//                        mockClearCache, mockConnectionUtils);
//    }
//
//    @Test
//    public void testClearCache() {
//        ocmController.clearCache(true, true, null);
//
//        verify(mockClearCache).execute(any(DisposableObserver.class), any(ClearCache.Params.class),
//                any(PriorityScheduler.Priority.class));
//        verifyNoMoreInteractions(mockClearCache);
//    }
//
//    @Test
//    public void testGetDetail() {
//        ocmController.getDetails(FAKE_DETAIL, null);
//
//        verify(mockGetDetail).execute(any(DisposableObserver.class), any(GetDetail.Params.class),
//                any(PriorityScheduler.Priority.class));
//        verifyNoMoreInteractions(mockGetDetail);
//    }
//
//    @Test
//    public void testGetSection() {
////        ocmController.getSection(DataRequest.FORCE_CLOUD, FAKE_SECTION, FAKE_IMAGES, null);
////
////        verify(mockGetSection).execute(any(DisposableObserver.class), any(GetSection.Params.class),
////                any(PriorityScheduler.Priority.class));
////        verifyNoMoreInteractions(mockGetSection);
//    }
//
//    @Test
//    public void testGetMenus() {
//        ocmController.getMenu(mockMenusControllerCallback, null);
//
//        verify(mockGetMenus).execute(any(DisposableObserver.class), any(GetMenus.Params.class),
//                any(PriorityScheduler.Priority.class));
//        verifyNoMoreInteractions(mockGetMenus);
//    }
//
//    @Test
//    public void testSearchElement() {
//        ocmController.search(FAKE_TEXT, null);
//
//        verify(mockSearchElements).execute(any(DisposableObserver.class),
//                any(SearchElements.Params.class), any(PriorityScheduler.Priority.class));
//
//        verifyNoMoreInteractions(mockSearchElements);
//    }
//}
