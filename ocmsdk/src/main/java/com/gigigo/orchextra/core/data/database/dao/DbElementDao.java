package com.gigigo.orchextra.core.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbElement;
import com.gigigo.orchextra.core.data.database.entities.DbMenuElementJoin;
import java.util.List;

@Dao public interface DbElementDao {
  @Query("SELECT * FROM element WHERE slug = :slug") DbElement fetchElement(String slug);

  @Query("SELECT DISTINCT element.* FROM element INNER JOIN menu_element_join ON element_slug = slug WHERE menu_slug = :menuSlug") List<DbElement> fetchMenuElements(String menuSlug);

  @Query("SELECT DISTINCT element.* FROM element INNER JOIN schedule_dates WHERE slug = :slug AND date_start < :timestamp AND date_end > :timestamp") List<DbElement> fetchMenuElementsOnTime(String slug, Long timestamp);

  @Query("SELECT COUNT(*) FROM element WHERE slug = :slug") int hasElement(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertElement(DbElement element);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertMenuElement(DbMenuElementJoin menuElementJoin);

  @Query("DELETE FROM element") void deleteAll();

  @Query("SELECT element.* FROM element INNER JOIN section_element_join ON element.slug = section_element_join.element_slug WHERE section_slug = :sectionSlug")
  List<DbElement> fetchSectionElements(String sectionSlug);
}
