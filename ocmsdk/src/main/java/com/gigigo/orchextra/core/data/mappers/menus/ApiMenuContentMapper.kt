package com.gigigo.orchextra.core.data.mappers.menus

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.menus.ApiMenuContent
import com.gigigo.orchextra.core.data.mappers.elements.ApiElementMapper
import com.gigigo.orchextra.core.domain.entities.elements.Element
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent
import java.util.ArrayList

class ApiMenuContentMapper(
    private val apiMenuItemMapper: ApiElementMapper) : ExternalClassToModelMapper<ApiMenuContent, MenuContent> {

  override fun externalClassToModel(data: ApiMenuContent): MenuContent {
    val time = System.currentTimeMillis()

    val model = MenuContent()

    model.slug = data.slug

    val menuItemList = ArrayList<Element>()

    data.elements?.let {
      for (apiMenuItem in data.elements) {
        apiMenuItemMapper.externalClassToModel(apiMenuItem)?.let {
          menuItemList.add(it)
        }
      }
    }

    model.elements = menuItemList

    Log.v("TT - ApiMenuContent", ((System.currentTimeMillis() - time) / 1000).toString() + "")

    return model
  }
}