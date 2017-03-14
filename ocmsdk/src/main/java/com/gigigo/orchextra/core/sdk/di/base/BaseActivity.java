package com.gigigo.orchextra.core.sdk.di.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gigigo.ggglib.device.AndroidSdkVersion;

public class BaseActivity extends AppCompatActivity {
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (AndroidSdkVersion.hasJellyBean16()) {
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
  }
}
