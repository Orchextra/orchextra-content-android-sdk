package com.gigigo.showcase.presentation.view.settings;

import android.support.annotation.NonNull;
import com.gigigo.showcase.BuildConfig;
import java.util.ArrayList;
import java.util.List;

public final class ProjectData {

  @NonNull private final String name;
  @NonNull private final String apiKey;
  @NonNull private final String apiSecret;

  public ProjectData(@NonNull String name, @NonNull String apiKey, @NonNull String apiSecret) {
    this.name = name;
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
  }

  @NonNull public String getName() {
    return name;
  }

  @NonNull public String getApiKey() {
    return apiKey;
  }

  @NonNull public String getApiSecret() {
    return apiSecret;
  }

  @NonNull public static List<ProjectData> getDefaultProjectDataList() {

    List<ProjectData> projectDataList = new ArrayList<>();

    if (BuildConfig.BUILD_TYPE == "release") {

      projectDataList.add(new ProjectData("Default", "33ecdcbe03d60cb530e6ae13a531a3c9cf3c150e",
          "be772ab61e2571230c596aa95237cc618023befb"));

      projectDataList.add(
          new ProjectData("[PRO][ES] ORCHEXTRA DEMO", "9d9f74d0a9b293a2ea1a7263f47e01baed2cb0f3",
              "6a4d8072f2a519c67b0124656ce6cb857a55276a"));

      projectDataList.add(
          new ProjectData("[UAT] WOAH MARKETS", "ef08c4dccb7649b9956296a863db002a68240be2",
              "6bc18c500546f253699f61c11a62827679178400"));
    } else if (BuildConfig.BUILD_TYPE == "quality") {

      projectDataList.add(
          new ProjectData("[UAT][ES] ORCHEXTRA DEMO", "f01b8a0912540ba0c358dfc7dedca4f3b4aab2a5",
              "df36979bd7f844d0fa1a0cf0add796aa6735f90e"));

      projectDataList.add(new ProjectData("[UAT][ES] PUSH NOTIFICATION TEST",
          "a332a27f4c136f4bf3d6958035cffdf2ee13f805", "54949ba59875b6465f292d760a389d04213af799"));
    } else {

      projectDataList.add(new ProjectData("Orchextra Demo - {{staging}}",
          "8286702045adf5a3ad816f70ecb80e4c91fbb8de", "eab37080130215ced60eb9d5ff729049749ec205"));

      projectDataList.add(
          new ProjectData("ANDROID SDK - {{staging}}", "34a4654b9804eab82aae05b2a5f949eb2a9f412c",
              "2d5bce79e3e6e9cabf6d7b040d84519197dc22f3"));
    }

    return projectDataList;
  }

  @NonNull public static String getDefaultApiKey() {
    return getDefaultProjectDataList().get(0).apiKey;
  }

  @NonNull public static String getDefaultApiSecret() {
    return getDefaultProjectDataList().get(0).apiSecret;
  }
}