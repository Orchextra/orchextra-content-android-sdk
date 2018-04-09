package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Context;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface OcmDataStore {

  Observable<VersionData> getVersion();

  Observable<MenuContentData> getMenus();

  Observable<ContentData> getSection(String elementUrl, int numberOfElementsToDownload);

  Observable<ContentData> searchByText(String section);

  Observable<ElementData> getElementById(String slug);

  Observable<VimeoInfo> getVideoById(Context context, String videoId, boolean isWifiConnection,
      boolean isFastConnection);

  boolean isFromCloud();
}
