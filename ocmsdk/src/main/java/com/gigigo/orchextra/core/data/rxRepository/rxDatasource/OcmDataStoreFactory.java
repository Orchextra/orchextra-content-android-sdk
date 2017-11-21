package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.util.Log;
import com.gigigo.orchextra.core.data.rxCache.OcmCache;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

/**
 * Created by francisco.hernandez on 23/5/17.
 */

@Singleton public class OcmDataStoreFactory {

  private static final String TAG = OcmDataStoreFactory.class.getSimpleName();

  private final OcmCloudDataStore cloudDataStore;
  private final OcmDiskDataStore diskDataStore;
  private final ConnectionUtils connectionUtils;

  @Inject
  public OcmDataStoreFactory(OcmCloudDataStore cloudDataStore, OcmDiskDataStore diskDataStore,
      ConnectionUtils connectionUtils) {
    this.cloudDataStore = cloudDataStore;
    this.diskDataStore = diskDataStore;
    this.connectionUtils = connectionUtils;
  }

  public OcmDataStore getDataStoreForMenus(boolean force) {
    OcmDataStore ocmDataStore;

    if (!connectionUtils.hasConnection())
      return getDiskDataStore();

    if (force) {
      Log.i(TAG, "CLOUD - Menus");
      ocmDataStore = getCloudDataStore();
    } else {
      OcmCache cache = diskDataStore.getOcmCache();
      if (cache.isMenuCached() && !cache.isMenuExpired()) {
        Log.i(TAG, "DISK  - Menus");
        ocmDataStore = getDiskDataStore();
      } else {
        Log.i(TAG, "CLOUD - Menus");
        ocmDataStore = getCloudDataStore();
      }
    }

    return ocmDataStore;
  }

  public OcmDataStore getDataStoreForSections(boolean force, String section) {
    OcmDataStore ocmDataStore;

    if (!connectionUtils.hasConnection())
      return getDiskDataStore();

    if (force) {
      Log.i(TAG, "CLOUD - Sections");
      ocmDataStore = getCloudDataStore();
    } else {
      OcmCache cache = diskDataStore.getOcmCache();
      if (cache.isSectionCached(section) && !cache.isSectionExpired(section)) {
        Log.i(TAG, "DISK  - Sections");
        ocmDataStore = getDiskDataStore();
      } else {
        Log.i(TAG, "CLOUD - Sections");
        ocmDataStore = getCloudDataStore();
      }
    }

    return ocmDataStore;
  }

  public OcmDataStore getDataStoreForDetail(boolean force, String elementUrl) {
    OcmDataStore ocmDataStore;

    if (!connectionUtils.hasConnection())
      return getDiskDataStore();

    if (force) {
      Log.i(TAG, "CLOUD - Detail");
      ocmDataStore = getCloudDataStore();
    } else {
      OcmCache cache = diskDataStore.getOcmCache();
      if (cache.isDetailCached(elementUrl) && !cache.isDetailExpired(elementUrl)) {
        Log.i(TAG, "DISK  - Detail");
        ocmDataStore = getDiskDataStore();
      } else {
        Log.i(TAG, "CLOUD - Detail");
        ocmDataStore = getCloudDataStore();
      }
    }

    return ocmDataStore;
  }

  public OcmDataStore getCloudDataStore() {
    return cloudDataStore;
  }

  public OcmDataStore getDiskDataStore() {
    return diskDataStore;
  }
}
