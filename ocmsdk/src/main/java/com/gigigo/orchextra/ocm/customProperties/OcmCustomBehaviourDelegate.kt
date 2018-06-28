package com.gigigo.orchextra.ocm.customProperties

import android.view.View

interface OcmCustomBehaviourDelegate{
  fun contentNeedsValidation(customProperties: Map<String, Any>, viewType: ViewType?, completion: (Boolean) -> Unit)
  fun customizationForContent(customProperties: Map<String, Any>,
      viewType: ViewType, onGetCustomization: (List<ViewCustomizationType>) -> Unit)
}

enum class ViewType {
  GRID_CONTENT,
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