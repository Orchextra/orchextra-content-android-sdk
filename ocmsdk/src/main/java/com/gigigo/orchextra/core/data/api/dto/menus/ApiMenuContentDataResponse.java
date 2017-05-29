package com.gigigo.orchextra.core.data.api.dto.menus;

import com.gigigo.orchextra.core.data.api.dto.base.BaseApiResponse;
import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp;
import com.mskn73.kache.Kacheable;
import com.mskn73.kache.annotations.KacheLife;
import org.jetbrains.annotations.NotNull;

@KacheLife(expiresTime = 1000 * 60L)
public class ApiMenuContentDataResponse extends BaseApiResponse<ApiMenuContentData>
    implements Kacheable {

  @NotNull @Override public String getKey() {
    return OcmCacheImp.MENU_KEY;
  }
}
