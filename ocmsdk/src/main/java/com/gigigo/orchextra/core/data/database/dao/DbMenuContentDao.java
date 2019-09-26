package com.gigigo.orchextra.core.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbMenuContent;
import java.util.List;

@Dao public interface DbMenuContentDao {
  @Query("SELECT * FROM menu") List<DbMenuContent> fetchMenus();

  @Query("SELECT COUNT(*) FROM menu") int hasMenus();

  @Query("SELECT * FROM menu WHERE slug = :slug") DbMenuContent fetchMenu(String slug);

  @Query("SELECT COUNT(*) FROM menu WHERE slug = :slug") int hasMenu(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertMenu(DbMenuContent menu);

  @Query("DELETE FROM menu") void deleteAll();
}
