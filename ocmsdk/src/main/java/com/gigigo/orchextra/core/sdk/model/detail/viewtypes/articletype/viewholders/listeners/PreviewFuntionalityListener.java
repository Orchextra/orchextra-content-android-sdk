package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.listeners;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;

public interface PreviewFuntionalityListener {

  void onClickShare(ElementCacheShare share);

  void onClickGoToArticleButton();

  void disablePreviewScrolling();
}
