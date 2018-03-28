package com.gigigo.orchextra.core.data.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity


@Entity(tableName = "version", primaryKeys = arrayOf("id"))
data class DbVersionData(
    var id: String = VERSION_KEY,
    var version: String = "") {
  companion object {
    @JvmField
    val VERSION_KEY = "VERSION_KEY"
  }
}

@Entity(tableName = "video", primaryKeys = arrayOf("id"))
data class DbVideoData(
    var id : String = "",
    @Embedded(prefix = "vimeo_") var element: DbVimeoInfo? = DbVimeoInfo(),
    @ColumnInfo(name = "expire_at") var expireAt : Long? = -1
)

data class DbVimeoInfo(
    var videoPath: String? = "",
    var thumbPath: String? = ""
)
