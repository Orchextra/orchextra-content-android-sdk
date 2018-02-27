package com.gigigo.orchextra.core.data.api.dto.video

import com.mskn73.kache.Kacheable
import com.mskn73.kache.annotations.KacheLife
import gigigo.com.vimeolibs.VimeoInfo

@KacheLife(expiresTime = 1000 * 60 * 60 * 24) // 1 day
class ApiVideoData(val element: VimeoInfo) : Kacheable {

  override val key: String
    get() = element.id
}
