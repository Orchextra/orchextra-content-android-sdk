package com.gigigo.orchextra.core.sdk.model.grid.factory;

import android.content.Context;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolderFactory;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.model.grid.viewholders.CellImageViewHolder;
import com.gigigo.ui.imageloader.ImageLoader;

public class ElementsViewHolderFactory extends BaseViewHolderFactory {

  private final ImageLoader imageLoader;
  private final Authoritation authoritation;
  private final int widthCell;

  public ElementsViewHolderFactory(Context context, ImageLoader imageLoader,
      Authoritation authoritation, int widthCell) {
    super(context);
    this.imageLoader = imageLoader;
    this.authoritation = authoritation;
    this.widthCell = widthCell;
  }

  @Override public BaseViewHolder create(Class valueClass, ViewGroup parent) {
    if (valueClass == CellGridContentData.class) {
      return new CellImageViewHolder(context, parent, imageLoader, authoritation, widthCell);
    } else if (valueClass == CellBlankElement.class) {
      return new CellBlankViewHolder(context, parent);
    } else {
      return null;
    }
  }
}
