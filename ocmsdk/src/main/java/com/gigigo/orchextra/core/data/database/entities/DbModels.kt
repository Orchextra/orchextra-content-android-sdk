package com.gigigo.orchextra.core.data.database.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "version")
data class DbVersionData(
    @PrimaryKey var id: String = VERSION_KEY,
    var version: String = "")
{
 companion object {
    @JvmField
    val VERSION_KEY = "VERSION_KEY"
  }
}

data class DbMenuContentData @JvmOverloads constructor(
    var isFromCloud: Boolean = false,
    var menuContentList: List<DbMenuContent>? = emptyList(),
    var elementsCache: Map<String, DbElementCache>? = emptyMap())


@Entity(tableName = "menu")
data class DbMenuContent(
    @PrimaryKey var slug: String = "",
    var elements: List<DbElement> = emptyList())

@Entity(tableName = "element")
data class DbElement(
  @PrimaryKey var slug: String = "",
  var tags: List<String> = emptyList(),
  var customProperties: Map<String, Any> = emptyMap(),
  @Embedded(prefix = "view_") var sectionView: DbElementSectionView = DbElementSectionView(),
  var elementUrl: String = "",
  var name: String = ""//,
  //var dates: List<List<String>> = emptyList()
)

data class DbElementSectionView(
    var text: String? = "",
    var imageUrl: String? = "",
    var imageThumb: String? = ""
)


@Entity(tableName = "element_cache")
data class DbElementCache(
    @PrimaryKey var slug: String = "",
    var type: String? = "",
    var tags: List<String>? = emptyList(),
    @Embedded(prefix = "preview_") var preview: DbElementCachePreview? = DbElementCachePreview(),
    @Embedded(prefix = "render_") var render: DbElementCacheRender? = DbElementCacheRender(),
    @Embedded(prefix = "share_") var share: DbElementCacheShare? = DbElementCacheShare(),
    var customProperties: Map<String, Any>? = emptyMap(),
    var name: String? = "",
    var updatedAt: Long = 0)

data class DbElementCachePreview(
    var imageUrl: String? = "",
    var text: String? = "",
    var behaviour: String? = "",
    var imageThumb: String? = ""
)

data class DbElementCacheRender(
    var contentUrl: String? = "",
    var url: String? = "",
    var title: String? = "",
    var elements: List<DbArticleElement>? = emptyList(),
    var schemeUri: String? = "",
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
    var customProperties: Map<String, Any>?,
    @Embedded(prefix = "render_") var render: DbArticleElementRender? = DbArticleElementRender()
)

data class DbArticleElementRender(
    var text: String? = "",
    var imageUrl: String? = "",
    var elementUrl: String? = "",
    var html: String? = "",
    var format: String? = "",
    var source: String? = "",
    var imageThumb: String? = "",
    var ratios: List<Float>? = emptyList(),
    var type: String? = "",
    var size: String? = "",
    var textColor: String? = "",
    var bgColor: String? = ""
)

data class DbFederatedAuthorizationData(
    var active: Boolean = false,
    var type: String = "",
    @Embedded(prefix = "cid_") var keys: DbCidKeyData = DbCidKeyData()
)

data class DbCidKeyData(var siteName: String? = "")