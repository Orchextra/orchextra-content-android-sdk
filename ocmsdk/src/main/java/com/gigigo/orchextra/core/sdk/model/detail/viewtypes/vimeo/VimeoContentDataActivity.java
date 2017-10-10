package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.vimeo;

import android.content.Context;
import android.content.Intent;
import com.gigigo.orchextra.core.sdk.di.base.BaseActivity;
@Deprecated
public class VimeoContentDataActivity extends BaseActivity {

  private static final String EXTRA_VIMEO_VIDEO_ID = "EXTRA_VIMEO_VIDEO_ID";

  public static void open(Context context, String videoId) {
    Intent intent = new Intent(context, VimeoContentDataActivity.class);
    intent.putExtra(EXTRA_VIMEO_VIDEO_ID, videoId);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   // context.startActivity(intent);
  }
}
