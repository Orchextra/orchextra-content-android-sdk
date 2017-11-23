package com.gigigo.orchextra.core.sdk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
          Date startTime = convertStringToDateWithDeviceTimeZone(startTimeString);
          Date endTime = convertStringToDateWithDeviceTimeZone(endTimeString);

          if (startTime != null
              && endTime != null
              && startTime.before(calendarTime)
              && endTime.after(calendarTime)) {
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

    Date expiredAtDate = convertStringToDateWithDeviceTimeZone(expiredAt);

    return expiredAtDate != null && calendarTime.after(expiredAtDate);
  }

  private static Date convertStringToDateWithDeviceTimeZone(String timeStringToConvert) {
    try {
      SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_TIME, Locale.getDefault());

      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date startTime = format.parse(timeStringToConvert);

      TimeZone tz = TimeZone.getDefault();
      SimpleDateFormat destFormat = new SimpleDateFormat(DATE_FORMAT_TIME, Locale.getDefault());
      destFormat.setTimeZone(tz);

      String result = destFormat.format(startTime);
      return destFormat.parse(result);
    } catch (ParseException e) {
      return null;
    }
  }
}
