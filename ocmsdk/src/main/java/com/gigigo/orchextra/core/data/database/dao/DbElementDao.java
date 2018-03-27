package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbElement;
import java.util.List;

@Dao public interface DbElementDao {
  @Query("SELECT * FROM element") List<DbElement> fetchElements();

  @Query("SELECT COUNT(*) FROM element WHERE slug = :slug") int hasElement(String slug);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertElement(DbElement element);
}
