package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.domain.entities.elements.Element
import com.gigigo.orchextra.ocmsdk.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ElementComparator : Comparator<Element> {


  override fun compare(element1: Element, element2: Element): Int {
    return getStartDate(element2).compareTo(getStartDate(element1))
  }

  private fun getStartDate(element: Element): Date {
    return try {
      val stringDate = element.dates[element.dates.size - 1][0]
      val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
      df.parse(stringDate)
    } catch (e: Exception) {
      Date()
    }
  }
}