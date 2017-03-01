package com.gigigo.orchextra.core.domain.data;

import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;

public interface ElementNetworkDataSource {
  BusinessObject<ElementData> getElementById(String elementId);
}
