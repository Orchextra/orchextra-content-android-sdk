package com.gigigo.orchextra.core.data.api.dto.content;

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.mskn73.kache.Kacheable;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

//@KacheLife(expiresTime = 1000 * 60 * 60 * 24)  // 1 day
public class ApiSectionContentData implements Kacheable {

  private ApiContentItem content;
  private Map<String, ApiElementCache> elementsCache;
  private String key;
  private String version;
  private String expireAt;
  private boolean fromCloud;

  public ApiContentItem getContent() {
    return content;
  }

  public String getVersion() {
    return version;
  }

  public String getExpireAt() {
    return expireAt;
  }

  public Map<String, ApiElementCache> getElementsCache() {
    return elementsCache;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @NotNull @Override public String getKey() {
    return key;
  }

  public void setFromCloud(boolean fromCloud) {
    this.fromCloud = fromCloud;
  }

  public boolean isFromCloud() {
    return fromCloud;
  }
}
