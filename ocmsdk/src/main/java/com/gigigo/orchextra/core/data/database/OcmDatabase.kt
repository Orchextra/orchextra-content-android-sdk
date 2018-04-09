package com.gigigo.orchextra.core.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.gigigo.orchextra.core.data.database.converters.Converters
import com.gigigo.orchextra.core.data.database.dao.DbElementCacheDao
import com.gigigo.orchextra.core.data.database.dao.DbElementDao
import com.gigigo.orchextra.core.data.database.dao.DbMenuContentDao
import com.gigigo.orchextra.core.data.database.dao.DbScheduleDatesDao
import com.gigigo.orchextra.core.data.database.dao.DbSectionContentDataDao
import com.gigigo.orchextra.core.data.database.dao.DbVersionDataDao
import com.gigigo.orchextra.core.data.database.dao.DbVideoDao
import com.gigigo.orchextra.core.data.database.entities.DbElement
import com.gigigo.orchextra.core.data.database.entities.DbElementCache
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent
import com.gigigo.orchextra.core.data.database.entities.DbMenuElementJoin
import com.gigigo.orchextra.core.data.database.entities.DbScheduleDates
import com.gigigo.orchextra.core.data.database.entities.DbSectionContentData
import com.gigigo.orchextra.core.data.database.entities.DbSectionElementJoin
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.data.database.entities.DbVideoData

@Database(
    entities = arrayOf(DbVersionData::class, DbMenuContent::class,
        DbSectionContentData::class, DbSectionElementJoin::class,
        DbElement::class, DbScheduleDates::class,
        DbMenuElementJoin::class, DbElementCache::class, DbVideoData::class),
    version = 1, exportSchema = true)
@TypeConverters(value = arrayOf(Converters::class))
abstract class OcmDatabase : RoomDatabase() {

  abstract fun versionDao(): DbVersionDataDao
  abstract fun menuDao(): DbMenuContentDao
  abstract fun elementDao(): DbElementDao
  abstract fun sectionDao(): DbSectionContentDataDao
  abstract fun scheduleDatesDao(): DbScheduleDatesDao
  abstract fun elementCacheDao(): DbElementCacheDao
  abstract fun videoDao(): DbVideoDao

  fun deleteAll() {
    versionDao().deleteAll()
    menuDao().deleteAll()
    sectionDao().deleteAll()
    elementDao().deleteAll()
    scheduleDatesDao().deleteAll()
    elementCacheDao().deleteAll()
    videoDao().deleteAll()
  }

  companion object {
    private val DEFAULT_DATABASE_NAME = "ocm.db"

    @JvmOverloads
    fun create(context: Context, useInMemory: Boolean = false,
        dbName: String = DEFAULT_DATABASE_NAME): OcmDatabase {
      val databaseBuilder: RoomDatabase.Builder<OcmDatabase>
      if (useInMemory) {
        databaseBuilder = Room.inMemoryDatabaseBuilder(context, OcmDatabase::class.java)
      } else {
        databaseBuilder = Room.databaseBuilder(context, OcmDatabase::class.java, dbName)
      }

      return databaseBuilder
          .fallbackToDestructiveMigration()
          .build()
    }
  }
}