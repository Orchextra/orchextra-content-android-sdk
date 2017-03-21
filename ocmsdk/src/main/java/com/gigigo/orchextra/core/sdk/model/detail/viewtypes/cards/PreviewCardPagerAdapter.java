package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.cards.ElementCachePreviewCard;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;
import com.gigigo.ui.imageloader.ImageLoader;

class PreviewCardPagerAdapter extends FragmentPagerAdapter{

  private final ImageLoader imageLoader;
  private ElementCachePreviewCard previewCard;
  private ElementCacheShare shareContent;

  public PreviewCardPagerAdapter(FragmentManager fm, ImageLoader imageLoader) {
    super(fm);
    this.imageLoader = imageLoader;
  }

  @Override public int getCount() {
    return previewCard.getPreviewList().size();
  }

  public void setPreviewList(ElementCachePreviewCard previewCard) {
    this.previewCard = previewCard;
  }

  @Override public Fragment getItem(int position) {
    ElementCachePreview elementCachePreview = previewCard.getPreviewList().get(position);

    PreviewContentData previewContentData = PreviewContentData.newInstance();
    previewContentData.setImageLoader(imageLoader);
    previewContentData.setPreview(elementCachePreview);
    previewContentData.setShare(shareContent);

    return previewContentData;
  }

  public void setShareContent(ElementCacheShare shareContent) {
    this.shareContent = shareContent;
  }
}
