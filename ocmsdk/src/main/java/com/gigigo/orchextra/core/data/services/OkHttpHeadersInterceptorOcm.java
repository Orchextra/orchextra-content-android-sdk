package com.gigigo.orchextra.core.data.services;


import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.core.domain.entities.ocm.OxSession;
import java.io.IOException;
import java.util.Locale;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHeadersInterceptorOcm implements Interceptor {

  private static final String ACCEPT_LANGUAGE = "Accept-Language";
  private static final String X_OCM_VERSION = "X-ocm-version";
  private static final String AUTHORIZATION = "Authorization";

  private final OxSession session;

  public OkHttpHeadersInterceptorOcm(OxSession session) {
    this.session = session;
  }

  @Override public Response intercept(Chain chain) throws IOException {

    Request original = chain.request();

    Request.Builder builder = original.newBuilder();

    builder = builder.header(ACCEPT_LANGUAGE, OCManager.getContentLanguage());

    builder = builder.header(AUTHORIZATION, session.getAccessToken());

    builder = builder.header(X_OCM_VERSION, BuildConfig.OCM_SDK_VERSION);

    Request request = builder.method(original.method(), original.body()).build();

    return chain.proceed(request);
  }
}
