package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBaseView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBlankView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleButtonView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleHeaderView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleImageView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleRichTextView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;

public class ArticleItemViewContainer extends LinearLayout {

  private final Context context;
  private LinearLayout articleListContainer;
  private boolean thumbnailEnabled;

  public ArticleItemViewContainer(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public ArticleItemViewContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public ArticleItemViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ArticleItemViewContainer(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.context = context;

    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_article_item_list_layout, this, true);

    articleListContainer = (LinearLayout) view.findViewById(R.id.articleListContainer);
  }

  public void addArticleElementList(List<ArticleElement> articleElementList) {
    if (articleElementList != null) {
      for (ArticleElement articleElement : articleElementList) {
        ArticleBaseView articleBaseView = create(articleElement);

        if (articleBaseView != null) {
          articleListContainer.addView(articleBaseView);
        }
      }
    }
  }

  //todo change by factory
  public ArticleBaseView create(ArticleElement articleElement) {
    Class<? extends ArticleElement> valueClass = articleElement.getClass();

    if (valueClass == ArticleHeaderElement.class) {
      return new ArticleHeaderView(context, (ArticleHeaderElement) articleElement,
          thumbnailEnabled);
    } else if (valueClass == ArticleImageElement.class) {
      return new ArticleImageView(context, (ArticleImageElement) articleElement, thumbnailEnabled);
    } else if (valueClass == ArticleRichTextElement.class) {
      return new ArticleRichTextView(context, (ArticleRichTextElement) articleElement);
    } else if (valueClass == ArticleVideoElement.class) {
      return new ArticleVideoView(context, (ArticleVideoElement) articleElement);
    } else if (valueClass == ArticleButtonElement.class) {
      return new ArticleButtonView(context, (ArticleButtonElement) articleElement);
    } else if (valueClass == ArticleBlankElement.class) {
      return new ArticleBlankView(context, (ArticleBlankElement) articleElement);
    } else {
      return null;
    }
  }

  public void setThumbnailEnabled(boolean thumbnailEnabled) {
    this.thumbnailEnabled = thumbnailEnabled;
  }
}
