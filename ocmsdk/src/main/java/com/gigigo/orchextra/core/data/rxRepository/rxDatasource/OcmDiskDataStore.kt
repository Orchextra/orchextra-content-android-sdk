package com.gigigo.orchextra.core.data.rxRepository.rxDatasource

import android.content.Context
import com.gigigo.orchextra.core.data.database.entities.DbElementData
import com.gigigo.orchextra.core.data.database.entities.DbMenuContentData
import com.gigigo.orchextra.core.data.database.entities.DbSectionContentData
import com.gigigo.orchextra.core.data.database.entities.DbVideoData
import com.gigigo.orchextra.core.data.mappers.toContentData
import com.gigigo.orchextra.core.data.mappers.toElementData
import com.gigigo.orchextra.core.data.mappers.toMenuContentData
import com.gigigo.orchextra.core.data.mappers.toVimeoInfo
import com.gigigo.orchextra.core.data.rxCache.OcmCache
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData
import com.gigigo.orchextra.core.domain.entities.elements.ElementData
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData
import gigigo.com.vimeolibs.VimeoInfo
import io.reactivex.Observable
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton

@Singleton
class OcmDiskDataStore @Inject constructor(val ocmCache: OcmCache) : OcmDataStore {

  override fun getMenus(): Observable<MenuContentData> {
    return ocmCache.getMenus().map(DbMenuContentData::toMenuContentData)
  }

  override fun getSection(elementUrl: String,
      numberOfElementsToDownload: Int): Observable<ContentData> {
    return ocmCache.getSection(elementUrl).map(DbSectionContentData::toContentData)
  }

  override fun searchByText(section: String): Observable<ContentData>? {
    return null
  }

  override fun getElementById(slug: String): Observable<ElementData> {
    return ocmCache.getDetail(slug).map(DbElementData::toElementData)
  }

  override fun getVideoById(context: Context, videoId: String, isWifiConnection: Boolean,
      isFastConnection: Boolean): Observable<VimeoInfo> {
    return ocmCache.getVideo(videoId).map(DbVideoData::toVimeoInfo)
  }

  override fun isFromCloud(): Boolean {
    return false
  }
}