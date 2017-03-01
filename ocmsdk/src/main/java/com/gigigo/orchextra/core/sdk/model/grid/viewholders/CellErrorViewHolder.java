package com.gigigo.orchextra.core.sdk.model.grid.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.sdk.model.grid.dto.CellErrorElement;
import com.gigigo.orchextra.ocmsdk.R;

public class CellErrorViewHolder extends BaseViewHolder<CellErrorElement> {

  private View.OnClickListener onClickListener;
  private View retryButton;

  public CellErrorViewHolder(Context context, ViewGroup parent,
      View.OnClickListener onClickListener) {
    super(context, parent, R.layout.ocm_retry_content_item);

    retryButton = itemView.findViewById(R.id.ocm_retry_button);
    this.onClickListener = onClickListener;
  }

  @Override public void bindTo(CellErrorElement item, int position) {
    retryButton.setOnClickListener(onClickListener);
  }
}
