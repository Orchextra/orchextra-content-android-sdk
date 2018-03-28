package com.gigigo.orchextra.core.data.rxRepository

import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionData
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.data.database.entities.DbVersionData.Companion.VERSION_KEY
import com.gigigo.orchextra.core.data.database.entities.DbVideoData
import com.gigigo.orchextra.core.data.database.entities.DbVimeoInfo
import com.gigigo.orchextra.core.domain.entities.version.VersionData
import gigigo.com.vimeolibs.VimeoInfo

//region VERSION
fun DbVersionData.toVersionData(): VersionData = with(this) {
  val versionData = VersionData()
  versionData.version = version
  return versionData
}

fun ApiVersionData.toDbVersionData(): DbVersionData = with(this) {
  val versionData = DbVersionData()
  versionData.id = VERSION_KEY
  versionData.version = version
  return versionData
}
//endregion

fun ApiVideoData.toVimeoInfo(): VimeoInfo = with(this) {
  val video = VimeoInfo()
  video.id = element.id
  video.thumbPath = element.thumbPath
  video.videoPath = element.videoPath
  return video
}

fun DbVideoData.toVimeoInfo(): VimeoInfo = with(this) {
  val video = VimeoInfo()
  video.id = id
  video.thumbPath = element?.thumbPath
  video.videoPath = element?.videoPath
  return video
}

fun ApiVideoData.toDbVideoData(): DbVideoData {
  val video = DbVideoData()
  video.id = element.id
  video.element = element.toDbVimeoInfo()
  video.expireAt =
      return video
}

private fun VimeoInfo.toDbVimeoInfo(): DbVimeoInfo {
  val vimeo = DbVimeoInfo()
  vimeo.videoPath = videoPath
  vimeo.thumbPath = thumbPath
  return vimeo
}


//endregion
