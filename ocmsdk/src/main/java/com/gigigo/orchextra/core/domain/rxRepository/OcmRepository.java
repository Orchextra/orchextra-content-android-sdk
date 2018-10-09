package com.gigigo.orchextra.core.domain.rxRepository;

import android.content.Context;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;

public interface OcmRepository {

  Observable<MenuContentData> getMenus();

  Observable<ContentData> getSectionElements(boolean forceReload, String elementUrl,
      int numberOfElementsToDownload);

  Observable<ElementData> getDetail(boolean forceReload, String section);

  Observable<VimeoInfo> getVideo(Context context, boolean forceReload, String videoId,
      boolean isWifiConnection, boolean isFastConnection);

  Observable<ContentData> doSearch(String textToSearch);

  Observable<Void> clear(boolean images, boolean data);
}
