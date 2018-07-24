package com.gigigo.orchextra.core.domain.entities.elements

class Element {
  var slug: String = ""
  var customProperties: Map<String, Any>? = null
  var sectionView: ElementSectionView? = null
  var elementUrl: String? = null
  var contentVersion: String? = null
  var tags: List<String>? = null
  var name: String? = null
  var dates: List<List<String>> = emptyList()
  var hasNewVersion: Boolean = false
}
