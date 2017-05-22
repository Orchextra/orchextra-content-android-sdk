package com.gigigo.orchextra.core.domain.repository;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.exception.ErrorBundle;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.List;

/**
 * Created by francisco.hernandez on 22/5/17.
 */

public interface OcmRepository {

  interface MenuCallback {
    void onMenuLoaded(List<UiMenu> menus);
    void onError(ErrorBundle errorBundle);
  }

  interface ElementCacheCallback {
    void onCachedElementLoaded(ElementCache elementCache);
    void onError(ErrorBundle errorBundle);
  }

  interface ElementCacheBySectionCallback {
    void onCachedElementLoaded(ElementCache elementCache);
    void onError(ErrorBundle errorBundle);
  }

  interface ContentUrlBySection {
    void onContentUrlLoaded(String url);
    void onError(ErrorBundle errorBundle);
  }

  interface SectionContentById {
    void onSectionContentLoaded(String section);
    void onError(ErrorBundle errorBundle);
  }
}
