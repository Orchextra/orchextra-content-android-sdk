package com.gigigo.orchextra.core.sdk.model.grid.viewholders;

import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class CellImageViewHolder extends BaseViewHolder<CellGridContentData> {

  private final View mainLayout;
  private final View padlockView;
  private final int widthCell;

  private ImageLoader imageLoader;
  private ImageView imageView;
  private Authoritation authoritation;

  public CellImageViewHolder(Context context, ViewGroup parent, ImageLoader imageLoader,
      Authoritation authoritation, int widthCell) {
    super(context, parent, R.layout.cell_image_content_item);

    this.imageLoader = imageLoader;
    this.authoritation = authoritation;
    this.widthCell = widthCell;

    imageView = (ImageView) itemView.findViewById(R.id.cell_image_view);
    padlockView = itemView.findViewById(R.id.padlock_layout);
    mainLayout = itemView.findViewById(R.id.cell_image_content_layout);
  }

  @Override public void bindTo(CellGridContentData item, int position) {
    final ElementSectionView sectionView = item.getData().getSectionView();

    if (sectionView != null) {
      ImageGenerator.generateThumbImage(sectionView.getImageThumb(), imageView);

      byte[] imageByteArray = Base64.decode(sectionView.getImageThumb(), Base64.DEFAULT);

      String generatedImageUrl =
          ImageGenerator.generateImageUrl(sectionView.getImageUrl(), widthCell);

      imageLoader.load(generatedImageUrl).thumbnailByte(imageByteArray).into(imageView);

      System.out.println("Url grid cell: " + generatedImageUrl);
    }

    if (item.getData().getSegmentation().getRequiredAuth().equals(RequiredAuthoritation.LOGGED)) {
      padlockView.setVisibility(authoritation.isAuthorizatedUser() ? View.GONE : View.VISIBLE);
    } else {
      padlockView.setVisibility(View.GONE);
    }
  }
}
