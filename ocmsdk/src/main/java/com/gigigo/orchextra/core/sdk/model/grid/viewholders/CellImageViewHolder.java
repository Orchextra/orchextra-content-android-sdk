package com.gigigo.orchextra.core.sdk.model.grid.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.customProperties.ViewCustomizationType;
import com.gigigo.orchextra.ocm.customProperties.ViewLayer;
import com.gigigo.orchextra.ocm.customProperties.ViewType;
import com.gigigo.orchextra.ocmsdk.R;
import java.lang.ref.WeakReference;
import java.util.Map;

public class CellImageViewHolder extends BaseViewHolder<CellGridContentData> {

  private final WeakReference<View> mainLayoutWeakReference;
  private final Context context;
  private final boolean thumbnailEnabled;

  private WeakReference<ImageView> imageViewWeakReference;
  private WeakReference<ImageView> imageViewOverlayWeakReference;

  public CellImageViewHolder(Context context, ViewGroup parent, boolean thumbnailEnabled) {
    super(context, parent, R.layout.cell_image_content_item);

    this.context = context;
    this.thumbnailEnabled = thumbnailEnabled;

    new WeakReference<>(itemView);
    imageViewOverlayWeakReference =
        new WeakReference<>(itemView.findViewById(R.id.cell_image_view_overlay));
    imageViewWeakReference = new WeakReference<>(itemView.findViewById(R.id.cell_image_view));
    mainLayoutWeakReference =
        new WeakReference<>(itemView.findViewById(R.id.cell_image_content_layout));
  }

  @Override public void bindTo(CellGridContentData item, final int position) {
    final ElementSectionView sectionView = item.getData().getSectionView();
    final Map<String, Object> customProperties = item.getData().getCustomProperties();

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
                      .dontAnimate();

                  Transformation<Bitmap> bitmapTransformReadArticles =
                      OCManager.getBitmapTransformReadArticles();
                  if (bitmapTransformReadArticles != null) {
                    requestBuilder.bitmapTransform(bitmapTransformReadArticles);
                  } else {
                    //todo overlay, image.setForeground only api23, or ovelary with other image
                    //now change de image for test
                    ImageView imageViewOverlay = imageViewOverlayWeakReference.get();
                    imageViewOverlay.setVisibility(View.VISIBLE);
                  }
                }

                if (thumbnailEnabled && sectionView.getImageThumb() != null) {
                  byte[] imageByteArray =
                      Base64.decode(sectionView.getImageThumb(), Base64.DEFAULT);
                  requestBuilder =
                      requestBuilder.thumbnail(Glide.with(context).load(imageByteArray));
                }

                requestBuilder.into(imageView);
              }

              mainLayout.getViewTreeObserver().removeOnPreDrawListener(this);

              return true;
            }
          });
    }

    ViewGroup mainView = (ViewGroup) mainLayoutWeakReference.get();

    ViewCustomizationType[] viewCustomizationTypes = OCManager.getOcmCustomBehaviourDelegate()
        .customizationForContent(customProperties, ViewType.GRID_CONTENT);

    for (ViewCustomizationType viewCustomizationType : viewCustomizationTypes) {
      if (viewCustomizationType instanceof ViewLayer) {
        View view = ((ViewLayer) viewCustomizationType).getView();

        if (mainView != null) {
          mainView.addView(view);
        }
      }
    }
  }
}
