package com.gigigo.orchextra.ocm.customProperties

import androidx.annotation.StringRes
import android.view.View

interface OcmCustomBehaviourDelegate {
  fun contentNeedsValidation(customProperties: Map<String, Any>, viewType: ViewType?,
      completion: (Boolean) -> Unit)

  fun customizationForContent(customProperties: Map<String, Any>,
      viewType: ViewType, onGetCustomization: (List<ViewCustomizationType>) -> Unit)
}

interface OcmCustomTranslationDelegate {
  fun getTranslation(@StringRes key: Int, completion: (String?) -> Unit)
}

enum class ViewType {
  GRID_CONTENT,
  FULL_SCREEN_CONTENT,
  BUTTON_ELEMENT,
  VIDEO_ELEMENT
}

sealed class ViewCustomizationType

class GrayScale : ViewCustomizationType()

class DarkLayer : ViewCustomizationType()

class ViewLayer(val view: View) : ViewCustomizationType()

class ErrorMessage : ViewCustomizationType()

class Disabled : ViewCustomizationType()

class Hidden : ViewCustomizationType()