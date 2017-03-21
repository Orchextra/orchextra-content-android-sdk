package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolderFactory;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleImageView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleRichTextView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardRichTextViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardVideoViewHolder;
import com.gigigo.ui.imageloader.ImageLoader;

class CardViewHolderFactory extends BaseViewHolderFactory {

  private final ImageLoader imageLoader;

  public CardViewHolderFactory(Context context, ImageLoader imageLoader) {
    super(context);
    this.imageLoader = imageLoader;
  }

  @Override public BaseViewHolder create(Class valueClass, ViewGroup parent) {
    if (valueClass == ArticleImageElement.class) {
      return new CardImageViewHolder(context, parent, imageLoader);
    } else if (valueClass == ArticleRichTextElement.class) {
      return new CardRichTextViewHolder(context, parent);
    } else if (valueClass == ArticleVideoElement.class) {
      return new CardVideoViewHolder(context, parent);
    } else {
      return null;
    }
  }
}
