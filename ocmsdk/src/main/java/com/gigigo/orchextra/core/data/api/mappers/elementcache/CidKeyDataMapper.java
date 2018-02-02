package com.gigigo.orchextra.core.data.api.mappers.elementcache;

import android.util.Log;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elementcache.CidKeyData;
import com.gigigo.orchextra.core.domain.entities.elementcache.CidKey;

public class CidKeyDataMapper implements ExternalClassToModelMapper<CidKeyData, CidKey> {

  @Override public CidKey externalClassToModel(CidKeyData data) {
    final long time = System.currentTimeMillis();

    CidKey model = new CidKey();

    if (data != null) {
      model.setSiteName(data.getSiteName());
    }

    Log.v("TT - CidKeyData", (System.currentTimeMillis() - time) / 1000 + "");

    return model;
  }
}
