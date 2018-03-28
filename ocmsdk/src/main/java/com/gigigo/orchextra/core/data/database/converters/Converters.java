package com.gigigo.orchextra.core.data.database.converters;

import android.arch.persistence.room.TypeConverter;
import com.gigigo.orchextra.core.data.database.entities.DbArticleElement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Converters {
  @TypeConverter public static Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
  }

  @TypeConverter public static Long dateToTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }

  @TypeConverter public static List<DbArticleElement> fromJsonToListArticle(String value) {
    Type listType = new TypeToken<List<DbArticleElement>>() {
    }.getType();
    return new Gson().fromJson(value, listType);
  }

  @TypeConverter public static String fromListArticleToJson(List<DbArticleElement> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
  }

  @TypeConverter public static List<String> fromJsonToListString(String value) {
    Type listType = new TypeToken<List<String>>() {
    }.getType();
    return new Gson().fromJson(value, listType);
  }

  @TypeConverter public static String fromListStringToJson(List<String> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
  }

  @TypeConverter public static List<Float> fromJsonToListFloat(String value) {
    Type listType = new TypeToken<List<Float>>() {
    }.getType();
    return new Gson().fromJson(value, listType);
  }

  @TypeConverter public static String fromListFloatToJson(List<Float> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
  }

  @TypeConverter public static List<? extends List<?>> fromJsonToListList(String value) {
    Type listType = new TypeToken<List<? extends List<?>>>() {
    }.getType();
    return new Gson().fromJson(value, listType);
  }

  @TypeConverter public static String fromListListToJson(List<? extends List<?>> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
  }

  @TypeConverter public static Map<String, ? extends Object> fromJsonToMap(String value) {
    Type mapType = new TypeToken<Map<String, ? extends Object>>() {
    }.getType();
    return new Gson().fromJson(value, mapType);
  }

  @TypeConverter public static String fromMapToJson(Map<String, ? extends Object> map) {
    Gson gson = new Gson();
    String json = gson.toJson(map);
    return json;
  }
}