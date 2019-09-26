package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleBlankView extends BaseViewHolder<ArticleBlankElement> {

  private View blankLayout;

  public ArticleBlankView(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_article_blank_item);

    blankLayout = itemView.findViewById(R.id.blankLayout);
  }

  @Override public void bindTo(ArticleBlankElement articleBlankElement, int i) {
    RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, articleBlankElement.getHeight());
    blankLayout.setLayoutParams(lp);
  }
}
