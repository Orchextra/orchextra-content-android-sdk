package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.cards.ElementCachePreviewCard;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;

class PreviewCardPagerAdapter extends FragmentStatePagerAdapter {

  private ElementCachePreviewCard previewCard;

  public PreviewCardPagerAdapter(FragmentManager fm) {
    super(fm);
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
    previewContentData.setPreview(elementCachePreview);

    return previewContentData;
  }
}
