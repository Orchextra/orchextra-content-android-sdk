package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbVersionData;

@Dao public interface DbVersionDataDao {
  @Query("SELECT * FROM version WHERE id = :versionKey") DbVersionData fetchVersion(String versionKey);
  @Query("SELECT COUNT(*) FROM version WHERE id = :versionKey") int hasVersion(String versionKey);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertVersion(DbVersionData versionData);
  @Query("DELETE FROM version") void deleteAll();
}
