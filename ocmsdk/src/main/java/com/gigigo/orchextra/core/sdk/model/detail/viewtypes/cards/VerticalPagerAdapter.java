package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageAndTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleTextAndImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageAndTextDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardRichTextDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.PreviewContentDataView;

public class VerticalPagerAdapter extends PagerAdapter {

  private final Context context;
  private final ElementCache elementCache;

  public VerticalPagerAdapter(Context context, ElementCache elementCache) {
    this.context = context;
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
      previewCardContentData.setPreview(elementCache.getPreview());
      previewCardContentData.initialize();

      container.addView(previewCardContentData);

      return previewCardContentData;
    } else {

      int adapterPosition = position;

      if (elementCache.getPreview() != null) {
        adapterPosition--;
      }

      CardDataView cardViewElement = null;

      ArticleElement articleElement = elementCache.getRender().getElements().get(adapterPosition);
      if (articleElement.getClass() == ArticleImageElement.class) {
        CardImageDataView cardImageViewHolder = new CardImageDataView(context);
        cardImageViewHolder.setImageElement((ArticleImageElement) articleElement);
        cardImageViewHolder.initialize();

        cardViewElement = cardImageViewHolder;
      } else if (articleElement.getClass() == ArticleRichTextElement.class) {
        CardRichTextDataView cardRichTextViewHolder = new CardRichTextDataView(context);
        cardRichTextViewHolder.setRichTextElement((ArticleRichTextElement) articleElement);
        cardRichTextViewHolder.initialize();

        cardViewElement = cardRichTextViewHolder;
      } else if (articleElement.getClass() == ArticleYoutubeVideoElement.class) {
        CardVideoView cardVideoViewHolder = new CardVideoView(context);
        cardVideoViewHolder.setArticleElement((ArticleYoutubeVideoElement) articleElement);
        cardVideoViewHolder.initialize();

        cardViewElement = cardVideoViewHolder;
      } else if (articleElement.getClass() == ArticleImageAndTextElement.class) {
        CardImageAndTextDataView cardRichTextViewHolder = new CardImageAndTextDataView(context);
        cardRichTextViewHolder.setDataElement((ArticleImageAndTextElement) articleElement);
        cardRichTextViewHolder.setFirstItem(CardImageAndTextDataView.ITEM.IMAGE);
        cardRichTextViewHolder.initialize();
      } else if (articleElement.getClass() == ArticleTextAndImageElement.class) {
        CardImageAndTextDataView cardRichTextViewHolder = new CardImageAndTextDataView(context);
        cardRichTextViewHolder.setDataElement((ArticleImageAndTextElement) articleElement);
        cardRichTextViewHolder.setFirstItem(CardImageAndTextDataView.ITEM.TEXT);
        cardRichTextViewHolder.initialize();
      }

      container.addView(cardViewElement);

      return cardViewElement;
    }
  }
}
