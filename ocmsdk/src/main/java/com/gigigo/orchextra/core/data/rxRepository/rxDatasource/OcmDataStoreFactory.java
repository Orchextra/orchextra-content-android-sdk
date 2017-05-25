package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Named;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmDataStoreFactory {

  private final OcmDataStore cloudDataStore;
  private final OcmDataStore diskDataStore;

  @Inject public OcmDataStoreFactory(OcmCloudDataStore cloudDataStore,
      OcmDiskDataStore diskDataStore) {
    this.cloudDataStore = cloudDataStore;
    this.diskDataStore = diskDataStore;
  }

  public OcmDataStore getCloudDataStore() {
    return cloudDataStore;
  }

  public OcmDataStore getDiskDataStore() {
    return diskDataStore;
  }
}
