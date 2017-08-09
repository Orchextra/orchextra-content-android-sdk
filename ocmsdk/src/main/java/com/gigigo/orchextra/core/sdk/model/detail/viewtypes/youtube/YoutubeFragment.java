package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.glide.transformations.BlurTransformation;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import java.lang.ref.WeakReference;

      public class YoutubeFragment extends UiBaseContentData {

        private static final String EXTRA_PLAYED_VIDEO = "EXTRA_PLAYED_VIDEO";
        private static final String EXTRA_IS_PLAYING = "EXTRA_IS_PLAYING";
        private static final String YOUTUBE_FRAGMENT = "YOUTUBE_FRAGMENT";
        private static final String EXTRA_YOUTUBE_ID = "EXTRA_YOUTUBE_ID";

        private RelativeLayout youtubeLayoutContainer;
        private ImageView imgBlurBackground;

        private String youtubeId;
        private BitmapImageViewTarget mImageCallback;
        private int playedVideo;
        private boolean isPlaying;

        YouTubePlayer.OnInitializedListener onInitializedListener =
            new YouTubePlayer.OnInitializedListener() {

              @Override
              public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                  boolean wasRestored) {

                if (!wasRestored) {
                  setYouTubePlayer(player);
                }
              }

              @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
                  YouTubeInitializationResult error) {
              }
            };

        public static YoutubeFragment newInstance(String youtubeId) {
          YoutubeFragment youtubeElements = new YoutubeFragment();

          Bundle bundle = new Bundle();
          bundle.putString(EXTRA_YOUTUBE_ID, youtubeId);
          youtubeElements.setArguments(bundle);

          return youtubeElements;
        }

        @Override public void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          setRetainInstance(true);
        }

        @Nullable @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

          View mView = inflater.inflate(R.layout.view_youtube_elements_item, container, false);

          initViews(mView);

          mImageCallback = new BitmapImageViewTarget(imgBlurBackground) {

            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
              WeakReference<Bitmap> resizedbitmap = new WeakReference<>(Bitmap.createBitmap(bitmap, 0, 45, 480, 270));
              imgBlurBackground.setImageBitmap(resizedbitmap.get());
            }
          };

          initThumbnail();
          initYoutubeFragment();

          return mView;
        }

        private void initViews(View view) {
          youtubeLayoutContainer = (RelativeLayout) view.findViewById(R.id.youtubeLayoutContainer);

          imgBlurBackground = (ImageView) view.findViewById(R.id.imgBlurBackground);

          youtubeLayoutContainer.getViewTreeObserver()
              .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override public void onGlobalLayout() {
                  FrameLayout.LayoutParams lp =
                      new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                          FrameLayout.LayoutParams.MATCH_PARENT);

                  youtubeLayoutContainer.setLayoutParams(lp);
                  if (AndroidSdkVersion.hasJellyBean16()) {
                    youtubeLayoutContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                  }
                }
              });
        }

        private void initThumbnail() {
          youtubeId = getArguments().getString(EXTRA_YOUTUBE_ID);

          String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";
          Glide.with(this)
              .load(strImgForBlur)
              .asBitmap()
              .transform(new BlurTransformation(YoutubeFragment.this.getActivity(), 20))
              .into(mImageCallback);
        }

        private void initYoutubeFragment() {
          try {
            YouTubePlayerFragment youTubePlayerFragment2 = YouTubePlayerFragment.newInstance();
            youTubePlayerFragment2.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);

            if (this.getActivity() != null && !this.getActivity().isFinishing()) {
              getChildFragmentManager().beginTransaction()
                  .replace(R.id.youtubePlayerFragmentContent, youTubePlayerFragment2, YOUTUBE_FRAGMENT)
                  .commitAllowingStateLoss();
            }
          } catch (Exception ignored) {
          }
        }

        public void setYouTubePlayer(final YouTubePlayer player) {
          try {
            if (player == null) {
              return;
            }

            player.setShowFullscreenButton(true);
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

            if (playedVideo >= 0) {
              if (playedVideo == 0 || isPlaying) {
                player.loadVideo(youtubeId, playedVideo);
              } else {
                player.cueVideo(youtubeId, playedVideo);
              }
            }
          } catch (Exception ignored) {
          }
        }

        @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);

          if (savedInstanceState != null) {
            playedVideo = savedInstanceState.getInt(EXTRA_PLAYED_VIDEO);
            isPlaying = savedInstanceState.getBoolean(EXTRA_IS_PLAYING);
          }
        }

        @Override public void onSaveInstanceState(Bundle outState) {
          try {
            YouTubePlayerFragment youTubePlayerSupportFragment =
                (YouTubePlayerFragment) getChildFragmentManager().findFragmentByTag(YOUTUBE_FRAGMENT);
            YouTubePlayer mPlayer = youTubePlayerSupportFragment.getPlayer();

            if (mPlayer != null) {
              outState.putInt(EXTRA_PLAYED_VIDEO, mPlayer.getCurrentTimeMillis());
              outState.putBoolean(EXTRA_IS_PLAYING, mPlayer.isPlaying());
            }
          } catch (Exception ignored) {
          }

          super.onSaveInstanceState(outState);
        }
      }
