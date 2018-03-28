package com.gigigo.orchextra.core.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.gigigo.orchextra.core.data.database.dao.DbVersionDataDao
import com.gigigo.orchextra.core.data.database.dao.DbVideoDao
import com.gigigo.orchextra.core.data.database.entities.DbVersionData
import com.gigigo.orchextra.core.data.database.entities.DbVideoData

@Database(
    entities = arrayOf(DbVersionData::class, DbVideoData::class),
    version = 1, exportSchema = true)
abstract class OcmDatabase : RoomDatabase() {

  abstract fun versionDao(): DbVersionDataDao
  abstract fun videoDao(): DbVideoDao

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