package com.gigigo.orchextra.core.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gigigo.orchextra.core.data.database.entities.DbScheduleDates;
import java.util.List;

@Dao public interface DbScheduleDatesDao {
  @Query("SELECT * FROM schedule_dates WHERE element_slug = :slug") List<DbScheduleDates> fetchSchedule(String slug);

  @Query("SELECT * FROM schedule_dates WHERE element_slug = :slug AND date_start < :timestamp AND date_end > :timestamp") List<DbScheduleDates> fetchSlugOnTime(String slug, Long timestamp);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertSchedule(DbScheduleDates scheduleDates);

  @Query("DELETE FROM schedule_dates") void deleteAll();
}
