package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;

@Dao public interface DbElementCacheDao {
  /*
  @Query("SELECT * FROM element_cache") List<DbElementCache> fetchElementsCache();

  @Query("SELECT COUNT(*) FROM element_cache WHERE slug = :slug") int hasElementCache(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertElementCache(DbElementCache elementCache);
  */
}
