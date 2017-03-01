package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleBlankView extends ArticleBaseView<ArticleBlankElement> {

  private View blankLayout;

  public ArticleBlankView(Context context, ArticleBlankElement articleElement) {
    super(context, articleElement);
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_blank_item;
  }

  @Override protected void bindViews() {
    blankLayout = itemView.findViewById(R.id.blankLayout);
  }

  @Override protected void bindTo(ArticleBlankElement articleElement) {
    RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, articleElement.getHeight());
    blankLayout.setLayoutParams(lp);
  }
}
