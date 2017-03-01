package com.gigigo.orchextra.core.sdk.model.grid.factory;

import android.content.Context;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolderFactory;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.viewholder.CellBlankViewHolder;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.model.grid.dto.CellElementAdapter;
import com.gigigo.orchextra.core.sdk.model.grid.viewholders.CellImageViewHolder;
import com.gigigo.ui.imageloader.ImageLoader;

public class ElementsViewHolderFactory extends BaseViewHolderFactory {

  private final ImageLoader imageLoader;
  private final Authoritation authoritation;

  public ElementsViewHolderFactory(Context context, ImageLoader imageLoader, Authoritation authoritation) {
    super(context);
    this.imageLoader = imageLoader;
    this.authoritation = authoritation;
  }

  @Override public BaseViewHolder create(Class valueClass, ViewGroup parent) {
    if (valueClass == CellElementAdapter.class) {
      return new CellImageViewHolder(context, parent, imageLoader, authoritation);
    } else if (valueClass == CellBlankElement.class) {
      return new CellBlankViewHolder(context, parent);
      /** Not used for a library error */
    //} else if (valueClass == CellReloadContentElement.class) {
    //  return new CellReloadContentViewHolder(context, parent, null);
    //} else if (valueClass == CellErrorElement.class) {
    //  return new CellErrorViewHolder(context, parent, null);
    } else {
      return null;
    }
  }
}
