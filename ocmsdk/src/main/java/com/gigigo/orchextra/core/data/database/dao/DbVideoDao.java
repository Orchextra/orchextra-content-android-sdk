package com.gigigo.orchextra.core.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbVideoData;

@Dao public interface DbVideoDao {
  @Query("SELECT * FROM video WHERE id = :id") DbVideoData fetchVideo(String id);

  @Query("SELECT COUNT(*) FROM video WHERE id = :id") int hasVideo(String id);

  @Query("SELECT COUNT(*) FROM video WHERE id = :id AND expire_at < :timestamp ") int hasExpiredVideo(String id, Long timestamp);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertVideo(DbVideoData videoData);

  @Query("DELETE FROM video") void deleteAll();
}
