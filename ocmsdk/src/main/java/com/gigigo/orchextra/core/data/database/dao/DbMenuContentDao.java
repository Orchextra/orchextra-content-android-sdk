package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent;
import java.util.List;

@Dao public interface DbMenuContentDao {
  @Query("SELECT * FROM menu") List<DbMenuContent> fetchMenus();

  @Query("SELECT COUNT(*) FROM menu WHERE slug = :slug") int hasMenus(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertMenu(DbMenuContent menu);
}
