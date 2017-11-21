package com.gigigo.orchextra.core.sdk.utils;

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtils {

  private static final String DATE_FORMAT_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  public static boolean isBetweenTwoDates(List<List<String>> dates) {
    if (dates == null) {
      return true;
    }

    Calendar calendar = Calendar.getInstance();
    Date calendarTime = calendar.getTime();

    for (List<String> date : dates) {
      if (date.size() == 2) {

        String startTimeString = date.get(0);
        String endTimeString = date.get(1);

        try {
          SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_TIME, Locale.getDefault());

          Date startTime = format.parse(startTimeString);
          Date endTime = format.parse(endTimeString);

          if (startTime.before(calendarTime) && endTime.after(calendarTime)) {
            return true;
          }

        } catch (Exception ignored) {
          ignored.printStackTrace();
        }
      }
    }

    return false;
  }

  public static boolean isAfterCurrentDate(String expiredAt) {
    Calendar calendar = Calendar.getInstance();
    Date calendarTime = calendar.getTime();

    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_TIME, Locale.getDefault());

    try {
      Date expiredAtDate = format.parse(expiredAt);
      return calendarTime.after(expiredAtDate);
    } catch (ParseException ignored) {
      return false;
    }
  }
}
