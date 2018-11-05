package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbElement;
import com.gigigo.orchextra.core.data.database.entities.DbSectionContentData;
import com.gigigo.orchextra.core.data.database.entities.DbSectionElementJoin;
import java.util.List;

@Dao public interface DbSectionContentDataDao {
  @Query("SELECT * FROM section") List<DbSectionContentData> fetchAllSectionsContentData();

  @Query("SELECT * FROM section WHERE `key` = :key") DbSectionContentData fetchSectionContentData(
      String key);

  @Query("SELECT COUNT(*) FROM section WHERE `key` = :key") int hasSectionContentData(String key);

  //@Query("SELECT DISTINCT element.* FROM element INNER JOIN section_element_join ON element.slug = section_element_join.element_slug WHERE section_slug = :sectionSlug")
  //List<DbElement> fetchSectionElements(String sectionSlug);

  @Query("SELECT element_cache.* FROM element_cache INNER JOIN section_element_join ON element_cache.slug = section_element_join.element_slug WHERE section_slug = :sectionSlug")
  List<DbElement> fetchSectionElements(String sectionSlug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertSectionContentData(
      DbSectionContentData sectionContentData);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertSectionElement(
      DbSectionElementJoin sectionElementJoin);

  @Query("DELETE FROM section") void deleteAll();
}
