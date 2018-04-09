package com.gigigo.orchextra.core.data.database.converters;

import android.arch.persistence.room.TypeConverter;
import com.gigigo.orchextra.core.data.database.entities.DbArticleElement;
import com.gigigo.orchextra.core.data.database.entities.DbContentItemPattern;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Converters {
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

  @TypeConverter public static List<DbContentItemPattern> fromJsonToListContentItemPattern(String value) {
    Type listType = new TypeToken<List<DbContentItemPattern>>() {
    }.getType();
    return new Gson().fromJson(value, listType);
  }

  @TypeConverter public static String fromLisContentItemPatternToJson(List<DbContentItemPattern> list) {
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

  @TypeConverter public static Map<String, String> fromJsonToMap(String value) {
    Type mapType = new TypeToken<Map<String, String>>() {
    }.getType();
    return new Gson().fromJson(value, mapType);
  }

  @TypeConverter public static String fromMapToJson(Map<String, String> map) {
    Gson gson = new Gson();
    String json = gson.toJson(map);
    return json;
  }
}