package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardRichTextViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardVideoViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardViewElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.PreviewContentDataView;
import com.gigigo.ui.imageloader.ImageLoader;

public class VerticalPagerAdapter extends PagerAdapter {

  private final Context context;
  private final ImageLoader imageLoader;
  private final ElementCache elementCache;

  public VerticalPagerAdapter(Context context, ImageLoader imageLoader, ElementCache elementCache) {
    this.context = context;
    this.imageLoader = imageLoader;
    this.elementCache = elementCache;
  }

  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override public int getCount() {
    if (elementCache == null
        || elementCache.getRender() == null
        || elementCache.getRender().getElements() == null) {
      return 0;
    }

    int numElements = elementCache.getRender().getElements().size();

    if (elementCache.getPreview() != null) {
      return numElements + 1;
    } else {
     return numElements;
    }
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {

    if (elementCache.getPreview() != null && position == 0) {
      PreviewContentDataView previewCardContentData = new PreviewContentDataView(context);
      previewCardContentData.setImageLoader(imageLoader);
      previewCardContentData.setPreview(elementCache.getPreview());
      previewCardContentData.setShare(elementCache.getShare());
      previewCardContentData.initialize();

      container.addView(previewCardContentData);

      return previewCardContentData;
    } else {

      int adapterPosition = position;

      if (elementCache.getPreview() != null) {
        adapterPosition--;
      }

      CardViewElement cardViewElement = null;

      ArticleElement articleElement = elementCache.getRender().getElements().get(adapterPosition);
      if (articleElement.getClass() == ArticleImageElement.class) {
        CardImageViewHolder cardImageViewHolder = new CardImageViewHolder(context);
        cardImageViewHolder.setImageLoader(imageLoader);
        cardImageViewHolder.setImageElement((ArticleImageElement) articleElement);
        cardImageViewHolder.initialize();

        cardViewElement = cardImageViewHolder;
      } else if (articleElement.getClass() == ArticleRichTextElement.class) {
        CardRichTextViewHolder cardRichTextViewHolder = new CardRichTextViewHolder(context);
        cardRichTextViewHolder.setRichTextElement((ArticleRichTextElement) articleElement);
        cardRichTextViewHolder.initialize();

        cardViewElement = cardRichTextViewHolder;
      } else if (articleElement.getClass() == ArticleVideoElement.class) {
        CardVideoViewHolder cardVideoViewHolder = new CardVideoViewHolder(context);
        cardVideoViewHolder.setArticleElement((ArticleVideoElement) articleElement);
        cardVideoViewHolder.initialize();

        cardViewElement = cardVideoViewHolder;
      }

      container.addView(cardViewElement);

      return cardViewElement;
    }
  }
}
