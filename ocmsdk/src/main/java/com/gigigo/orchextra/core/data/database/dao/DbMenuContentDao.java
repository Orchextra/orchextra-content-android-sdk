package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;

@Dao public interface DbMenuContentDao {
  /*
  @Query("SELECT * FROM menu") List<DbMenuContent> fetchMenus();

  @Query("SELECT COUNT(*) FROM menu WHERE slug = :slug") int hasMenus(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertMenu(DbMenuContent menu);
  */
}
