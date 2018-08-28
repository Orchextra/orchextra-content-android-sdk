package com.gigigo.showcase.ocm

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.gigigo.orchextra.ocm.OCManagerCallbacks
import com.gigigo.orchextra.ocm.Ocm
import com.gigigo.orchextra.ocm.OcmBuilder
import com.gigigo.orchextra.ocm.OcmStyleUiBuilder
import com.gigigo.orchextra.ocm.callbacks.OcmCredentialCallback
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver
import com.gigigo.orchextra.wrapper.CrmUser
import com.gigigo.orchextra.wrapper.OxManager
import com.gigigo.showcase.domain.entity.ConfigData
import com.gigigo.showcase.domain.entity.UserData
import com.gigigo.showcase.presentation.view.splash.SplashActivity
import timber.log.Timber
import java.util.HashMap

class ContentManager(private val context: Application) {

  private var token = ""

  fun init(configData: ConfigData, userData: UserData, onSuccess: () -> Unit = {},
      onError: () -> Unit = {}) {

    finish()

    val ocmBuilder = OcmBuilder(context).setOrchextraCredentials(configData.apiKey,
        configData.apiSecret)
        .setContentLanguage("en")
        .setBusinessUnit(configData.businessUnit)
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
//        bindUserData(userData)
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

  private fun bindUserData(userData: UserData, onSuccess: () -> Unit = {},
      onError: () -> Unit = {}) {

    Ocm.bindUser(CrmUser(userData.id, null, null), object : OxManager.StatusListener {
      override fun onSuccess() {
        val customFields = HashMap<String, String>()
        customFields["type"] = userData.type ?: ""
        customFields["level"] = userData.level ?: ""
        Ocm.setCustomFields(customFields, object : OxManager.StatusListener {
          override fun onSuccess() {
            onSuccess()
          }

          override fun onError(error: String) {
            Timber.e("setCustomFields error: %s", error)
            onError()
          }
        })
      }

      override fun onError(error: String) {
        Timber.e("bindUser error: %s", error)
        onError()
      }
    })
  }

  fun clearData(onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
    Ocm.clearData(true, true, object : OCManagerCallbacks.Clear {
      override fun onDataClearedSuccessfull() {
        onSuccess()
      }

      override fun onDataClearFails(e: Exception?) {
        Timber.e(e, "clearData()")
        onError()
      }
    })
  }

  private fun finish() {
    try {
      Ocm.stop()
    } catch (e: Exception) {
      Timber.e(e, "finish()")
    }
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