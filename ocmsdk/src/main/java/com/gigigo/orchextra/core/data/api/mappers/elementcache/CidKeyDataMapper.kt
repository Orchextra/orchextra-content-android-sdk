package com.gigigo.orchextra.core.data.api.mappers.elementcache

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.elementcache.CidKeyData
import com.gigigo.orchextra.core.domain.entities.elementcache.CidKey

class CidKeyDataMapper : ExternalClassToModelMapper<CidKeyData, CidKey> {

  override fun externalClassToModel(data: CidKeyData?): CidKey {
    val time = System.currentTimeMillis()

    val model = CidKey()

    data?.let {
      model.siteName = data.siteName
    }

    Log.v("TT - CidKeyData", ((System.currentTimeMillis() - time) / 1000).toString() + "")

    return model
  }
}