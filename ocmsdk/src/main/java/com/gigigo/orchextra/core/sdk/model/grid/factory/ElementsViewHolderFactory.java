package com.gigigo.orchextra.core.sdk.model.grid.factory;

import android.content.Context;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolderFactory;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.sdk.model.grid.viewholders.CellImageViewHolder;

public class ElementsViewHolderFactory extends BaseViewHolderFactory {

  private final boolean thumbnailEnabled;

  public ElementsViewHolderFactory(Context context, boolean thumbnailEnabled) {
    super(context);
    this.thumbnailEnabled = thumbnailEnabled;
  }

  @Override public BaseViewHolder create(Class valueClass, ViewGroup parent) {
    if (valueClass == CellGridContentData.class) {

      return new CellImageViewHolder(context, parent, thumbnailEnabled);
    } else if (valueClass == CellBlankElement.class) {
      return new CellBlankViewHolder(context, parent);
    } else {
      return null;
    }
  }
}
