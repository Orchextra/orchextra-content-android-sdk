package com.gigigo.orchextra.core.sdk.model.grid.viewholders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.R;
import java.lang.ref.WeakReference;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public class CellImageViewHolder extends BaseViewHolder<CellGridContentData> {

  private final WeakReference<View> mainLayoutWeakReference;
  private final WeakReference<View> padlockViewWeakReference;
  private final Context context;

  private WeakReference<ImageView> imageViewWeakReference;
  private Authoritation authoritation;

  public CellImageViewHolder(Context context, ViewGroup parent, Authoritation authoritation) {
    super(context, parent, R.layout.cell_image_content_item);

    this.context = context;
    this.authoritation = authoritation;

    new WeakReference<>(itemView);

    imageViewWeakReference =
        new WeakReference<>((ImageView) itemView.findViewById(R.id.cell_image_view));
    padlockViewWeakReference = new WeakReference<>(itemView.findViewById(R.id.padlock_layout));
    mainLayoutWeakReference =
        new WeakReference<>(itemView.findViewById(R.id.cell_image_content_layout));
  }

  @Override public void bindTo(CellGridContentData item, final int position) {
    final ElementSectionView sectionView = item.getData().getSectionView();

    if (sectionView != null) {

      mainLayoutWeakReference.get()
          .getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override public boolean onPreDraw() {
              ImageView imageView = imageViewWeakReference.get();
              View mainLayout = mainLayoutWeakReference.get();

              if (imageView != null) {

                String generatedImageUrl =
                    ImageGenerator.generateImageUrl(sectionView.getImageUrl(),
                        mainLayout.getWidth(), mainLayout.getHeight());
                DrawableRequestBuilder<String> requestBuilder;

                if (!OCManager.isThisArticleReaded(item.getData().getSlug())) {
                  requestBuilder = OcmImageLoader.load(context, generatedImageUrl)
                      .priority(Priority.NORMAL)
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .dontAnimate();
                } else {
                  requestBuilder = OcmImageLoader.load(context, generatedImageUrl)
                      .priority(Priority.NORMAL)
                      .diskCacheStrategy(DiskCacheStrategy.ALL)
                      .bitmapTransform(new GrayscaleTransformation(context))
                      .dontAnimate();
                }
                //if (thumbnailEnabled) {
                //requestBuilder = requestBuilder.thumbnail(Glide.with(context).load(imageByteArray));
                //}

                requestBuilder.into(imageView);
              }

              mainLayout.getViewTreeObserver().removeOnPreDrawListener(this);

              return true;
            }
          });
    }

    View padlockView = padlockViewWeakReference.get();

    if (padlockView != null) {
      if (item.getData().getSegmentation().getRequiredAuth().equals(RequiredAuthoritation.LOGGED)) {
        padlockView.setVisibility(authoritation.isAuthorizatedUser() ? View.GONE : View.VISIBLE);
      } else {
        padlockView.setVisibility(View.GONE);
      }
    }
  }
}
