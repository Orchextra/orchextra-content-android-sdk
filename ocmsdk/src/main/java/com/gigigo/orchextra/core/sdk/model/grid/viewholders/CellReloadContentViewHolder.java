package com.gigigo.orchextra.core.sdk.model.grid.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.sdk.model.grid.dto.CellReloadContentElement;

public class CellReloadContentViewHolder extends BaseViewHolder<CellReloadContentElement> {

  private View.OnClickListener onClickListener;
  private View moreButton;

  public CellReloadContentViewHolder(Context context, ViewGroup parent,
      View.OnClickListener onClickListener) {
    super(context, parent, R.layout.ocm_more_content_item);

    moreButton = itemView.findViewById(R.id.ocm_more_button);
    this.onClickListener = onClickListener;
  }

  @Override public void bindTo(CellReloadContentElement item, int position) {
    moreButton.setOnClickListener(onClickListener);
  }
}
