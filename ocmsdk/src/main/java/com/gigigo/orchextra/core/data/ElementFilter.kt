package com.gigigo.orchextra.core.data

import com.gigigo.orchextra.core.domain.entities.elements.Element
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

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
      df.timeZone = TimeZone.getTimeZone("GMT")

      val calendar = Calendar.getInstance()
      val endDate = Calendar.getInstance()
      endDate.time = df.parse(stringDate)

      calendar.timeInMillis > endDate.timeInMillis

    } catch (e: Exception) {
      Timber.e(e, "isElementFinished()")
      true
    }
  }
}