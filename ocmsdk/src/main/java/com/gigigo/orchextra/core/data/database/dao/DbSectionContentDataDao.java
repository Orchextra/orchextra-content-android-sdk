package com.gigigo.orchextra.core.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gigigo.orchextra.core.data.database.entities.DbSectionContentData;
import com.gigigo.orchextra.core.data.database.entities.DbSectionElementJoin;
import java.util.List;

@Dao public interface DbSectionContentDataDao {
  @Query("SELECT * FROM section") List<DbSectionContentData> fetchAllSectionsContentData();

  @Query("SELECT * FROM section WHERE `key` = :key") DbSectionContentData fetchSectionContentData(
      String key);

  @Query("SELECT COUNT(*) FROM section WHERE `key` = :key") int hasSectionContentData(String key);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertSectionContentData(
      DbSectionContentData sectionContentData);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertSectionElement(
      DbSectionElementJoin sectionElementJoin);

  @Query("DELETE FROM section") void deleteAll();
}
