package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.domain.entities.elements.Element
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ElementFilter {

  fun removeFinishedElements(elements: List<Element>): List<Element> {
    return elements.filter { element ->
      !isElementFinished(element)
    }
  }

  private fun isElementFinished(element: Element): Boolean {

    return try {
      val stringDate = element.dates[element.dates.size - 1][1]
      val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

      val now = Date()
      val endDate = df.parse(stringDate)

      now.time > endDate.time

    } catch (e: Exception) {
      Timber.e(e, "isElementFinished()")
      true
    }
  }
}