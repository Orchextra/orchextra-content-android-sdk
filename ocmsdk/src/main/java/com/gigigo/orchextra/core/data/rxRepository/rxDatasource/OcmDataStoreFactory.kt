package com.gigigo.orchextra.core.data.rxRepository.rxDatasource

import android.util.Log
import com.gigigo.orchextra.core.domain.entities.ocm.OxSession
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton
import kotlin.coroutines.experimental.CoroutineContext

private val uiContext: CoroutineContext = UI
private val bgContext: CoroutineContext = CommonPool

@Singleton
class OcmDataStoreFactory
@Inject constructor(
    val cloudDataStore: OcmCloudDataStore,
    val diskDataStore: OcmDiskDataStore,
    private val connectionUtils: ConnectionUtils,
    private val session: OxSession) {

  private val TAG = OcmDataStoreFactory::class.java.simpleName

  fun getDataStoreForVersion(): OcmDataStore {
    if (session.token == null) {
      return diskDataStore
    }

    lateinit var dataStore: OcmDataStore

    runBlocking {
      dataStore = dataStoreForVersion().await()
    }

    return dataStore
  }

  private fun dataStoreForVersion() =
      async(bgContext) {
        var dataStore: OcmDataStore

        if (diskDataStore.ocmCache.isVersionCached) {
          Log.i(TAG, "DISK - Version")
          dataStore = diskDataStore
        } else {
          Log.i(TAG, "CLOUD - Version")
          dataStore = cloudDataStore
        }

        dataStore
      }

  fun getDataStoreForMenus(force: Boolean): OcmDataStore {
    val ocmDataStore: OcmDataStore

    if (!connectionUtils.hasConnection()) return diskDataStore

    ocmDataStore = if (force) {
      Log.i(TAG, "CLOUD - Menus")
      cloudDataStore
    } else {
      val cache = diskDataStore.ocmCache
      if (cache.isMenuCached && !cache.isMenuExpired) {
        Log.i(TAG, "DISK  - Menus")
        diskDataStore
      } else {
        Log.i(TAG, "CLOUD - Menus")
        cloudDataStore
      }
    }

    return ocmDataStore
  }

  fun getDataStoreForSections(force: Boolean, section: String): OcmDataStore {
    val ocmDataStore: OcmDataStore

    if (!connectionUtils.hasConnection()) return diskDataStore

    ocmDataStore = if (force) {
      Log.i(TAG, "CLOUD - Sections")
      cloudDataStore
    } else {
      val cache = diskDataStore.ocmCache
      if (cache.isSectionCached(section) && !cache.isSectionExpired(section)) {
        Log.i(TAG, "DISK  - Sections")
        diskDataStore
      } else {
        Log.i(TAG, "CLOUD - Sections")
        cloudDataStore
      }
    }

    return ocmDataStore
  }

  fun getDataStoreForDetail(force: Boolean, slug: String): OcmDataStore {
    val ocmDataStore: OcmDataStore

    if (!connectionUtils.hasConnection()) return diskDataStore

    ocmDataStore = if (force) {
      Log.i(TAG, "CLOUD - Detail")
      cloudDataStore
    } else {
      val cache = diskDataStore.ocmCache
      if (cache.isDetailCached(slug) && !cache.isDetailExpired(slug)) {
        Log.i(TAG, "DISK  - Detail")
        diskDataStore
      } else {
        Log.i(TAG, "CLOUD - Detail")
        cloudDataStore
      }
    }

    return ocmDataStore
  }

  fun getDataStoreForVideo(force: Boolean, videoId: String): OcmDataStore {

    val ocmDataStore: OcmDataStore

    if (!connectionUtils.hasConnection()) return diskDataStore

    ocmDataStore = if (force) {
      Log.i(TAG, "CLOUD - Video")
      cloudDataStore
    } else {
      val cache = diskDataStore.ocmCache
      if (cache.isVideoCached(videoId) && !cache.isVideoExpired(videoId)) {
        Log.i(TAG, "DISK  - Video")
        diskDataStore
      } else {
        Log.i(TAG, "CLOUD - Video")
        cloudDataStore
      }
    }

    return ocmDataStore
  }
}