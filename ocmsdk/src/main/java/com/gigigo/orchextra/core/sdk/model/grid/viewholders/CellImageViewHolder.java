package com.gigigo.orchextra.core.sdk.model.grid.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
  private final WeakReference<View> layerViewWR;
  private final Context context;
  private final boolean thumbnailEnabled;
  private ProgressBar progress;
  private boolean isLoading = false;

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
    layerViewWR = new WeakReference<>(itemView.findViewById(R.id.layer_view));

    progress = itemView.findViewById(R.id.notification_progress);
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

    ViewGroup layerView = (ViewGroup) layerViewWR.get();

    if (layerView != null) {
      layerView.removeAllViews();

      if (customProperties != null) {
        showLoading();
        OCManager.notifyCustomizationForContent(customProperties, ViewType.GRID_CONTENT,
            customizations -> {
              for (ViewCustomizationType viewCustomizationType : customizations) {

                if (viewCustomizationType instanceof ViewLayer) {
                  View view = ((ViewLayer) viewCustomizationType).getView();
                  layerView.addView(view);
                }
              }
              hideLoading();
              return null;
            });
      }
    }
  }

  @Override public void onClick(View v) {
    if (!isLoading) {
      super.onClick(v);
    }
  }

  private void showLoading() {
    progress.setVisibility(View.VISIBLE);
    isLoading = true;
  }

  private void hideLoading() {
    progress.setVisibility(View.GONE);
    isLoading = false;
  }
}
