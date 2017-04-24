package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class ArticleButtonView extends ArticleBaseView<ArticleButtonElement> {

  private final ImageLoader imageLoader;

  private TextView articleTextButton;
  private ImageView articleImageButton;

  public ArticleButtonView(Context context, ArticleButtonElement articleElement,
      ImageLoader imageLoader) {
    super(context, articleElement);
    this.imageLoader = imageLoader;
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_button_item;
  }

  @Override protected void bindViews() {
    articleTextButton = (TextView) itemView.findViewById(R.id.articleTextButton);
    articleImageButton = (ImageView) itemView.findViewById(R.id.articleImageButton);
  }

  @Override protected void bindTo(ArticleButtonElement articleElement) {
    switch (articleElement.getType()) {
      case IMAGE:
        bindImageButton(articleElement);
        break;
      case DEFAULT:
        bindTextButton(articleElement);
    }
  }

  private void bindTextButton(final ArticleButtonElement articleElement) {
    //TODO Check 'size' value
    articleTextButton.setVisibility(VISIBLE);

    articleTextButton.setText(articleElement.getText());

    try {
      articleTextButton.setTextColor(Color.parseColor(articleElement.getTextColor()));
      articleTextButton.setBackgroundColor(Color.parseColor(articleElement.getBgColor()));
    } catch (Exception ignored) {
    }

    articleTextButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        processClickListener(articleElement.getElementUrl());
      }
    });
  }

  private void bindImageButton(final ArticleButtonElement articleElement) {
    articleImageButton.setVisibility(VISIBLE);

    imageLoader.load(articleElement.getImageUrl()).into(articleImageButton);

    articleImageButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        processClickListener(articleElement.getElementUrl());
      }
    });
  }

  private void processClickListener(String elementUrl) {
    if (elementUrl != null) {
      Ocm.processDeepLinks(elementUrl);
    }
  }
}
