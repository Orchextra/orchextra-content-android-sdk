package com.gigigo.showcase.ocm


interface OcmWrapper {

  val isOcmInitialized: Boolean

  fun startWithCredentials(apiKey: String, apiSecret: String, businessUnit: String,
      onStartWithCredentialsCallback: OnStartWithCredentialsCallback)

  fun setUserIsAuthorizated(isUserLogged: Boolean)

  fun setContentLanguage(languageCode: String)

  fun scanCode(scanCodeListener: ScanCodeListener)

  interface OnStartWithCredentialsCallback {
    fun onCredentialReceiver(accessToken: String)

    fun onCredentailError()
  }

  interface StatusListener {
    fun onSuccess()

    fun onError(error: String)
  }

  interface ScanCodeListener {
    fun onCodeScan(code: String)
  }
}
