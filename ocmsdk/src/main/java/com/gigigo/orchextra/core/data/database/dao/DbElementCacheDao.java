package com.gigigo.orchextra.core.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbElementCache;
import java.util.List;

@Dao public interface DbElementCacheDao {
  @Query("SELECT * FROM element_cache") List<DbElementCache> fetchElementsCache();

  @Query("SELECT * FROM element_cache WHERE slug = :slug") DbElementCache fetchElementCache(String slug);

  @Query("SELECT COUNT(*) FROM element_cache WHERE slug = :slug") int hasElementCache(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertElementCache(
      DbElementCache elementCache);

  @Query("DELETE FROM element_cache") void deleteAll();
}
