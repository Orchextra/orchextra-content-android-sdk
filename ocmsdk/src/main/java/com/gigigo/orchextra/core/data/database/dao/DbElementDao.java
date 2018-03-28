package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;

@Dao public interface DbElementDao {
  /*
  @Query("SELECT * FROM element") List<DbElement> fetchElements();

  @Query("SELECT COUNT(*) FROM element WHERE slug = :slug") int hasElement(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertElement(DbElement element);
  */
}
