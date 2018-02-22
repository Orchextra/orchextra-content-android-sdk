package com.gigigo.orchextra.core.domain.entities.elements

class Element {

  var customProperties: Map<String, Any>? = null
  var sectionView: ElementSectionView? = null
  var slug: String? = null
  var elementUrl: String? = null
  var tags: List<String>? = null
  var name: String? = null
  var dates: List<List<String>>? = null
}
