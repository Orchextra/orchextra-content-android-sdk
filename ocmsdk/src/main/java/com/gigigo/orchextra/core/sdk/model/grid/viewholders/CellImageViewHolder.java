package com.gigigo.orchextra.core.sdk.model.grid.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.model.grid.dto.CellElementAdapter;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.ui.imageloader.ImageLoader;

public class CellImageViewHolder extends BaseViewHolder<CellElementAdapter> {

  private final View mainLayout;
  private final View padlockView;

  private ImageLoader imageLoader;
  private ImageView imageView;
  private Authoritation authoritation;

  public CellImageViewHolder(Context context, ViewGroup parent, ImageLoader imageLoader,
      Authoritation authoritation) {
    super(context, parent, R.layout.cell_image_content_item);

    this.imageLoader = imageLoader;
    this.authoritation = authoritation;

    imageView = (ImageView) itemView.findViewById(R.id.cell_image_view);
    padlockView = itemView.findViewById(R.id.padlock_layout);
    mainLayout = itemView.findViewById(R.id.cell_image_content_layout);
  }

  @Override public void bindTo(CellElementAdapter item, int position) {
    final ElementSectionView sectionView = item.getData().getSectionView();

    ImageGenerator.generateThumbImage(sectionView.getImageThumb(), imageView);

    mainLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        String generatedImageUrl =
            ImageGenerator.generateImageUrl(sectionView.getImageUrl(), mainLayout.getWidth(),
                mainLayout.getHeight());

        imageLoader.load(generatedImageUrl)
            .into(imageView)
            .override(mainLayout.getWidth(), mainLayout.getHeight())
            .build();

        mainLayout.getViewTreeObserver().removeOnPreDrawListener(this);

        return true;
      }
    });

    if (item.getData()
        .getSegmentation()
        .getRequiredAuth()
        .equals(RequiredAuthoritation.LOGGED)) {
      padlockView.setVisibility(authoritation.isAuthorizatedUser() ? View.GONE : View.VISIBLE);
    } else {
      padlockView.setVisibility(View.GONE);
    }
  }
}
