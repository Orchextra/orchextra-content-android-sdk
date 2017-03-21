package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.cards.ElementCachePreviewCard;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class PreviewCardContentData extends UiBaseContentData {

  private AppCompatActivity activity;

  private ViewPager cardViewPager;
  private PreviewCardPagerAdapter pagerAdapter;
  private ImageLoader imageLoader;
  private ElementCachePreviewCard content;
  private ElementCacheShare share;

  public static PreviewCardContentData newInstance() {
    return new PreviewCardContentData();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    this.activity = (AppCompatActivity) context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_preview_cards_view_pager, container, false);

    init(view);

    return view;
  }

  private void init(View view) {
    initViews(view);
  }

  private void initViews(View view) {
    cardViewPager = (ViewPager) view.findViewById(R.id.cardViewPager);
  }

  private void initViewPager() {
    pagerAdapter = new PreviewCardPagerAdapter(activity.getSupportFragmentManager(), imageLoader);

    cardViewPager.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            //ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
            ViewGroup.LayoutParams layoutParams = cardViewPager.getLayoutParams();
            layoutParams.height = DeviceUtils.calculateRealHeightDevice(activity);
            cardViewPager.setLayoutParams(layoutParams);
          }
        });
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    initViewPager();
    setContentInViewPager();
  }

  private void setContentInViewPager() {
    pagerAdapter.setPreviewList(content);
    pagerAdapter.setShareContent(share);
    cardViewPager.setAdapter(pagerAdapter);
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setPreview(ElementCachePreviewCard content) {
    this.content = content;
  }

  public void setShare(ElementCacheShare share) {
    this.share = share;
  }
}
