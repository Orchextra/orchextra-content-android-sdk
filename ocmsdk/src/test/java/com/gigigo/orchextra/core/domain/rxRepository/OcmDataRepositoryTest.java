//package com.gigigo.orchextra.core.domain.rxRepository;
//
//@RunWith(MockitoJUnitRunner.class)
//public class OcmDataRepositoryTest {
//
//  private static final int FAKE_USER_ID = 123;
//
//  private OcmDataRepository ocmDataRepository;
//
//  @Mock private UserDataStoreFactory mockUserDataStoreFactory;
//  @Mock private UserEntityDataMapper mockUserEntityDataMapper;
//  @Mock private UserDataStore mockUserDataStore;
//  @Mock private UserEntity mockUserEntity;
//  @Mock private User mockUser;
//
//  @Before
//  public void setUp() {
//    ocmDataRepository = new OcmDataRepository(mockUserDataStoreFactory, mockUserEntityDataMapper);
//    given(mockUserDataStoreFactory.create(anyInt())).willReturn(mockUserDataStore);
//    given(mockUserDataStoreFactory.createCloudDataStore()).willReturn(mockUserDataStore);
//  }
//
//  @Test
//  public void testGetUsersHappyCase() {
//    List<UserEntity> usersList = new ArrayList<>();
//    usersList.add(new UserEntity());
//    given(mockUserDataStore.userEntityList()).willReturn(Observable.just(usersList));
//
//    ocmDataRepository.users();
//
//    verify(mockUserDataStoreFactory).createCloudDataStore();
//    verify(mockUserDataStore).userEntityList();
//  }
//
//  @Test
//  public void testGetUserHappyCase() {
//    UserEntity userEntity = new UserEntity();
//    given(mockUserDataStore.userEntityDetails(FAKE_USER_ID)).willReturn(Observable.just(userEntity));
//    ocmDataRepository.user(FAKE_USER_ID);
//
//    verify(mockUserDataStoreFactory).create(FAKE_USER_ID);
//    verify(mockUserDataStore).userEntityDetails(FAKE_USER_ID);
//  }
//}
