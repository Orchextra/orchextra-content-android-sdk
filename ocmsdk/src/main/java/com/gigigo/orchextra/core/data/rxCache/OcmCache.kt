package com.gigigo.orchextra.core.data.rxCache

import android.content.Context
import com.gigigo.orchextra.core.data.api.dto.content.ApiSectionContentData
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContentData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.data.database.entities.DbVideoData
import com.gigigo.orchextra.core.domain.entities.elements.ElementData
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import gigigo.com.vimeolibs.VimeoInfo
import io.reactivex.Observable

interface OcmCache {

  val context: Context

  fun getVersion(): Observable<DbVersionData>
  fun isVersionCached(): Boolean
  fun isVersionExpired(): Boolean
  fun putVersion(versionData: VersionData)

  fun getMenus(): Observable<ApiMenuContentData>
  fun isMenuCached(): Boolean
  fun isMenuExpired(): Boolean
  fun putMenus(apiMenuContentData: ApiMenuContentData)

  fun getSection(elementUrl: String): Observable<ApiSectionContentData>
  fun putSection(apiSectionContentData: ApiSectionContentData)
  fun isSectionCached(elementUrl: String): Boolean
  fun isSectionExpired(elementUrl: String): Boolean

  fun getDetail(slug: String): Observable<ElementData>
  fun isDetailCached(slug: String): Boolean
  fun isDetailExpired(slug: String): Boolean
  fun putDetail(elementData: ElementData)

  fun getVideo(videoId: String): Observable<DbVideoData>
  fun isVideoCached(videoId: String): Boolean
  fun isVideoExpired(videoId: String): Boolean
  fun putVideo(videoData: VimeoInfo)

  fun evictAll(images: Boolean, data: Boolean)
}