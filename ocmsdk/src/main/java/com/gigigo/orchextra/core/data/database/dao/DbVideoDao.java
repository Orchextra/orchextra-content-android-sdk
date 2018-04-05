package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbVideoData;
import java.util.List;

@Dao public interface DbVideoDao {
  @Query("SELECT * FROM video WHERE id = :id") DbVideoData fetchVideo(String id);

  @Query("SELECT COUNT(*) FROM video WHERE id = :id") int hasVideo(String id);

  @Query("SELECT COUNT(*) FROM video WHERE id = :id AND expire_at < :timestamp ") int hasExpiredVideo(String id, Long timestamp);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertVideo(DbVideoData videoData);
}
