package com.gigigo.orchextra.ocm.customProperties

import android.view.View

interface OcmCustomBehaviourDelegate {
  fun contentNeedsValidation(customProperties: Map<String, Any>, completion: (Boolean) -> Unit)
  fun customizationForContent(customProperties: Map<String, Any>,
      viewType: ViewType, customizationListener: CustomizationListener)
}

enum class ViewType {
  GRID_CONTENT,
  BUTTON_ELEMENT,
  VIDEO_ELEMENT
}

interface CustomizationListener {
  fun onGetCustomization(customizations: List<ViewCustomizationType>)
}

sealed class ViewCustomizationType

class GrayScale : ViewCustomizationType()

class DarkLayer : ViewCustomizationType()

class ViewLayer(val view: View) : ViewCustomizationType()

class ErrorMessage : ViewCustomizationType()

class Disabled : ViewCustomizationType()

class Hidden : ViewCustomizationType()