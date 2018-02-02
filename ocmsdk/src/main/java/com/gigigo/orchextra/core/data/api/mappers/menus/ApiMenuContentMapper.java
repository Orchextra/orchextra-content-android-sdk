package com.gigigo.orchextra.core.data.api.mappers.menus;

import android.util.Log;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent;
import com.gigigo.orchextra.core.data.api.mappers.elements.ApiElementMapper;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent;
import java.util.ArrayList;
import java.util.List;

public class ApiMenuContentMapper
    implements ExternalClassToModelMapper<ApiMenuContent, MenuContent> {

  private final ApiElementMapper apiMenuItemMapper;

  public ApiMenuContentMapper(ApiElementMapper apiMenuItemMapper) {
    this.apiMenuItemMapper = apiMenuItemMapper;
  }

  @Override public MenuContent externalClassToModel(ApiMenuContent data) {
    final long time = System.currentTimeMillis();

    MenuContent model = new MenuContent();

    model.setId(data.getId());
    model.setSlug(data.getSlug());

    List<Element> menuItemList = new ArrayList<>();
    if (data.getElements() != null) {
      for (ApiElement apiMenuItem : data.getElements()) {
        menuItemList.add(apiMenuItemMapper.externalClassToModel(apiMenuItem));
      }
    }

    model.setElements(menuItemList);

    Log.v("TT - ApiMenuContent", (System.currentTimeMillis() - time) / 1000 + "");

    return model;
  }
}
