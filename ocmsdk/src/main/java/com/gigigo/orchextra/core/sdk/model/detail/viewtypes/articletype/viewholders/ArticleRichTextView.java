package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;


public class ArticleRichTextView extends ArticleBaseView<ArticleRichTextElement> {

  private TextView articleRichText;

  public ArticleRichTextView(Context context, ArticleRichTextElement articleElement) {
    super(context, articleElement);
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_rich_text_item;
  }

  @Override protected void bindViews() {
    articleRichText = (TextView) itemView.findViewById(R.id.article_rich_text);
  }

  @Override protected void bindTo(ArticleRichTextElement articleElement) {
    if (!TextUtils.isEmpty(articleElement.getHtml())) {
      articleRichText.setText(Html.fromHtml(articleElement.getHtml()));
    }

  }
}