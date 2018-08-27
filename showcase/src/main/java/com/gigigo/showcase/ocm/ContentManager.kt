package com.gigigo.showcase.ocm

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.gigigo.orchextra.ocm.Ocm
import com.gigigo.orchextra.ocm.OcmBuilder
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver
import com.gigigo.orchextra.wrapper.OxManager
import com.gigigo.showcase.splash.SplashActivity
import timber.log.Timber

class ContentManager(private val context: Application) {

  private var token = ""

  fun init(apiKey: String, apiSecret: String, businessUnit: String, onSuccess: () -> Unit = {},
      onError: () -> Unit = {}) {

    val ocmBuilder = OcmBuilder(context).setOrchextraCredentials(apiKey, apiSecret)
        .setContentLanguage("en")
        .setBusinessUnit(businessUnit)
        .setTriggeringEnabled(false)
        .setAnonymous(true)
        .setProximityEnabled(false)
        .setNotificationActivityClass(SplashActivity::class.java)

    Ocm.initialize(ocmBuilder, object : OcmCredentialCallback {
      override fun onCredentialReceiver(accessToken: String) {
        Timber.d("onCredentialReceiver")

        Ocm.setErrorListener(object : OxManager.ErrorListener {
          override fun onError(error: String) {
            Timber.e("Ox error: %s", error)
            onError()
          }
        })

        Ocm.setOnCustomSchemeReceiver(onCustomSchemeReceiver)

        val ocmStyleUiBuilder = OcmStyleUiBuilder()
            .setTitleToolbarEnabled(false)
            .disableThumbnailImages()
            .setEnabledStatusBar(false)
        Ocm.setStyleUi(ocmStyleUiBuilder)

        token = accessToken
        onSuccess()

      }

      override fun onCredentailError(code: String) {
        Timber.e("Error on init Ox: $code")
        if (!code.contains("tr.")) {
          onError()
        }
      }
    })
  }

  private val onCustomSchemeReceiver = OnCustomSchemeReceiver { scheme ->
    try {
      Timber.d("CustomScheme: %s", scheme)
      val intent = Intent()
      intent.action = "android.intent.action.VIEW"

      val deeplink = Uri.parse(scheme)
      intent.data = deeplink

      TODO("implement deeplinkHandler")
//      deeplinkHandler.processDeeplink(intent)

    } catch (exception: Exception) {
      Timber.e(exception, "onCustomSchemeReceiver")
    }
  }
}