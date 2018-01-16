package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolderFactory;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBlankView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleButtonView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleHeaderView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleImageView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleRichTextView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVimeoVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleYoutubeVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;

public class ArticleContentDataFactory extends BaseViewHolderFactory {

  private final boolean thumbnailEnabled;

  public ArticleContentDataFactory(Context context, boolean thumbnailEnabled) {
    super(context);
    this.thumbnailEnabled = thumbnailEnabled;
  }

  @Override public BaseViewHolder create(Class valueClass, ViewGroup parent) {
    if (valueClass == ArticleYoutubeVideoElement.class) {
      return new ArticleYoutubeVideoView(context, parent);
    } else if (valueClass == ArticleVimeoVideoElement.class) {
      return new ArticleVimeoVideoView(context, parent);
    } else if (valueClass == ArticleRichTextElement.class) {
      return new ArticleRichTextView(context, parent);
    } else if (valueClass == ArticleImageElement.class) {
      return new ArticleImageView(context, parent, thumbnailEnabled);
    } else if (valueClass == ArticleHeaderElement.class) {
      return new ArticleHeaderView(context, parent, thumbnailEnabled);
    } else if (valueClass == ArticleButtonElement.class) {
      return new ArticleButtonView(context, parent);
    } else if (valueClass == ArticleBlankElement.class) {
      return new ArticleBlankView(context, parent);
    } else {
      return null;
    }
  }
}
