package com.gigigo.orchextra.wrapper

import android.app.Application
import com.gigigo.orchextra.core.Orchextra
import com.gigigo.orchextra.core.OrchextraErrorListener
import com.gigigo.orchextra.core.OrchextraOptions
import com.gigigo.orchextra.core.OrchextraStatusListener
import com.gigigo.orchextra.core.domain.entities.Error
import com.gigigo.orchextra.core.domain.entities.OxCRM
import com.gigigo.orchextra.geofence.OxGeofenceImp
import com.gigigo.orchextra.indoorpositioning.OxIndoorPositioningImp
import com.gigigo.orchextra.ocm.callbacks.OnCustomSchemeReceiver
import com.gigigo.orchextra.scanner.OxScannerImp
import com.gigigo.orchextra.wrapper.OxManager.ErrorListener
import com.gigigo.orchextra.wrapper.OxManager.ScanCodeListener
import com.gigigo.orchextra.wrapper.OxManager.StatusListener
import com.gigigo.orchextra.wrapper.OxManager.TokenReceiver

class OxManagerImpl : OxManager {

  private val orchextra = Orchextra
  private var customSchemeReceiver: OnCustomSchemeReceiver? = null
  private val genders: HashMap<CrmUser.Gender, String> = HashMap()

  init {
    genders[CrmUser.Gender.GenderFemale] = "female"
    genders[CrmUser.Gender.GenderMale] = "male"
    genders[CrmUser.Gender.GenderND] = "nd"
  }

  override fun startImageRecognition() = orchextra.openImageRecognition()

  override fun startScanner() = orchextra.openScanner()

  override fun scanCode(scanCodeListener: ScanCodeListener) = with(orchextra) {
    scanCode { scanCodeListener.onCodeScan(it) }
  }

  override fun init(application: Application, config: OxConfig, statusListener: StatusListener) {

    orchextra.setStatusListener(object : OrchextraStatusListener {
      override fun onStatusChange(isReady: Boolean) {
        if (isReady) {
          orchextra.getTriggerManager().scanner = OxScannerImp.create(application)
          orchextra.getTriggerManager().geofence = OxGeofenceImp.create(application)
          orchextra.getTriggerManager().indoorPositioning = OxIndoorPositioningImp.create(
              application)

          config.notificationActivityClass?.let {
            orchextra.setNotificationActivityClass(it)
          }

          statusListener.onSuccess()
        } else {
          statusListener.onError("SDK isn't ready")
        }
      }
    })

    orchextra.setErrorListener(object : OrchextraErrorListener {
      override fun onError(error: Error) {
        statusListener.onError(error.message)
      }
    })

    val options = OrchextraOptions.Builder().firebaseApiKey(config.firebaseApiKey)
        .firebaseApplicationId(config.firebaseApplicationId)
        .triggeringEnabled(config.triggeringEnabled)
        .anonymous(config.anonymous)
        .deviceBusinessUnits(config.deviceBusinessUnits)
        .debuggable(true)

        .build()

    orchextra.init(application, config.apiKey, config.apiSecret, options)
    orchextra.setScanTime(30)
  }

  override fun finish() = orchextra.finish()

  override fun removeListeners() = with(orchextra) {
    removeStatusListener()
    removeErrorListener()
  }

  override fun isReady(): Boolean = orchextra.isReady()

  override fun getToken(tokenReceiver: TokenReceiver) =
      orchextra.getToken { token -> tokenReceiver.onGetToken(token) }

  override fun setErrorListener(errorListener: ErrorListener) {
    orchextra.setErrorListener(object : OrchextraErrorListener {
      override fun onError(error: Error) {
        errorListener.onError(error.message)
      }
    })
  }

  override fun setBusinessUnits(businessUnits: List<String>, statusListener: StatusListener) {
    orchextra.getCrmManager().setDeviceData(null, businessUnits) { statusListener.onSuccess() }
  }

  override fun bindUser(crmUser: CrmUser, statusListener: StatusListener) {
    val crm = OxCRM(crmUser.crmId, crmUser.gender?.name, crmUser.birthdate?.time)
    orchextra.getCrmManager().bindUser(crm) { statusListener.onSuccess() }
  }

  override fun unBindUser(statusListener: StatusListener) {
    orchextra.getCrmManager().unbindUser { statusListener.onSuccess() }
  }

  override fun setCustomSchemeReceiver(customSchemeReceiver: OnCustomSchemeReceiver) {
    this.customSchemeReceiver = customSchemeReceiver
    orchextra.setCustomActionListener { customSchemeReceiver.onReceive(it) }
  }

  override fun onCustomScheme(customScheme: String) {
    customSchemeReceiver?.onReceive(customScheme)
  }
}