package com.gigigo.orchextra.core.domain.data;

import com.gigigo.interactorexecutor.interactors.InteractorResponse;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;

public interface DataBaseDataSource {
  BusinessObject<MenuContentData> retrieveMenu();

  void saveMenu(MenuContentData data);

  void clearMenu();

  void saveElementWithId(String elementId, ElementCache data);

  void clearElements();

  ElementCache retrieveElementById(String elementId);

  ContentItem retrieveDetailContentData(String section);

  void saveDetailElementContentItemBySection(String section, ContentItem contentItem);

  void clearDetailElements();
}
