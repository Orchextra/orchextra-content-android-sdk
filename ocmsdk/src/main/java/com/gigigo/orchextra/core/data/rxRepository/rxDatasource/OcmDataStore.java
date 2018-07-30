package com.gigigo.orchextra.core.data.rxRepository.rxDatasource;

import android.content.Context;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface OcmDataStore {

  Observable<ContentData> getSection(String elementUrl, int numberOfElementsToDownload);

  Observable<ContentData> searchByText(String section);

  Observable<ElementData> getElementById(String slug);

  Observable<VimeoInfo> getVideoById(Context context, String videoId, boolean isWifiConnection,
      boolean isFastConnection);

  boolean isFromCloud();
}
