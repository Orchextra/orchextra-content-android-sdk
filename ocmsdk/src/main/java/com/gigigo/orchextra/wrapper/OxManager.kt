package com.gigigo.orchextra.wrapper

import android.app.Application
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver

interface OxManager {

  fun startImageRecognition()

  fun startScanner()

  fun init(application: Application, config: OxConfig, statusListener: StatusListener)

  fun finish()

  fun removeListeners()

  fun isReady(): Boolean

  fun getToken(tokenReceiver: TokenReceiver)

  fun setErrorListener(errorListener: ErrorListener)

  fun setBusinessUnits(businessUnits: List<String>, statusListener: StatusListener)

  fun bindUser(crmUser: CrmUser, statusListener: StatusListener)

  fun setCustomSchemeReceiver(customSchemeReceiver: OnCustomSchemeReceiver)

  fun onCustomScheme(customScheme: String)

  fun scanCode(scanCodeListener: ScanCodeListener)

  interface TokenReceiver {
    fun onGetToken(token: String)
  }

  interface StatusListener {
    fun onSuccess()

    fun onError(error: String)
  }

  interface ErrorListener {
    fun onError(error: String)
  }

  interface ScanCodeListener {
    fun onCodeScan(code: String)
  }
}