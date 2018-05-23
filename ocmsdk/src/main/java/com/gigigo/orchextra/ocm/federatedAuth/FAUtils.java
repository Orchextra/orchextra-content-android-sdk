package com.gigigo.orchextra.ocm.federatedAuth;

import android.util.Pair;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public final class FAUtils {

  private static final String URL_START_QUERY_DELIMITER = "?";
  private static final String URL_CONCAT_QUERY_DELIMITER = "&";
  private static final String URL_QUERY_VALUE_DELIMITER = "=";

  private static String getQueryDelimiter(String url) {
    try {
      return new URL(url).getQuery() != null ? URL_CONCAT_QUERY_DELIMITER
          : URL_START_QUERY_DELIMITER;
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String addQueryParamsToUrl(List<Pair<String, String>> queryParams, String url) {
    if (getQueryDelimiter(url) != null) {
      url = url + getQueryDelimiter(url);

      Iterator<Pair<String, String>> iterator = queryParams.iterator();
      while (iterator.hasNext()) {
        Pair<String, String> pair = iterator.next();
        url =
            url + pair.first + URL_QUERY_VALUE_DELIMITER + pair.second + URL_CONCAT_QUERY_DELIMITER;
      }

      return url.substring(0, url.length() - 2);
    } else {
      return null;
    }
  }
}
