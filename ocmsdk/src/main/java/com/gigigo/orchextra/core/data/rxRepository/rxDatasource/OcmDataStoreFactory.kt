package com.gigigo.orchextra.core.data.rxRepository.rxDatasource

import android.util.Log
import com.gigigo.orchextra.core.domain.entities.ocm.OxSession
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils
import com.gigigo.orchextra.core.utils.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


private val uiContext: CoroutineContext = Dispatchers.Main
private val bgContext: CoroutineContext = CoroutineExecutor.BACKGROUND

@Singleton
class OcmDataStoreFactory
@Inject constructor(
    val cloudDataStore: OcmCloudDataStore,
    val diskDataStore: OcmDiskDataStore,
    private val connectionUtils: ConnectionUtils,
    private val session: OxSession
) {

    private val TAG = OcmDataStoreFactory::class.java.simpleName

    fun getDataStoreForVersion(): OcmDataStore {
        if (session.token == null) {
            return diskDataStore
        }

        lateinit var dataStore: OcmDataStore

        runBlocking {
            println("*****GETVERSION RUNBLOCKING THREAD " + Thread.currentThread().name)
            dataStore = dataStoreForVersion().await()
        }

        return dataStore
    }

    private fun dataStoreForVersion() =
        GlobalScope.async(bgContext) {

            println("*****GETVERSION ASYNC THREAD " + Thread.currentThread().name)
            var dataStore: OcmDataStore

            if (false) {
                Log.i(TAG, "DISK - Version")
                dataStore = diskDataStore
            } else {
                Log.i(TAG, "CLOUD - Version")
                dataStore = cloudDataStore
            }

            dataStore
        }

    fun getDataStoreForMenus(force: Boolean): OcmDataStore {
        //if (!connectionUtils.hasConnection()) return diskDataStore

        lateinit var dataStore: OcmDataStore

        runBlocking {
            dataStore = dataStoreForMenus(force).await()
        }

        return dataStore
    }

    private fun dataStoreForMenus(force: Boolean) =
        GlobalScope.async(bgContext) {
            var dataStore: OcmDataStore

            dataStore = if (force) {
                Log.i(TAG, "CLOUD - Menus")
                cloudDataStore
            } else {
                val cache = diskDataStore.ocmCache
                if (cache.hasMenusCached()) {
                    Log.i(TAG, "DISK  - Menus")
                    diskDataStore
                } else {
                    Log.i(TAG, "CLOUD - Menus")
                    cloudDataStore
                }
            }

            dataStore
        }

    fun getDataStoreForSection(force: Boolean, section: String): OcmDataStore {
        if (!connectionUtils.hasConnection()) return diskDataStore

        lateinit var dataStore: OcmDataStore

        runBlocking {
            dataStore = dataStoreForSection(force, section).await()
        }

        return dataStore
    }

    private fun dataStoreForSection(force: Boolean, section: String) =
        GlobalScope.async(bgContext) {
            var dataStore: OcmDataStore

            dataStore = if (force) {
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

            dataStore
        }

    fun getDataStoreForDetail(force: Boolean, slug: String): OcmDataStore {
        if (!connectionUtils.hasConnection()) return diskDataStore

        lateinit var dataStore: OcmDataStore

        runBlocking {
            dataStore = dataStoreForDetail(force, slug).await()
        }

        return dataStore
    }

    private fun dataStoreForDetail(force: Boolean, slug: String) =
        GlobalScope.async(bgContext) {
            var dataStore: OcmDataStore

            dataStore = if (force) {
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

            dataStore
        }

    fun getDataStoreForVideo(force: Boolean, videoId: String): OcmDataStore {
        if (!connectionUtils.hasConnection()) return diskDataStore

        lateinit var dataStore: OcmDataStore

        runBlocking {
            dataStore = dataStoreForVideo(force, videoId).await()
        }

        return dataStore
    }

    private fun dataStoreForVideo(force: Boolean, videoId: String) =
        GlobalScope.async(bgContext) {
            var dataStore: OcmDataStore

            dataStore = if (force) {
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

            dataStore
        }
}