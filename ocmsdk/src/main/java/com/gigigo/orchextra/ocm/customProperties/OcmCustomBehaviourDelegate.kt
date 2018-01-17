package com.gigigo.orchextra.ocm.customProperties

interface OcmCustomBehaviourDelegate {
  fun contentNeedsValidation(customProperties: Map<String, Any>, completion: (Boolean) -> Unit)
  fun customizationForContent(customProperties: Map<String, Any>,
      viewType: ViewType): Array<ViewCustomizationType>
}

enum class ViewType {
  GRID_CONTENT,
  BUTTON_ELEMENT,
  VIDEO_ELEMENT
}

enum class ViewCustomizationType {
  GRAY_SCALE,
  DARK_LAYER,
  VIEW_LAYER,
  ERROR_MESSAGE,
  DISABLED,
  HIDDEN
}
