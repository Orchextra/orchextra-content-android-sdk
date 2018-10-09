package com.gigigo.orchextra.core.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.gigigo.orchextra.core.data.database.entities.DbElement;
import com.gigigo.orchextra.core.data.database.entities.DbMenuElementJoin;
import com.gigigo.orchextra.core.data.database.entities.DbScheduleDates;
import java.util.List;

@Dao public interface DbScheduleDatesDao {
  @Query("SELECT * FROM schedule_dates WHERE element_slug = :slug") List<DbScheduleDates> fetchSchedule(String slug);

  @Query("SELECT * FROM schedule_dates WHERE element_slug = :slug AND date_start < :timestamp AND date_end > :timestamp") List<DbScheduleDates> fetchSlugOnTime(String slug, Long timestamp);

  @Insert(onConflict = OnConflictStrategy.REPLACE) void insertSchedule(DbScheduleDates scheduleDates);

  @Query("DELETE FROM schedule_dates") void deleteAll();
}
