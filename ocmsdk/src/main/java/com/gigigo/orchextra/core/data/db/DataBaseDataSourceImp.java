package com.gigigo.orchextra.core.data.db;

import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.data.DataBaseDataSource;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import java.util.HashMap;
import java.util.Map;

public class DataBaseDataSourceImp implements DataBaseDataSource {

  private MenuContentData savedMenuContentData;
  private Map<String, ElementCache> elementCacheDataList;
  private Map<String, ContentItem> contentDataList;

  public DataBaseDataSourceImp() {
    elementCacheDataList = new HashMap<>();
    contentDataList = new HashMap<>();
  }

  @Override public BusinessObject<MenuContentData> retrieveMenu() {
    return new BusinessObject<>(savedMenuContentData, BusinessError.createOKInstance());
  }

  @Override public void saveMenu(MenuContentData data) {
    savedMenuContentData = data;
  }

  @Override public void clearMenu() {
    savedMenuContentData = null;
  }

  @Override public void saveElementWithId(String elementId, ElementCache elementCache) {
    elementCacheDataList.put(elementId, elementCache);
  }

  @Override public void clearElements() {
    elementCacheDataList = new HashMap<>();
  }

  @Override
  public ElementCache retrieveElementById(String elementId) {
    return elementCacheDataList.get(elementId);
  }

  @Override public ContentItem retrieveDetailContentData(String section) {
    return contentDataList.get(section);
  }

  @Override public void saveDetailElementContentItemBySection(String section, ContentItem contentItem) {
    contentDataList.put(section, contentItem);
  }

  @Override public void clearDetailElements() {
    contentDataList = new HashMap<>();
  }
}
