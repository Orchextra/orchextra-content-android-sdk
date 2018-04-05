package com.gigigo.orchextra.core.data.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore


@Entity(tableName = "version", primaryKeys = arrayOf("id"))
data class DbVersionData(
    var id: String = VERSION_KEY,
    var version: String? = "") {
  companion object {
    @JvmField
    val VERSION_KEY = "VERSION_KEY"
  }
}

/*
data class DbMenuContentData @JvmOverloads constructor(
    var isFromCloud: Boolean = false,
    var menuContentList: List<DbMenuContent>? = emptyList(),
    var elementsCache: Map<String, DbElementCache>? = emptyMap()
)


@Entity(tableName = "menu", primaryKeys = arrayOf("slug"))
data class DbMenuContent(
    var slug: String = "",
    @Ignore var elements: List<DbElement>? = emptyList()
)

@Entity(tableName = "menu_element_join",
    foreignKeys = arrayOf(
        ForeignKey(parentColumns = arrayOf("slug"), childColumns = arrayOf("menu_slug"),
            entity = DbMenuContent::class),
        ForeignKey(parentColumns = arrayOf("slug"), childColumns = arrayOf("element_slug"),
            entity = DbElement::class)),
    primaryKeys = arrayOf("menu_slug", "element_slug")
)
data class DbMenuElementJoin(
    @ColumnInfo(name = "menu_slug") var menuSlug: String = "",
    @ColumnInfo(name = "element_slug") var elementSlug: String = ""
)

@Entity(tableName = "element", primaryKeys = arrayOf("slug"))
data class DbElement(
    var slug: String = "",
    var tags: List<String>? = emptyList(),
    @ColumnInfo(name = "custom_properties") var customProperties: Map<String, String>? = emptyMap(),
    @Embedded(prefix = "view_") var sectionView: DbElementSectionView? = DbElementSectionView(),
    @ColumnInfo(name = "element_url") var elementUrl: String? = "",
    var name: String? = ""
    //var dates: List<List<String>> = emptyList()
)
*/
data class DbElementSectionView(
    var text: String? = "",
    @ColumnInfo(name = "image_url") var imageUrl: String? = "",
    @ColumnInfo(name = "image_thumb") var imageThumb: String? = ""
)

@Entity(tableName = "element_cache", primaryKeys = arrayOf("slug"))
data class DbElementCache(
    var slug: String = "",
    var type: String? = "",
    var tags: List<String>? = emptyList(),
    @Embedded(prefix = "preview_") var preview: DbElementCachePreview? = DbElementCachePreview(),
    @Embedded(prefix = "render_") var render: DbElementCacheRender? = DbElementCacheRender(),
    @Embedded(prefix = "share_") var share: DbElementCacheShare? = DbElementCacheShare(),
    @ColumnInfo(name = "custom_properties") var customProperties: Map<String, String>? = emptyMap(),
    var name: String? = "",
    @ColumnInfo(name = "updated_at") var updatedAt: Long = 0
)

data class DbElementCachePreview(
    @ColumnInfo(name = "image_url") var imageUrl: String? = "",
    var text: String? = "",
    var behaviour: String? = "",
    @ColumnInfo(name = "image_thumb") var imageThumb: String? = ""
)

data class DbElementCacheRender(
    @ColumnInfo(name = "content_url") var contentUrl: String? = "",
    var url: String? = "",
    var title: String? = "",
    var elements: List<DbArticleElement>? = emptyList(),
    @ColumnInfo(name = "scheme_uri") var schemeUri: String? = "",
    var source: String? = "",
    var format: String? = "",
    @Embedded(prefix = "federated_") var federatedAuth: DbFederatedAuthorizationData? = DbFederatedAuthorizationData()
)

data class DbElementCacheShare(
    var url: String? = "",
    var text: String? = ""
)

data class DbArticleElement(
    var type: String? = "",
    @ColumnInfo(name = "custom_properties") var customProperties: Map<String, String>? = emptyMap(),
    @Embedded(prefix = "render_") var render: DbArticleElementRender? = DbArticleElementRender()
)

data class DbArticleElementRender(
    var text: String? = "",
    @ColumnInfo(name = "image_url") var imageUrl: String? = "",
    @ColumnInfo(name = "element_url") var elementUrl: String? = "",
    var html: String? = "",
    var format: String? = "",
    var source: String? = "",
    @ColumnInfo(name = "image_thumb") var imageThumb: String? = "",
    var ratios: List<Float>? = emptyList(),
    var type: String? = "",
    var size: String? = "",
    @ColumnInfo(name = "text_color") var textColor: String? = "",
    @ColumnInfo(name = "bg_color") var bgColor: String? = ""
)

data class DbFederatedAuthorizationData(
    var active: Boolean = false,
    var type: String? = "",
    @Embedded(prefix = "cid_") var keys: DbCidKeyData? = DbCidKeyData()
)

data class DbCidKeyData(@ColumnInfo(name = "site_name") var siteName: String? = "")

@Entity(tableName = "video", primaryKeys = arrayOf("id"))
data class DbVideoData(
    var id: String = "",
    @Embedded(prefix = "vimeo_") var element: DbVimeoInfo? = DbVimeoInfo(),
    @ColumnInfo(name = "expire_at") var expireAt: Long? = -1
)

data class DbVimeoInfo(
    var videoPath: String? = "",
    var thumbPath: String? = ""
)

/*
@Entity(tableName = "sections")
data class DbSectionContentData(
    @Embedded(prefix = "content_")val content: DbContentItem? = DbContentItem(),
    @ColumnInfo(name = "elements_cache")val elementsCache: Map<String, ApiElementCache>? = emptyMap(),
    val version: String? = null,
    @ColumnInfo(name = "expire_at") val expireAt: String? = null,
    @ColumnInfo(name = "is_from_cloud")var isFromCloud: Boolean = false
)

data class DbContentItem(
    val slug: String = "",
    val type: String? = "",
    val tags: List<String>? = emptyList(),
    @Embedded(prefix = "layout_") val layout: DbContentItemLayout? = DbContentItemLayout(),
    val elements: List<DbElement>? = emptyList())

data class DbContentItemLayout(
    val name: String? = "",
    val pattern: List<DbContentItemPattern>? = emptyList(),
    val type: String? = "")

data class DbContentItemPattern(
    val row: Int = 0,
    val column: Int = 0)
*/