package com.gigigo.orchextra.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gigigo.orchextra.core.data.database.converters.Converters
import com.gigigo.orchextra.core.data.database.dao.DbElementCacheDao
import com.gigigo.orchextra.core.data.database.dao.DbElementDao
import com.gigigo.orchextra.core.data.database.dao.DbMenuContentDao
import com.gigigo.orchextra.core.data.database.dao.DbScheduleDatesDao
import com.gigigo.orchextra.core.data.database.dao.DbSectionContentDataDao
import com.gigigo.orchextra.core.data.database.dao.DbVideoDao
import com.gigigo.orchextra.core.data.database.entities.DbElement
import com.gigigo.orchextra.core.data.database.entities.DbElementCache
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent
import com.gigigo.orchextra.core.data.database.entities.DbMenuElementJoin
import com.gigigo.orchextra.core.data.database.entities.DbScheduleDates
import com.gigigo.orchextra.core.data.database.entities.DbSectionContentData
import com.gigigo.orchextra.core.data.database.entities.DbSectionElementJoin
import com.gigigo.orchextra.core.data.database.entities.DbVideoData

@Database(
    entities = [(DbMenuContent::class), (DbSectionContentData::class), (DbSectionElementJoin::class), (DbElement::class), (DbScheduleDates::class), (DbMenuElementJoin::class), (DbElementCache::class), (DbVideoData::class)],
    version = 4, exportSchema = true
)
@TypeConverters(value = [(Converters::class)])
abstract class OcmDatabase : RoomDatabase() {

    abstract fun menuDao(): DbMenuContentDao
    abstract fun elementDao(): DbElementDao
    abstract fun sectionDao(): DbSectionContentDataDao
    abstract fun scheduleDatesDao(): DbScheduleDatesDao
    abstract fun elementCacheDao(): DbElementCacheDao
    abstract fun videoDao(): DbVideoDao

    fun deleteAll() {
        menuDao().deleteAll()
        sectionDao().deleteAll()
        elementDao().deleteAll()
        scheduleDatesDao().deleteAll()
        elementCacheDao().deleteAll()
        videoDao().deleteAll()
    }

    companion object {
        private const val DEFAULT_DATABASE_NAME = "ocm.db"

        @JvmOverloads
        fun create(
            context: Context, useInMemory: Boolean = false,
            dbName: String = DEFAULT_DATABASE_NAME
        ): OcmDatabase {
            val databaseBuilder: Builder<OcmDatabase> = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, OcmDatabase::class.java)
            } else {
                Room.databaseBuilder(context, OcmDatabase::class.java, dbName)
            }

            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}